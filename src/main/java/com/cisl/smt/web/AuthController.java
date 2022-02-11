package com.cisl.smt.web;

import com.alibaba.fastjson.JSON;
import com.cisl.smt.po.User;
import com.cisl.smt.service.UserService;
import com.cisl.smt.web.Temp.AuthInfo;
import com.cisl.smt.web.Temp.UserMap;
import com.cisl.smt.web.Temp.UserTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class AuthController {
    /**
     * @description: 登录与注册的控制器
     * @author: Hopenx
     * @date: 2020-10-22 21:36
     */

    @Autowired
    private UserService userService;

    public static String sendPost(String urlString, String jsonInputString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println("登录接口返回: " + response.toString());
            return response.toString();
        }

    }

    @GetMapping("/test")
    public ModelAndView test() {
        return new ModelAndView("test");
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping("/auth")
    public ModelAndView auth(@RequestParam("token") String token, HttpServletResponse response) {
        HashMap<String, UserTemp> map = UserMap.getInstance().getTempHashMap();
        ModelAndView modelAndView = new ModelAndView("auth");   //成功就跳转到 start
        try {
            //每次重启服务器 map 都会清空。所幸只是临时 token
            UserTemp userTemp = map.get(token);
            String user_id = userTemp.getUser_id();
            String user_seq = userTemp.getUser_seq();
            String level = userTemp.getLevel();
            Cookie cookie = new Cookie("user_id", user_id);
            response.addCookie(cookie);
            cookie = new Cookie("user_seq", user_seq);
            response.addCookie(cookie);
            cookie = new Cookie("level", level);
            response.addCookie(cookie);

        } catch (Exception e) {
            e.printStackTrace();
            modelAndView = new ModelAndView("login");   //失败就需要登录
        }

        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView("register");
        return modelAndView;
    }

    @PostMapping("/postRegister")
    public String postRegister(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("re_password") String re_password,
                               HttpServletResponse response) {

        System.out.println(userService.getUserByUsername(username));
        if (userService.getUserByUsername(username) != null) {
            System.out.println("exist");
            return "Exist";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.saveUser(user);
        User tmp = userService.getUserByUsername(username);
        Long user_id = tmp.getUser_id();
        Cookie cookie = new Cookie("user_id", user_id.toString());
        response.addCookie(cookie);

        return user.toString();
    }

    @PostMapping("/postLogin")
    public String postLogin(@RequestParam("institute_seq") String institute_seq,
                            @RequestParam("user_id") String user_id,
                            @RequestParam("passwd") String passwd,
                            HttpServletResponse response) {

        System.out.println(String.format("请求参数: {\"user_id\":\"%s\",\"passwd\":\"%s\",\"institute_seq\":\"%s\"}", user_id, passwd, institute_seq));
        try {
            // 仅供内部测试使用，绕过登录验证
            if(institute_seq.equals("1111") && user_id.equals("1111") && passwd.equals("1111")) {
                return "OK";
            }
            String sr = sendPost("https://interface.smartreelearners.com:8442/api/app/auth/student/login/china",
                    String.format("{\"user_id\":\"%s\",\"passwd\":\"%s\",\"institute_seq\":\"%s\"}", user_id, passwd, institute_seq));
            HashMap hashMap = JSON.parseObject(sr, HashMap.class);

            if (hashMap.containsKey("error")) {
                if (hashMap.get("error").equals(1))
                    return "Fail";
                else if (hashMap.get("error").equals(2))
                    return "OK";
            }
            if (hashMap.containsKey("student_info")) {
                String infoString = hashMap.get("student_info").toString();
                HashMap infoMap = JSON.parseObject(infoString, HashMap.class);
                String level = infoMap.get("level").toString();

                return "OK " + infoMap.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Fail";
        }

        return "Fail";

    }

    @PostMapping("/postLogin0")
    public String postLogin0(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             HttpServletResponse response) {

        User tmp = userService.getUserByUsername(username);

        if (tmp == null) {
            return "Fail";
        }

        if (password.equals(tmp.getPassword())) {     //比较字符串必须用 equals
            Long user_id = tmp.getUser_id();
            Cookie cookie = new Cookie("user_id", user_id.toString());
            response.addCookie(cookie);
            return "OK";
        } else return "Fail";

    }

    @PostMapping(value = "/setCookie", produces = "application/json;charset=UTF-8")
    public String setCookie(@RequestBody AuthInfo authInfo,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        try {
            // 不允许有多个头
//            response.addHeader("Access-Control-Allow-Origin", "https://www.smartreelearners.com");
//            response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
            HashMap<String, UserTemp> map = UserMap.getInstance().getTempHashMap();    //全局唯一 user 映射, token 对应 UserTemp
            map.put(authInfo.getToken(), new UserTemp(authInfo.getUser_id(), authInfo.getUser_seq(), authInfo.getLevel()));
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail";
        }

        return "OK";
    }
}

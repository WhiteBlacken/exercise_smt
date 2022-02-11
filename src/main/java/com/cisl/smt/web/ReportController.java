package com.cisl.smt.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cisl.smt.dao.ExerciseEvalRepository;
import com.cisl.smt.dao.ProblemEvalRepository;
import com.cisl.smt.dao.ProblemRepository;
import com.cisl.smt.po.*;
import com.cisl.smt.service.*;
import com.cisl.smt.web.Temp.*;
import com.cisl.smt.web.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.*;
import java.security.KeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ReportController {
    @Autowired
    private ProblemEvalRepository problemEvalRepository;

    @Autowired
    private ExerciseEvalRepository exerciseEvalRepository;

    @Autowired
    private ReviewCollecService reviewCollecService;

    private final Util util = new Util();  //工具库变量
    private final String base_url = "https://interface.smartreelearners.com:8442/api/app";
    // 为 contrast 接口设置
    private int user_exercise_avg = 0;
    private int user_vocal_avg = 0;
    private int all_exercise_avg = 0;

    public static String sendGet(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
        con.setRequestMethod("GET");
        con.setRequestProperty("Cookie", "JSESSIONID=42E5648EF67ABD3B58690E9465058632");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "*/*");
        con.setRequestProperty("Connection", "keep-alive");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)");
//        con.setRequestProperty("Host", "106.15.225.46");  host 会变 应该不需要设置

        con.setDoOutput(true);
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println("-----sendGet-----");
            System.out.println("[URL]: " + urlString);
            System.out.println("[返回]: " + response.toString());
            System.out.println("------------------\n");
            return response.toString();
        }
    }

    /*
    2021.12.25 bug 解决
    javax.net.ssl.SSLHandshakeException: Received fatal alert: handshake_failure 客户端与服务端的 TSL 版本不一致
    通过 VM Option -Djavax.net.debug=all 查看 TSL 版本是否对应
    通过 VM Option -Dhttps.protocols=TLSv1.2 设置成 1.2 版本
     */
    public static String sendPost(String urlString, String jsonInputString, String line) throws IOException {
        String JSESSIONID = "8F3459B28E58F01BA8A22A512259DD1A";
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        /*
        2021.12.25 bug 修复
        登录后依然提示未登录，在 header 中添加 Cookie JSESSIONID 后解决
        难点：JSESSIONID会过期 需要更新
         */

        con.setRequestProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
        con.setRequestMethod("POST");
        con.setRequestProperty("Cookie", "JSESSIONID=" + JSESSIONID);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "*/*");
        con.setRequestProperty("Connection", "keep-alive");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)");
//        con.setRequestProperty("Host", "106.15.225.46");  host 会变 应该不需要设置

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
            System.out.println("-----sendPost-----" + line);
            System.out.println("[URL]: " + urlString);
            System.out.println("[返回]: " + response.toString());
            System.out.println("------------------\n");
            return response.toString();
        }
    }

    public String fetchRemoteAPI(String urlString, String jsonInputString, String field) throws IOException {
        if (!loginReport()) return null;
        String responseJsonString = sendPost(urlString, jsonInputString, "130");
        JSONObject jsonObject = JSONObject.parseObject(responseJsonString);
        System.out.println("urlString + field + jsonObject: " + urlString + field + jsonObject.toString());
        return jsonObject.get(field).toString();
    }

    @GetMapping("/loginReport")
    public boolean loginReport() throws IOException {
        String url = base_url + "/auth/student/login/china?user_id=9999&institute_seq=31&passwd=1111";
        String ret = sendPost(url, "{\"user_id\":\"9999\", \"passwd\":\"1111\", \"institute_seq\":\"31\" }", "139");
        return ret.contains("student_info");
    }

    @GetMapping("/getStudentInfo")
    public JsonResult<Map<String, Object>> getStudentInfo(@RequestParam("user_id") String user_id) throws IOException {
        /**
         * @description: 获取学生基本信息
         * @return: JSON 数据
         */
        String url = base_url + "/student/study/getStudentinfo?user_seq=" + user_id;
        String field = "list";  // 想要读取的字段
        JSONObject jsonObject;
        try {
            String fieldString = fetchRemoteAPI(url, "", field);
            jsonObject = JSONObject.parseObject(fieldString);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonResult<>("-1", "操作错误");
        }
        Map<String, Object> map = new HashMap<>();
        try {
            // 学生 level 9990 ~ 9999 为测试账号
            String level = jsonObject.get("level").toString();
            map.put("level", level);
            // 学生当前课时 week
            map.put("week", jsonObject.get("week"));
            // 学生当前课时 lesson
            map.put("lesson", jsonObject.get("lesson"));
            return new JsonResult<>(map);
        } catch (Exception e) {
            return new JsonResult<>("-1", "操作错误");
        }
    }

    @GetMapping("/getMainReport")
    public JsonResult<Map<String, Object>> getMainReport(@RequestParam("user_id") String user_id,
                                                         @RequestParam("orgCode") String institute_seq,
                                                         @RequestParam("password") String passwd) {
        System.out.println("169 getMainReport");
        try {
            String sr = sendPost("https://interface.smartreelearners.com:8442/api/app/auth/student/login/china",
                    String.format("{\"user_id\":\"%s\",\"passwd\":\"%s\",\"institute_seq\":\"%s\"}", user_id, passwd, institute_seq), "184");
            HashMap hashMap = JSON.parseObject(sr, HashMap.class);
            if (hashMap.containsKey("error")) {
               return new JsonResult<>("-1", "操作错误");
            }

            String infoString = hashMap.get("student_info").toString();
            HashMap infoMap = JSON.parseObject(infoString, HashMap.class);
            String level = infoMap.get("level").toString();
            String name = infoMap.get("name").toString();
            String week = infoMap.get("course_name").toString();
            if (week.length() > 0) week = week.substring(week.length()-1);
            String user_seq = infoMap.get("user_seq").toString();

            Map<String, Object> map = new HashMap<>();

            // 两周内上课次数
            map.put("weekly_lesson_finish", getFinishLessonWeekly(user_seq));
            // 两周内观影次数
            map.put("weekly_movie_finish", getFinishMovieWeekly(user_seq));
            // 本 level 累计已学单词
            map.put("level_word_learned", getLearnedWord(user_seq));
            // 本 level 累计已学句子
            map.put("level_sentence_learned", getLearnedSentence(user_seq));
            // 本 level 总计单词数
            map.put("level_word_total", getLevelWord(user_seq));
            // 本 level 总计句子数
            map.put("level_sentence_total", getLearnedSentence(user_seq));
            // 本 level 累计已观影数量
            map.put("level_movie_watched", getWatchedMovie(user_seq));
            // 本 level 总计影片数
            map.put("level_movie_total", getLevelMovie(user_seq));
            // 学生当前 level
            map.put("level", level);
            // 学生姓名
            map.put("name", name);
            // 学生 week
            map.put("week", week);
            return new JsonResult<>(map);
        } catch (Exception e) {
            return new JsonResult<>("-1", "操作错误");
        }
    }

    @GetMapping("/getFinishLessonWeekly")
    public Integer getFinishLessonWeekly(@RequestParam("user_seq") String user_seq) {
        /**
         * @description: 学生两周之内上课的数量
         * @return: int
         */
        String url = base_url + "/student/study/get_lesson_times?user_seq=" + user_seq;
        String field = "list";  // 想要读取的字段
        try {
            String fieldString = fetchRemoteAPI(url, "", field);
            return Integer.parseInt(fieldString);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @GetMapping("/getFinishMovieWeekly")
    public Integer getFinishMovieWeekly(@RequestParam("user_seq") String user_seq) {
        /**
         * @description: 学生两周之内观影的数量
         * @return: int
         */
        String url = base_url + "/iptv/user_watch_times_two_weeks?user_seq=" + user_seq;
        String field = "list";  // 想要读取的字段
        try {
            String fieldString = fetchRemoteAPI(url, "", field);
            return Integer.parseInt(fieldString);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @GetMapping("/getLearnedWord")
    public Integer getLearnedWord(@RequestParam("user_seq") String user_seq) {
        /**
         * @description: 学生累计已学的单词数量
         * @return: int
         */
        String url = base_url + "/student/study/get_all_words?user_seq=" + user_seq;
        String field = "list";  // 想要读取的字段
        try {
            String fieldString = fetchRemoteAPI(url, "", field);
            return Integer.parseInt(fieldString);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @GetMapping("/getLevelWord")
    public Integer getLevelWord(@RequestParam("user_seq") String user_seq) {
        /**
         * @description: 学生所在 level 的总单词数量
         * @return: int
         */
        String url = base_url + "/student/study/get_all_level_words?user_seq=" + user_seq;
        String field = "list";  // 想要读取的字段
        try {
            String fieldString = fetchRemoteAPI(url, "", field);
            return Integer.parseInt(fieldString);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @GetMapping("/getLearnedSentence")
    public Integer getLearnedSentence(@RequestParam("user_seq") String user_seq) {
        /**
         * @description: 学生累计已学的句型数量
         * @return: int
         */
        String url = base_url + "/student/study/get_all_sentences?user_seq=" + user_seq;
        String field = "list";  // 想要读取的字段
        try {
            String fieldString = fetchRemoteAPI(url, "", field);
            return Integer.parseInt(fieldString);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @GetMapping("/getLevelSentence")
    public Integer getLevelSentence(@RequestParam("user_seq") String user_seq) {
        /**
         * @description: 学生所在 level 的总句型数量
         * @return: int
         */
        String url = base_url + "/student/study/get_all_level_sentences?user_seq=" + user_seq;
        String field = "list";  // 想要读取的字段
        try {
            String fieldString = fetchRemoteAPI(url, "", field);
            return Integer.parseInt(fieldString);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @GetMapping("/getWatchedMovie")
    public Integer getWatchedMovie(@RequestParam("user_seq") String user_seq) {
        /**
         * @description: 学生累计观影次数
         * @return: int
         */
        String url = base_url + "/iptv/user_watch_times_all?user_seq=" + user_seq;
        String field = "list";  // 想要读取的字段
        try {
            String fieldString = fetchRemoteAPI(url, "", field);
            return Integer.parseInt(fieldString);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @GetMapping("/getLevelMovie")
    public Integer getLevelMovie(@RequestParam("user_seq") String user_seq) {
        /**
         * @description: 学生所在 level 的总影片数量
         * @return: int
         */
        String url = base_url + "/iptv/user_watch_times_level?user_seq=" + user_seq;
        String field = "list";  // 想要读取的字段
        try {
            String fieldString = fetchRemoteAPI(url, "", field);
            return Integer.parseInt(fieldString);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @GetMapping("/getExerciseReport")
    public JsonResult<Map<String, Object>> getExerciseReport(@RequestParam("user_id") String user_id) {
        /**
         * @description: 获取学生刷题模块的各项数据
         * @return: JSON 数据
         */
        /* ---- 以下均表示过去两周的范围 ---- */
        // 刷题次数
        int weeklyTimes = exerciseEvalRepository.getExerciseTimesWeekly(Long.parseLong(user_id));
        // 简单题目数
        int weeklyEasyNum = problemEvalRepository.getExerciseEasyNumWeekly(Long.parseLong(user_id));
        // 中等题目数
        int weeklyMediumNum = problemEvalRepository.getExerciseMediumNumWeekly(Long.parseLong(user_id));
        // 困难题目数
        int weeklyHardNum = problemEvalRepository.getExerciseHardNumWeekly(Long.parseLong(user_id));
        // 刷题数量
        int weeklyTotal = weeklyEasyNum + weeklyMediumNum + weeklyHardNum;
        // 这两周刷题平均分
        int exerciseAvgScoreWeekly = exerciseEvalRepository.getExerciseAvgScoreWeekly(Long.parseLong(user_id));
        // 上一个两周刷题平均分
        int exerciseLastAvgWeekly = exerciseEvalRepository.getExerciseLastAvgWeekly(Long.parseLong(user_id));
        // 所有时间刷题平均分
        int exerciseScoreAvg = exerciseEvalRepository.getExerciseScoreAvg(Long.parseLong(user_id));

        if (exerciseAvgScoreWeekly == 0)
            exerciseAvgScoreWeekly = exerciseScoreAvg;
        if (exerciseLastAvgWeekly == 0)
            exerciseLastAvgWeekly = exerciseScoreAvg;
        int enhance = Math.round(100 * (float) (exerciseAvgScoreWeekly - exerciseLastAvgWeekly) / exerciseAvgScoreWeekly);

        /* ---- 以下均表示所有时间的范围 ---- */
        // 所有时间刷题平均耗时
        int exerciseTimeAvg = exerciseEvalRepository.getExerciseTimeAvg(Long.parseLong(user_id));
        System.out.println("366: " + exerciseTimeAvg);
        int exerciseTimeAvgMinutes = Math.round((float) exerciseTimeAvg / 60);
        System.out.println("366: " + exerciseTimeAvgMinutes);

        // 已掌握考点个数 (正确率> 0.6)
        int handleCnt = problemEvalRepository.getHandlePointCount(Long.parseLong(user_id));
        // 已掌握考点 TOP3
        String[] exerciseRightPoint = problemEvalRepository.getExerciseRightPoint(Long.parseLong(user_id));
        // 未掌握考点 TOP3
        String[] exerciseWrongPoint = problemEvalRepository.getExerciseWrongPoint(Long.parseLong(user_id));
        // 过去 10 次练习得分
        int[] lastTenExercise = exerciseEvalRepository.getLastTenExercise(Long.parseLong(user_id));

        Map<String, Object> map = new HashMap<>();
        try {
            // 两周内刷题次数
            map.put("times", weeklyTimes);
            // 两周内总计练习题数量
            map.put("prob_num", weeklyTotal);
            // 刷题平均时间
            map.put("avg_time", exerciseTimeAvgMinutes);
            // 刷题平均得分
            map.put("avg_score", exerciseScoreAvg);
            // 已掌握考点数量
            map.put("point_handle", handleCnt);
            // 相对于上一阶段进步、退步
            map.put("progress", enhance);
            // 难度占比
            map.put("prob_easy", Math.round(100 * (float) weeklyEasyNum / weeklyTotal));
            map.put("prob_medium", Math.round(100 * (float) weeklyMediumNum / weeklyTotal));
            map.put("prob_hard", Math.round(100 * (float) weeklyHardNum / weeklyTotal));
            // 过去 10 次刷题得分
            map.put("last_ten_score", lastTenExercise);
            // 已掌握考点列表和未掌握考点列表
            map.put("handle_list", exerciseRightPoint);
            map.put("unhandle_list", exerciseWrongPoint);
            return new JsonResult<>(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult<>("-1", "操作错误");
        }
    }

    public void crossCA() throws NoSuchAlgorithmException, KeyManagementException {
        /* --- 以下步骤为了绕开不同域名 smartreelearners.com:9000 的证书验证*/
        //  直接通过主机认证
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        //  配置认证管理器
        javax.net.ssl.TrustManager[] trustAllCerts = {new TrustAllTrustManager()};
        SSLContext sc = SSLContext.getInstance("SSL");
        SSLSessionContext sslsc = sc.getServerSessionContext();
        sslsc.setSessionTimeout(0);
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        //  激活主机认证
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        /* --- End */
    }

    // TODO: 缺少过去 10 次的分数
    @GetMapping("/getVocalReport")
    public JsonResult<Map<String, Object>> getVocalReport(@RequestParam("user_id") String user_id) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        /**
         * @description: 获取学生语音模块的各项数据
         * @return: JSON 数据
         */
        crossCA();
        String urlString = "https://www.smartreelearners.com:9000/api/word_eva?user_id=" + user_id;
        JSONObject jsonObject = JSONObject.parseObject(sendGet(urlString));
        Map<String, Object> map = new HashMap<>();

        if (jsonObject.containsKey("data") && jsonObject.get("data").toString().equals("no data in two weeks")) {
            // 两周内练习次数
            map.put("times", 0);
            // 两周内总计练习用时（分钟）
            map.put("time", 0);
            // 练习平均耗时
            map.put("avg_time", 0);
            // 练习平均得分
            map.put("avg_score", 0);
            // 需要巩固的单词数量
            map.put("word_unhandle", 0);
            // 相对于上一阶段进步、退步
            map.put("progress", 0);
            // 过去 10 次训练得分
            map.put("last_ten_score", new int[]{64, 63, 52, 41, 49, 59, 62, 69, 81, 78});
            map.put("comment_list", new ArrayList<>());
            return new JsonResult<>(map);
        } else try {
            // 两周内练习次数
            map.put("times", jsonObject.get("op_times"));
            // 两周内总计练习用时（分钟）
            map.put("time", jsonObject.get("op_time"));
            // 练习平均耗时
            map.put("avg_time", Math.round(Float.parseFloat(jsonObject.get("op_time").toString()) / Float.parseFloat(jsonObject.get("op_time").toString())));
            // 练习平均得分
            float tmp = Float.parseFloat(jsonObject.get("op_avg_score").toString());
            map.put("avg_score", Math.round(tmp));
            // 需要巩固的单词数量
            map.put("word_unhandle", jsonObject.get("words_sum"));
            // 相对于上一阶段进步、退步
            float percent = Float.parseFloat(jsonObject.get("difference_percent").toString());
            if (jsonObject.get("difference_type").toString().equals("0"))
                percent *= -1;
            map.put("progress", percent);
            // 过去 10 次训练得分
            map.put("last_ten_score", new int[]{64, 63, 52, 41, 49, 59, 62, 69, 81, 78});
            // 已掌握考点列表和未掌握考点列表
            ArrayList<HashMap> comments = new ArrayList<>();
            JSONArray jsonArray = JSONArray.parseArray(jsonObject.get("words").toString());
            ArrayList<String> temp = new ArrayList<>();
            for (Object obj : jsonArray) {
                final String word = JSONObject.parseObject(obj.toString()).get("word").toString();
                final String status = JSONObject.parseObject(obj.toString()).get("status").toString();
                final String audio = JSONObject.parseObject(obj.toString()).get("audio").toString();
                if (temp.size() >= 3) break;  // 选取 3 个不合格单词
                if (temp.contains(word))
                    continue;
                else
                    temp.add(word);
                comments.add(new HashMap<String, String>() {
                    {
                        put("word", word);
                        put("status", status);
                        put("audio", audio);
                    }
                });
            }
            map.put("comment_list", comments);
            return new JsonResult<>(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult<>("-1", "操作错误");
        }
    }

    //TODO: token 验证方式总不对
    @GetMapping("/getLibraryReport")
    public JsonResult<Map<String, Object>> getLibraryReport(@RequestParam("user_id") String user_id) {
        /**
         * @description: 获取学生图书馆模块的各项数据
         * @return: JSON 数据
         */
        Map<String, Object> map = new HashMap<>();
        try {
            long seed = Long.parseLong(user_id) % 7 + 1;
            // 累计已读书本数量
            map.put("lib_book_total", seed * 2);
            // 累计阅读时间
            map.put("lib_hour_total", seed * 8);
            // 阅读书本图片 URL
            ArrayList<String> book_urls = new ArrayList<>();
            book_urls.add("https://i0.hdslb.com/bfs/album/1e3e118c2b8b0473bedbf34808928e63b066b634.jpg");
            book_urls.add("https://i0.hdslb.com/bfs/album/1ae25afb2e06e106a72dacd1d0910deb8b1e962d.png");
            book_urls.add("https://i0.hdslb.com/bfs/album/4468f61b04bb98ac13a1b210ce5e922e8c630d2b.png");
            map.put("book_urls", book_urls);
            // 当前所处阅读等级
            map.put("read_lev", seed);
            // 当前阅读量（这里的阅读量和之前的阅读书本数量以及阅读时长没有关系，是一个单位为 L 的数字）
            map.put("read_num", seed * 100);

            return new JsonResult<>(map);
        } catch (Exception e) {
            return new JsonResult<>("-1", "操作错误");
        }
    }

    public static String getToken(Long timestamp) {

        String appId = "App095b01217b56216e6";
        String secret = "694557be44408818ee569af8b38b17bc";
        String ip = "114.92.195.58";
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        StringBuffer sf = new StringBuffer();
        sf.append("[");
        sf.append(appId).append(",");
        sf.append(secret).append(",");
        sf.append(ip).append(",");
        sf.append(timestamp);
        sf.append("]");

        String tokenStr = sf.toString();

        String token = Util.getMd5(tokenStr).toLowerCase();

        System.out.println("tokenStr:" + tokenStr);
        System.out.println("token:" + token);

        return token;
    }

    @GetMapping("/getLibrary")
    public void testReadingBooks() throws Exception {
        // 测试接口使用
        Long timestamp = System.currentTimeMillis() / 1000;
        String url = "https://www.kdsreaders.com/api/o/reading/books";

        Map<String, Object> params = new HashMap<>();
        params.put("appId", "App095b01217b56216e6");
        params.put("useq", 1469);
        params.put("timestamp", timestamp);
        params.put("token", getToken(timestamp));

        String url_p = "";
        for (String key : params.keySet()) {
            url_p += key + "=" + params.get(key).toString() + "&";
        }
        url_p = url + "?" + url_p.substring(0, url_p.length() - 1);

        String responseJson = sendPost(url_p, "", "611");

        System.out.println(responseJson);
    }

    @GetMapping("/getContrastReport")
    public JsonResult<Map<String, Object>> getContrastReport(@RequestParam("user_id") String user_id) {
        /**
         * @description: 获取横向对比的各项数据，单词、句子、对话、语法、演讲/写作五个模块
         * @return: JSON 数据
         */
        Map<String, Object> map = new HashMap<>();
        try {
            crossCA();
            String urlString = "https://www.smartreelearners.com:9000/api/word_eva?user_id=" + user_id;
            JSONObject jsonObject = JSONObject.parseObject(sendGet(urlString));
            if (jsonObject.containsKey("data") && jsonObject.get("data").toString().equals("no data in two weeks"))
                user_vocal_avg = 0;
            else
                user_vocal_avg = Math.round(Float.parseFloat(jsonObject.get("op_avg_score").toString()));
            all_exercise_avg = exerciseEvalRepository.getExerciseAllAvg();
            user_exercise_avg = exerciseEvalRepository.getExerciseScoreAvg(Long.parseLong(user_id));

            // 该学生的各项平均成绩
            map.put("my_avg_score", new int[]{user_vocal_avg, 60, 40, user_exercise_avg, 60});
            // 所有学生的各项平均成绩
            map.put("all_avg_score", new int[]{30, 40, 50, all_exercise_avg, 40});

            return new JsonResult<>(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult<>("-1", "操作错误");
        }
    }

    // TODO: 2021.12.25 期中期末接口显示 404 Not Found 待解决
    @GetMapping("/getTermMid")
    public JsonResult<Map<String, Object>> getTermMid(@RequestParam("user_id") String user_id) {
        /**
         * @description: 获取期中成绩
         * @return: JSON 数据
         */
        // TODO: 2021.12.25 期中期末接口显示 404 Not Found 待解决
//        String url = "https://interface.smartreelearners.com:8442/api/user/report/adt";
//        String field = "list";  // 想要读取的字段
//        String input = "{\n" +
//                "  \"course_seq\":\"539\",\n" +
//                "  \"institute_seq\":\"31\",\n" +
//                "  \"class_seq\":\"0\",\n" +
//                "  \"user_seq\":\"285\",\n" +
//                "  \"test_type\":\"DT\"\n" +
//                "}";
//        JSONObject jsonObject;
//        try {
//            String fieldString = fetchRemoteAPI(url, input, field);
//            jsonObject = JSONObject.parseObject(fieldString);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new JsonResult<>("-1", "操作错误");
//        }
//
//        Map<String, Object> map = new HashMap<>();
//        try {
//            // 该学生的总分=各项成绩之和
//            map.put("total", jsonObject.get("total"));
//            map.put("phonics", jsonObject.get("phonics"));
//            map.put("speaking", jsonObject.get("speaking"));
//            map.put("reading", jsonObject.get("reading"));
//            map.put("writing", jsonObject.get("writing"));
//            map.put("grammar", jsonObject.get("grammar"));
//            return new JsonResult<>(map);
//        } catch (Exception e) {
//            return new JsonResult<>("-1", "操作错误");
//        }

        Map<String, Object> map = new HashMap<>();
        try {
            // 该学生的总分=各项成绩之和
            map.put("total", 96);
            map.put("phonics", 96);
            map.put("speaking", 0);
            map.put("reading", 0);
            map.put("writing", 0);
            map.put("grammar", 0);
            return new JsonResult<>(map);
        } catch (Exception e) {
            return new JsonResult<>("-1", "操作错误");
        }
    }

    @GetMapping("/getTermEnd")
    public JsonResult<Map<String, Object>> getTermEnd(@RequestParam("user_id") String user_id) {
        /**
         * @description: 获取期末成绩
         * @return: JSON 数据
         */

//        String url = "https://interface.smartreelearners.com:8442/api/user/report/adt";
//        String field = "list";  // 想要读取的字段
//        String input = "{\n" +
//                "  \"course_seq\":\"539\",\n" +
//                "  \"institute_seq\":\"31\",\n" +
//                "  \"class_seq\":\"0\",\n" +
//                "  \"user_seq\":\"285\",\n" +
//                "  \"test_type\":\"AT\"\n" +
//                "}";
//        JSONObject jsonObject;
//        try {
//            String fieldString = fetchRemoteAPI(url, input, field);
//            jsonObject = JSONObject.parseObject(fieldString);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new JsonResult<>("-1", "操作错误");
//        }
//
//        Map<String, Object> map = new HashMap<>();
//        try {
//            // 该学生的总分=各项成绩之和
//            map.put("total", jsonObject.get("total"));
//            map.put("phonics", jsonObject.get("phonics"));
//            map.put("speaking", jsonObject.get("speaking"));
//            map.put("reading", jsonObject.get("reading"));
//            map.put("writing", jsonObject.get("writing"));
//            map.put("grammar", jsonObject.get("grammar"));
//            return new JsonResult<>(map);
//        } catch (Exception e) {
//            return new JsonResult<>("-1", "操作错误");
//        }
        Map<String, Object> map = new HashMap<>();
        try {
            // 该学生的总分=各项成绩之和
            map.put("total", 96);
            map.put("phonics", 96);
            map.put("speaking", 0);
            map.put("reading", 0);
            map.put("writing", 0);
            map.put("grammar", 0);
            return new JsonResult<>(map);
        } catch (Exception e) {
            return new JsonResult<>("-1", "操作错误");
        }
    }

    // TODO: 轨迹报告基本都是空的
    @GetMapping("/getPortfolio")
    public JsonResult<Map<String, Object>> getPortfolio(@RequestParam("user_id") String user_id) throws IOException {
        /**
         * @description: 获取轨迹报告
         * @return: JSON 数据
         */
//        String urlString = "https://interface.smartreelearners.com:8442/api/admin/report/portfolio?userid=" + user_id.toString();
//        if (!loginReport()) return new JsonResult<>("-1", "API 登录错误");
//        String responseJsonString = sendGet(urlString);
//
//        JSONArray jsonArray = JSONObject.parseArray(JSONObject.parseObject(responseJsonString).get("portfolioList").toString());
//        List<Map<String, Object>> list =  new ArrayList<>();
//
//        for (Object obj : jsonArray) {
//            JSONObject item = JSONObject.parseObject(obj.toString());
//            Map<String, Object> mapItem = new HashMap<>();
//            mapItem.put("completeDate", item.get("completeDate"));
//            mapItem.put("userName", item.get("userName"));
//            mapItem.put("courseName", item.get("courseName"));
//            mapItem.put("fileType", item.get("fileType"));
//            mapItem.put("fileData", item.get("fileData").toString());
//            list.add(mapItem);
//        }
//        Map<String, Object> map = new HashMap<>();
//
//        try {
//            // 该学生的总分=各项成绩之和
//            map.put("portfolioList", list);
//            return new JsonResult<>(map);
//        } catch (Exception e) {
//            return new JsonResult<>("-1", "操作错误");
//        }

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mapItem = new HashMap<>();
        mapItem.put("completeDate", "2021-10-08 19:42:34.0");
        mapItem.put("userName", "Rex");
        mapItem.put("courseName", "Week3");
        mapItem.put("fileType", "image");
        mapItem.put("fileData", "/datafiles/portfolio/211008194234tste8t8s13q9612t4m4m2ootr461602eaa421d51.png");
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(mapItem);
        list.add(mapItem);
        try {
            // 该学生的总分=各项成绩之和
            map.put("portfolioList", list);
            return new JsonResult<>(map);
        } catch (Exception e) {
            return new JsonResult<>("-1", "操作错误");
        }
    }

    @GetMapping("/getSmtId")
    public String getSmtId() {
        return "";
    }

    @PostMapping("/bind")
    public String bind(@RequestParam("orgCode") String institute_seq,
                       @RequestParam("smtId") String user_id,
                       @RequestParam("password") String passwd,
                       HttpServletResponse response) {

        try {
            // 仅供内部测试使用，绕过登录验证
            if (institute_seq.equals("1111") && user_id.equals("1111") && passwd.equals("1111")) {
                return "OK";
            }
            String sr = sendPost("https://interface.smartreelearners.com:8442/api/app/auth/student/login/china",
                    String.format("{\"user_id\":\"%s\",\"passwd\":\"%s\",\"institute_seq\":\"%s\"}", user_id, passwd, institute_seq), "184");
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

                return "OK " + infoMap.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Fail";
        }

        return "Fail";

    }

}

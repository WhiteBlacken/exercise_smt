package com.cisl.smt.web;

import com.alibaba.fastjson.JSONObject;
import com.cisl.smt.web.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class WxController {
    /**
     * @description: 微信授权登录相关功能
     * @author: Hopenx
     * @date: 2021/8/1 2:02 下午
     */

    private final String appid = "wxc045a66643c66355";
    private final String appsecret = "a594408dcaa28a661b56acdc6c7b1d3c";
    private final String http = "https://interface.smartreelearners.com:8442/wxcallback";

    @GetMapping("/index")
    public String index() {
        return "/index";
    }

    @GetMapping("/wxlogin")
    public String wxlogin() {
        // 第一步：用户同意授权，获取code
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid +
                "&redirect_uri=" + http +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=STATE#wechat_redirect";
        return "redirect:"  + url;
    }

    @GetMapping("/wxcallback")
    public String wxcallback(String code, ModelMap map) throws IOException {
        // 第二步：通过code换取网页授权access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid +
                "&secret=" + appsecret +
                "&code=" + code +
                "&grant_type=authorization_code";
        JSONObject jsonObject = HttpClientUtils.doGet(url);

        String openid = jsonObject.getString("openid");
        String access_Token = jsonObject.getString("access_token");

        System.out.println(jsonObject);

        // 第四步：拉取用户信息(需scope为 snsapi_userinfo)
        url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_Token +
                "&openid=" + openid +
                "&lang=zh_CN";
        JSONObject userInfoJson = HttpClientUtils.doGet(url);
        System.out.println("UserInfo:" + userInfoJson);

        // 1种情况, 是基于微信授权的账号做为我们本系统的账号体系来使用
//         map.put("userinfo", userInfoJson);
//         return "/home";
        return userInfoJson.toJSONString();
    }

}

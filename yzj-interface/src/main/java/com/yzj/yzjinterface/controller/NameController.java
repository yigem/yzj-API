package com.yzj.yzjinterface.controller;

import com.yzj.yzjclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 服务端
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public String getNameByGet(String name){
        return "GET 你的名字是" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name){
        return "POST 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest request){
        //校验,从请求头获取accessKey，secretKey
//        String accessKey = request.getHeader("accessKey");
//        String nonce = request.getHeader("nonce");
//        String timestamp = request.getHeader("timestamp");
//        String sign = request.getHeader("sign");
//        String body = request.getHeader("body");
//        //todo 实际情况应该是去数据库中查是否已分配给用户
//        if(!accessKey.equals("yzj")){
//            throw new RuntimeException("无权限调用");
//        }
//        if(Long.parseLong(nonce) > 100000){
//            throw new RuntimeException("无权限调用");
//        }
//        // todo 时间和当前时间不能超过5分钟
////        if(timestamp){
////
////        }
//        //todo 实际情况中是从数据库中查出 secretKye
//        String serverSign = SignUtils.getSign(body, "abcdefgh");
//        if(!sign.equals(serverSign)){
//            throw new RuntimeException("无权限");
//        }
        return "Post 用户名字是" + user.getUsername();
    }
}

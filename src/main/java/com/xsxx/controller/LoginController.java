package com.xsxx.controller;

import com.xsxx.pojo.User;
import com.xsxx.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class LoginController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(("alarmfile"));


    @Autowired
    UserService userService;

    @RequestMapping("login")
    public String login(){
        return "userlogin";
    }

    @RequestMapping("loginUser")
    public String loginUser(String username, String password, HttpSession session,HttpServletRequest request) {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(usernamePasswordToken);   //完成登录
            User user = (User) subject.getPrincipal();
            session.setAttribute("user", user);
            return "redirect:/";
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "userlogin";
    }

    @RequestMapping("logOut")
    public String logOut(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "userlogin";
    }

    @RequestMapping("")
    public String index(){
        return "index";
    }

    @RequestMapping("home")
    public String home(){
        return "home";
    }

}

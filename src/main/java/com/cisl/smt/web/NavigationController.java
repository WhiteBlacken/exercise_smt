package com.cisl.smt.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class NavigationController {

    @GetMapping("/welcome")
    public ModelAndView welcome(){
        ModelAndView modelAndView = new ModelAndView("welcome");
        return modelAndView;
    }

    @GetMapping("/home")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView("homepage");
        return modelAndView;
    }

    @GetMapping("/week_test")
    public ModelAndView week_test(){
        ModelAndView modelAndView = new ModelAndView("week_test");
        return modelAndView;
    }

    @GetMapping("/wrong")
    public ModelAndView wrong(){
        ModelAndView modelAndView = new ModelAndView("wrong");
        return modelAndView;
    }

    @GetMapping("/history")
    public ModelAndView history(){
        ModelAndView modelAndView = new ModelAndView("history");
        return modelAndView;
    }
}

package com.example.studio_mksg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OrderCompleteController {

    @GetMapping("/orderComplete")
    public ModelAndView showOrderComplete(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/orderComplete");
        return mav;
    }
}

package com.example.studio_mksg.controller;

import com.example.studio_mksg.controller.form.OrderForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OrderInputController {
    //ユーザ登録画面表示
    @GetMapping("/orderInput")
    public ModelAndView showOrderInputForm(){
        ModelAndView mav = new ModelAndView();
        OrderForm orderForm = new OrderForm();
        mav.setViewName("/orderInput");
        mav.addObject("orderForm",orderForm);
        return mav;
    }
}

package com.eathub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mydining")
public class MyDiningController {
    @ModelAttribute("page")
    public String page() {
        return "mydining";
    }
    @GetMapping("")
    public String myDining(Model model) {
        return "/members/myDining";
    }
}

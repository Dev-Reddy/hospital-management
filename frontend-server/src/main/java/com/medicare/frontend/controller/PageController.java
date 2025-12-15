package com.medicare.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageRoute", "home");
        return "index";
    }

    @GetMapping("/team/{member}")
    public String team(@PathVariable String member, Model model) {
        model.addAttribute("pageRoute", member);
        return "team/" + member;
    }
}

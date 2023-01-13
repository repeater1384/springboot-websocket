package com.example.demo.test;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @RequestMapping(value = "/")
    public String main() {
        System.out.println("hihi");
        return "index.html";
    }

    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public String chat(Locale locale, Model model) {
        return "chat.html";
    }

    @RequestMapping(value = "/seller", method = RequestMethod.GET)
    public String seller() {
        return "seller.html";
    }

}
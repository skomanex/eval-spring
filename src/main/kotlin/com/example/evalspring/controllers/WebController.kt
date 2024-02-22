package com.example.evalspring.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WebController {

    //http://localhost:8080/home
    //http://141.94.237.180:8080/home
    @GetMapping("/home")
    fun home(): String{
        return "home"
    }
}
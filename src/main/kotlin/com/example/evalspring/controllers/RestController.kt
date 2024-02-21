package com.example.evalspring.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RestController {
    //http://localhost:8080/test pour test local
    //http://141.94.237.180:8080/test pour test en ligne
    @GetMapping("/test")
    fun testMethode(): String {
        println("/test")

        return "helloWorld"
    }
}
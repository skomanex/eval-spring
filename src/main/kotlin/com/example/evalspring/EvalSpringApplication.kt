package com.example.evalspring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EvalSpringApplication

fun main(args: Array<String>) {
    runApplication<EvalSpringApplication>(*args)
}

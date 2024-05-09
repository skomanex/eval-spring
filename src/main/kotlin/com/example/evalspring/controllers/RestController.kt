package com.example.evalspring.controllers

import com.example.evalspring.model.MatchService
import com.example.evalspring.model.Matches
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class PasswordRequest(val password: String)

@RestController
class RestController {
    //http://localhost:8080/test pour test local
    //http://141.94.237.180:8080/test pour test en ligne
    @GetMapping("/test")
    fun testMethode(): String {
        println("/test")

        return "Page test pour evaluation groupe VOLLAND/PIUZZI/GOMEZ"
    }
}

@RestController

class MatchRestController {
    @Autowired
    private lateinit var matchService: MatchService

    //http://localhost:8080/matchesJson
    @GetMapping("/matchesJson")
    fun getMatchesFromLastSevenDays(): List<Matches> {
        return matchService.getAll7Days()
    }

    @PostMapping("/matchesJson")

    fun getMatchesFromLastSevenDays(@RequestBody passwordRequest: PasswordRequest): ResponseEntity<List<Matches>> {
        return if (passwordRequest.password == "Password") {

            ResponseEntity.ok(matchService.getAll7Days())
        } else {
            ResponseEntity.badRequest().build()
        }
    }
}

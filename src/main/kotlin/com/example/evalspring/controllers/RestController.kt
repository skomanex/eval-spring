package com.example.evalspring.controllers

import com.example.evalspring.model.MatchRepository
import com.example.evalspring.model.Matches
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
class MatchRestController(private val matchRepository: MatchRepository) {

    //http://localhost:8080/matchesJson
    @GetMapping("/matchesJson")
    fun getAllMatches(): List<Matches> {
        return matchRepository.findAllOrderByMatchDateDesc()
    }
    @PostMapping("/matchesJson")
    fun getAllMatches(@RequestBody passwordRequest: PasswordRequest): ResponseEntity<List<Matches>> {
        return if (passwordRequest.password == "Password") {
            ResponseEntity.ok(matchRepository.findAllOrderByMatchDateDesc())
        } else {
            ResponseEntity.badRequest().build()
        }
    }
}

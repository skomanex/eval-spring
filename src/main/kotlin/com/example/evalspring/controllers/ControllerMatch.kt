package com.example.evalspring.controllers

import com.example.evalspring.model.MatchRepository
import com.example.evalspring.model.MatchService
import com.example.evalspring.model.Matches
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping


class ControllerMatch {

    @Controller
    class MatchController(
        val matchService: MatchService,
        private val matchRepository: MatchRepository
    ) {


//http://localhost:8080/matches

        @GetMapping("/matches")
        fun getAllMatches(model: Model): String {
            val matches = matchService.getAll()
            model.addAttribute("matches", matches)
            println("matches")
            for (matchData in matches) println(matchData)
            return "/matches"
        }

        //http://localhost:8080/newMatch
        @PostMapping("/newMatch")
        fun save(@ModelAttribute match: Matches): String {
            matchService.save(match)
            return "redirect:/home"
        }
    }
}
package com.example.evalspring.controllers

import com.example.evalspring.model.MatchRepository
import com.example.evalspring.model.MatchService
import com.example.evalspring.model.Matches
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDate


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

    @GetMapping("/newMatch")
    fun form(model: Model): String {
        model.addAttribute("matches", Matches())
        return "newMatch"
    }

    @PostMapping("/newMatch")
    fun newMatch(@ModelAttribute("matches") match: Matches, redirectAttributes: RedirectAttributes): String {
        matchService.save(match)
        redirectAttributes.addFlashAttribute("message", "Match ajouté avec succès")
        return "redirect:/matches"
    }

    @GetMapping("/matches/{id}/edit")
    fun editMatch(@PathVariable("id") id: Long, model: Model): String {
        val match = matchService.matchRepository
        model.addAttribute("match", match)
        return "edit-match"
    }

    @PostMapping("/matches/{id}/edit")
    fun updateMatch(
        @PathVariable("id") id: Long,
        @RequestParam("score1") score1: Int,
        @RequestParam("score2") score2: Int,
        @RequestParam("date1") date1: LocalDate,
        @RequestParam("image") image1: String,
        model: Model
    ): String {
        val updatedMatch = matchService.updateMatch(id, score1, score2, date1, image1)
        if (updatedMatch != null) {
            model.addAttribute("message", "Match updated successfully")
        } else {
            model.addAttribute("message", "Match not found")
        }
        return "edit-match"
    }

    @GetMapping("/false")
    fun getMatchesWithTermineFalse(model: Model): String {
        val matchesfalse = matchService.getAll()
        model.addAttribute("matches", matchesfalse)
        println("matches")
        return "matches-false"
    }
}

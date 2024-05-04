package com.example.evalspring.controllers

import com.example.evalspring.model.MatchRepository
import com.example.evalspring.model.MatchService
import com.example.evalspring.model.Matches
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class MatchController(
    val matchService: MatchService,
    private val matchRepository: MatchRepository
) {


    //http://localhost:8080/matches

    @GetMapping("/matches")
    fun getAllMatches(model: Model): String {
        val matches = matchService.getAll7Days()
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

    @GetMapping("/editMatch/{id}")
    fun editMatchForm(@PathVariable("id") id: Long, model: Model): String {
        val match = matchService.matchRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Match introuvable avec l'ID $id") }
        model.addAttribute("matches", match)
        return "/editMatch"
    }

    @PostMapping("/editMatch/{id}")
    fun editMatch(
        @PathVariable("id") id: Long,
        @RequestParam("score1") score1: Int,
        @RequestParam("score2") score2: Int,
        @ModelAttribute("matches") updatedMatch: Matches,
        redirectAttributes: RedirectAttributes
    ): String {
        if (matchService.updateMatch(id, score1, score2) != null) {
            redirectAttributes.addFlashAttribute("message", "Match mis à jour avec succès")
            return "redirect:/matches"
        } else {
            redirectAttributes.addFlashAttribute("message", "Match non trouvé")
            return "redirect:/matches"
        }
    }
    @GetMapping("/termineMatch/{id}")
    fun termineMatch(@PathVariable("id") id: Long, model: Model): String {
        val match = matchService.matchRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Match introuvable avec l'ID $id") }
        model.addAttribute("matches", match)
        return "/termineMatch"
    }
    @PostMapping("/termineMatch/{id}")
    fun termineMatch(
        @PathVariable("id") id: Long,
        @RequestParam("termine") termine: Boolean,
        redirectAttributes: RedirectAttributes
    ): String {
        if (matchService.finishMatch(id, termine) != null) {
            redirectAttributes.addFlashAttribute("message", "Match terminé avec succès")
            return "redirect:/matches"
        } else {
            redirectAttributes.addFlashAttribute("message", "Match non trouvé")
            return "redirect:/matches"
        }
    }

}


@Controller
class MatchWebSocketController(val matchService: MatchService) {

    @MessageMapping("/matchUpdate")
    @SendTo("/topic/matches")
    fun updateMatch(@Payload match: Matches, headers: SimpMessageHeaderAccessor): Matches {
        return match
    }

    @MessageMapping("/newMatch")
    @SendTo("/topic/matches")
    fun newMatch(@Payload matches: Matches, headers: SimpMessageHeaderAccessor): Matches {
        // Save the new match in the service
        return matchService.save(matches)
    }
}

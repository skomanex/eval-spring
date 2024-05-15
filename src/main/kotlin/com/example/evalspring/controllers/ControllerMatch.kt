package com.example.evalspring.controllers

import com.example.evalspring.model.MatchRepository
import com.example.evalspring.model.MatchService
import com.example.evalspring.model.Matches
import com.fasterxml.jackson.databind.deser.DataFormatReaders
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.messaging.SessionSubscribeEvent

@Controller
class MatchController(
    val matchService: MatchService,
    private val matchRepository: MatchRepository
) {


    //http://localhost:8080/matches

    @GetMapping("/matches")
    fun getAllMatches(@RequestParam(required = false) status: String?, model: Model): String {
        val matches = when (status) {
            "en-cours" -> {
                matchService.getOngoingMatches()
            }

            "termine" -> {
                matchService.getFinishedMatches()
            }

            else -> {
                matchService.getAll()
            }
        }
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
        @RequestParam("image") image1: String,
        @ModelAttribute("matches") updatedMatch: Matches,
        redirectAttributes: RedirectAttributes
    ): String {
        if (matchService.updateMatch(id, score1, score2, image1) != null) {
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
        if (!termine) {
            redirectAttributes.addFlashAttribute("message", "Un match non terminé ne peut pas être réinitialisé")
            return "redirect:/matches"
        }

        if (matchService.finishMatch(id, termine) != null) {
            redirectAttributes.addFlashAttribute("message", "Match terminé avec succès")
            return "redirect:/matches"
        } else {
            redirectAttributes.addFlashAttribute("message", "Match non trouvé")
            return "redirect:/matches"
        }
    }

    @Controller
    @RequestMapping("/ws") // Chemin de base pour toutes les méthodes de ce controleur
    class WebSocketController(private val messagingTemplate: SimpMessagingTemplate) {
        private val listMatches = ArrayList<Matches>()

        @MessageMapping("/addMatch")
        fun addMatch(match: Matches) {
            println("/topic/addMatches $match")
            listMatches.add(match)
            messagingTemplate.convertAndSend("/topic/addMatches")
        }
        @EventListener
            fun ecouteWebSocket(event: SessionSubscribeEvent){
            val headerAccessor = StompHeaderAccessor.wrap(event.message)
            if("/topic" == headerAccessor.destination){
                messagingTemplate.convertAndSend("/topic/addMatches")
            }
        }
    }
}
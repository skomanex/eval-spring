package ipi.matchsport.controller

import ipi.matchsport.model.MatchData
import ipi.matchsport.model.MatchRepository
import ipi.matchsport.model.MatchService
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class MatchController(val matchService: MatchService) {


//http://localhost:8080/matches

    @GetMapping("/matches")
    fun getAllMatches(model: Model): String {
        val matches = matchService.getAll()
        model.addAttribute("matches", matches)
        println("matches")
        for (matchData in matches) {
            println(matchData)
        }
        return "/matches"
    }

    //http://localhost:8080/newMatch
    @PostMapping("/newMatch")
    fun save(@ModelAttribute match: MatchData): String {
        matchService.save(match)
        return "/index"
    }
}





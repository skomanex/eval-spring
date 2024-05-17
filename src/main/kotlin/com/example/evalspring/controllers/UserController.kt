package com.example.evalspring.controllers

import com.example.evalspring.model.UserBean
import com.example.evalspring.model.UserService
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView

@Controller
@RequestMapping("/user")
class UserController(val userService: UserService) {

    @GetMapping("/")
    fun home(): RedirectView {
        return RedirectView("/user/accueil")
    }

    //Affichage du formulaire
    //http://localhost:8080/user/login

    @GetMapping("/accueil")
    fun showLoginForm(userbean: UserBean, session: HttpSession): String {
        println("/accueil ${session.id}")

        val user = userService.findBySessionId(session.id)
        if (user != null) {
            //déja connecté je redirige
            return "redirect:/newMatch"
        }

        //Spring créera une instance de userBean qu'il mettra dans le model
        return "accueil"
    }

    //Traitement du formulaire
    @PostMapping("/loginSubmit")
    fun loginSubmit(
        userBean: UserBean,
        redirect: RedirectAttributes,
        session: HttpSession
    ): String {
        println("/loginSubmit :  : $userBean")

        try {
            userService.insertOrCheck(userBean, session.id)

            //Cas qui marche
            return "redirect:/newMatch"
        } catch (e: Exception) {
            //Affiche le détail de l'erreur dans la console serveur
            e.printStackTrace()

            //pour garder les données entrées dans le formulaire par l'utilisateur
            redirect.addFlashAttribute("user", userBean)
            //Pour transmettre le message d'erreur
            redirect.addFlashAttribute("errorMessage", "Erreur : ${e.message}")
            //Cas d'erreur
            return "redirect:/user/login" //Redirige sur /form
        }
    }

    @GetMapping("/newMatch") //Affiche la page résultat
    fun newMatch(model: Model, session: HttpSession, redirect: RedirectAttributes): String {

        try {
            val user = userService.findBySessionId(session.id)
            if (user == null) {
                throw Exception("Veuillez vous reconnecter")
            }

            model.addAttribute("userBean", user)
            model.addAttribute("userList", userService.load())
            return "newMatch" //Lance newMatch.html

        } catch (e: Exception) {
            e.printStackTrace()
            //Pour transmettre le message d'erreur
            redirect.addFlashAttribute("errorMessage", "Erreur : ${e.message}")
            //Cas d'erreur
            return "redirect:/user/login" //Redirige sur /form
        }
    }
}

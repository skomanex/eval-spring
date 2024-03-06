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

@Controller
@RequestMapping("/user")
class ControllerLogin(val userService: UserService) {

    //Affichage du formulaire
    //http://localhost:8080/user/login
    @GetMapping("/login")
    fun form(userBean: UserBean, session: HttpSession): String {
        println("/login ${session.id}")

        if (userService.findBySessionId(session.id) != null) {
            //déja connecté je redirige
            return "redirect:/user/userregister"
        }

        //Spring créera une instance de StudentBean qu'il mettra dans le model
        return "login"
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
            return "redirect:/user/userregister" // Redirection sur /formResult
        } catch (e: Exception) {
            //Affiche le détail de l'erreur dans la console serveur
            e.printStackTrace()

            //pour garder les données entrées dans le formulaire par l'utilisateur
            redirect.addFlashAttribute("userBean", userBean)
            //Pour transmettre le message d'erreur
            redirect.addFlashAttribute("errorMessage", "Erreur : ${e.message}")
            //Cas d'erreur
            return "redirect:/user/login" //Redirige sur /form
        }
    }


    //affichage du chat
    @GetMapping("/userregister") //Affiche la page résultat
    fun userRegister(model: Model, session: HttpSession, redirect: RedirectAttributes): String {

        try {

            val user = userService.findBySessionId(session.id)
            if (user == null) {
                throw Exception("Veuillez vous reconnecter")
            }

            model.addAttribute("userBean", user)
            model.addAttribute("userList", userService.load())
            return "userregister" //Lance studentForm.html
        } catch (e: Exception) {
            e.printStackTrace()
            //Pour transmettre le message d'erreur
            redirect.addFlashAttribute("errorMessage", "Erreur : ${e.message}")
            //Cas d'erreur
            return "redirect:/user/login" //Redirige sur /form
        }
    }

    @GetMapping("/logout") //Affiche la page résultat
    fun logout(session: HttpSession): String {

        //Spring regénerera un nouveau sessionId au prochain appel
        session.invalidate()

        return "redirect:/user/login"
    }

}
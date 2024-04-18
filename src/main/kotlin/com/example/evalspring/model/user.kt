package com.example.evalspring.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import org.springframework.data.annotation.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Entity
@Table(name = "user")
data class UserBean(
    @jakarta.persistence.Id @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var login: String = "",
    var password: String = "",
    var sessionId: String = ""
)

@Repository                       //<Bean, Typage Id>
interface UserRepository : JpaRepository<UserBean, Long> {

    //En fonction du nom JPA crééra la méthode.
    fun findByLogin(login: String): UserBean?
    fun findBySessionId(sessionId: String): UserBean?
}

@Service
class UserService(val userRepository: UserRepository) {
    //Jeu de donnée
    private val list = ArrayList<UserBean>()

    //    init {
//        list.add(UserBean(login="aaa", password =  "aaa"))
//        list.add(UserBean(login="bbb", password = "bbb"))
//    }
    //Sauvegarde
    fun save(user: UserBean) {
        //On regarde s'il n'est pas déjà en base
        val userRegister = findByLogin(user.login)
        if (userRegister != null) {
            list.remove(userRegister)
        }
        //userRepository.save(user)
        list.add(user)
    }


    //Insert si le login n'existe pas sinon controle le mdp
    fun insertOrCheck(userBean: UserBean, sessionId: String) {

        if (userBean.login.isBlank()) {
            throw Exception("Nom manquant")
        } else if (userBean.password.isBlank()) {
            throw Exception("Password manquant")
        }

        val userBdd: UserBean? = findByLogin(userBean.login)

        if (userBdd != null && userBdd.password != userBean.password) {
            //password !ok
            throw Exception("Mot de passe incorrect")
        }

        if (userBdd != null) {

            //userBdd a son id ce qu'userBean n'a pas car il vient du post
            userBdd.sessionId = sessionId
            userRepository.save(userBdd)
        } else {
            userBean.sessionId = sessionId
            userRepository.save(userBean)
        }
    }

    //Retourne la liste
    fun load() = list

    //Retourne l'utilisateur qui a ce login ou null
    fun findByLogin(login: String) = userRepository.findByLogin(login)


    //Retourne l'utilisateur qui a cette session ou null
    fun findBySessionId(sessionId: String?): UserBean? =
        if (sessionId != null) userRepository.findBySessionId(sessionId) else null


    fun logout(sessionId: String) {
        userRepository.findBySessionId(sessionId)?.let {
            it.sessionId = ""
            userRepository.save(it)
        }
    }
}

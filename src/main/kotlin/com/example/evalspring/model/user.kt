package com.example.evalspring.model

import jakarta.persistence.*
import org.springframework.data.annotation.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Entity
@Table(name = "user")
data class UserBean(
    @jakarta.persistence.Id @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name")
    var login: String = "",
    var password: String = "",
    @Column(name = "id_session")
    var sessionId: String = ""
) {
    //constructor(login : String, password : String) :  this(null, login, password, "")

}

@Repository                       //<Bean, Typage Id>
interface UserRepository : JpaRepository<UserBean, Long> {

    //En fonction du nom JPA crééra la méthode.
    //Nom de méthode normé
    fun findByLogin(login: String): UserBean?
    fun findBySessionId(sessionId: String): UserBean?
}

@Service
class UserService(val userRepository: UserRepository) {

    //Jeu de donnée
//    init {
//        list.add(UserBean(login="aaa", password =  "aaa"))
//        list.add(UserBean(login="bbb", password = "bbb"))
//    }
    //Sauvegarde
    fun save(user: UserBean) {
        //On regarde s'il n'est pas déjà en base
        //val userRegister = findByLogin(user.login)
        userRepository.save(user)
    }




    //Insert si le login n'existe pas sinon controle le mdp
    fun insertOrCheck(userBean: UserBean, sessionId: String) {

        if (userBean.login.isBlank()) {
            throw Exception("Nom manquant")
        }
        else if (userBean.password.isBlank()) {
            throw Exception("Password manquant")
        }

        val userBdd: UserBean? = findByLogin(userBean.login)

        if(userBdd != null && userBdd.password != userBean.password) {
            //password !ok
            throw Exception("Mot de passe incorrect")
        }

        if(userBdd != null){

            //userBdd a son id ce qu'userBean n'a pas car il vient du post
            userBdd.sessionId = sessionId
            userRepository.save(userBdd)
        }
        else {
            userBean.sessionId = sessionId
            userRepository.save(userBean)
        }
    }

    //Retourne la liste
    fun load() = userRepository.findAll()

    //Retourne l'utilisateur qui a ce login ou null
    fun findByLogin(login: String) = userRepository.findByLogin(login)


    //Retourne l'utilisateur qui a cette session ou null
    fun findBySessionId(sessionId: String?): UserBean? =
        if (sessionId != null) userRepository.findBySessionId(sessionId) else null


    fun logOut(sessionId: String)  {
        userRepository.findBySessionId(sessionId)?.let {
            it.sessionId = ""
            userRepository.save(it)
        }
    }

}

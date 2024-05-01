package com.example.evalspring.model

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.time.LocalDate


@Entity
@Table(name = "Matches")
data class Matches(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "team1")
    val team1: String = "",
    @Column(name = "team2")
    val team2: String = "",
    @Column(name = "score1")
    var score1: Int = 0,
    @Column(name = "score2")
    var score2: Int = 0,
    @Column(name = "termine")
    var termine: Boolean = false,
    @Column(name = "date")
    var date: LocalDate = LocalDate.now(),
    @Column(name = "image")
    var image: String = ""
)

@Repository
interface MatchRepository : JpaRepository<Matches, Long> {
    @Query("SELECT m FROM Matches m ORDER BY m.date desc")
    fun findAllOrderByMatchDateDesc(): List<Matches>
}

@Service
class MatchService(val matchRepository: MatchRepository) {
    fun getAll(): List<Matches> = matchRepository.findAll()

    fun save(matches: Matches) {
        matchRepository.save(matches)
    }

    fun updateMatch(id: Long, score1: Int, score2: Int, termineOuNon: Boolean): Matches? {
        val match = matchRepository.findById(id).orElse(null)
        if (match != null) {
            match.termine = termineOuNon
            match.score1 = score1
            match.score2 = score2
            return matchRepository.save(match)
        }
        return null
    }

}

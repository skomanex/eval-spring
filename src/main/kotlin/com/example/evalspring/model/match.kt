package com.example.evalspring.model

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
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
    var team1: String = "",
    @Column(name = "team2")
    var team2: String = "",
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
    @Query("SELECT m FROM Matches m WHERE m.date >= :date ORDER BY m.date desc")
    fun findMatchByDateDesc(@Param("date") date: LocalDate): List<Matches>
    @Query("SELECT m from Matches m where m.termine = :status ORDER BY m.date desc")
    fun findMatchByTermine(@Param("status") status: Boolean): List<Matches>
}

@Service
class MatchService(val matchRepository: MatchRepository) {
    fun getAll(): List<Matches> = matchRepository.findAll()

    fun getAll7Days(): List<Matches> {
        val now = LocalDate.now()
        val sevenDaysAgo = now.minusDays(7)
        return matchRepository.findMatchByDateDesc(sevenDaysAgo)
    }

    fun save(matches: Matches): Matches {
        return matchRepository.save(matches)
    }

    fun updateMatch(id: Long, score1: Int, score2: Int): Matches? {
        val match = matchRepository.findById(id).orElse(null)
        if (match != null) {
            match.score1 = score1
            match.score2 = score2
            return matchRepository.save(match)
        }
        return null
    }
    fun finishMatch(id: Long, termine: Boolean): Matches? {
        val match = matchRepository.findById(id).orElse(null)
        if (match != null) {
            match.termine = termine
            return matchRepository.save(match)
        }
        return null
    }
    fun getOngoingMatches(): List<Matches> {
        return matchRepository.findMatchByTermine(false)
    }

    fun getFinishedMatches(): List<Matches> {
        return matchRepository.findMatchByTermine(true)
    }
}

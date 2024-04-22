package com.example.evalspring.model

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

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
    val termine: Boolean = false
)

@Repository
interface MatchRepository : JpaRepository<Matches, Long> {
    fun findByTermineFalse(): List<Matches>
}

@Service
class MatchService(val matchRepository: MatchRepository) {
    fun getAll(): List<Matches> = matchRepository.findAll()

    fun save(matches: Matches) {
        matchRepository.save(matches)
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
    fun getMatchWithTermineFalse(): List<Matches> {
        return matchRepository.findByTermineFalse()
    }
}

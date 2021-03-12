package tech.shali.boliboliapi.dao

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import tech.shali.boliboliapi.entity.Voice

interface VoiceDao : JpaRepository<Voice, String> {

    fun findByRJId(dlsiteId: String): Voice?

    fun findByKeyTextLike(keyword: String, pageable: Pageable): Page<List<Voice>>

}
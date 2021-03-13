package tech.shali.boliboliapi.dao

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import tech.shali.boliboliapi.entity.Voice

interface VoiceDao : JpaRepository<Voice, String> {

    fun findByRjId(dlsiteId: String): Voice?

    fun findByKeyTextLikeAndR18(keyword: String, R18: Boolean, pageable: Pageable): Page<List<Voice>>

}
package tech.shali.boliboliapi.dao

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import tech.shali.boliboliapi.entity.Voice
import tech.shali.boliboliapi.pojo.projections.SimpleVoice

interface VoiceDao : JpaRepository<Voice, String> {

    fun findByRjId(dlsiteId: String): Voice?

    fun findByKeyTextLikeAndR18False(keyword: String, pageable: Pageable): Page<List<SimpleVoice>>

    fun findByKeyTextLike(keyword: String, pageable: Pageable): Page<List<SimpleVoice>>

}
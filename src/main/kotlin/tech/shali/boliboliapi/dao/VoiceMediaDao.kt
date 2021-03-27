package tech.shali.boliboliapi.dao

import org.springframework.data.jpa.repository.JpaRepository
import tech.shali.boliboliapi.entity.Voice
import tech.shali.boliboliapi.entity.VoiceMedia
import tech.shali.boliboliapi.pojo.projections.SimpleVoiceMedia

interface VoiceMediaDao : JpaRepository<VoiceMedia, String> {
    fun findByVoice(voice: Voice): List<SimpleVoiceMedia>
}
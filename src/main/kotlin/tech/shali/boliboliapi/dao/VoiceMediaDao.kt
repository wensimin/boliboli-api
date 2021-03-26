package tech.shali.boliboliapi.dao

import org.springframework.data.jpa.repository.JpaRepository
import tech.shali.boliboliapi.entity.Voice
import tech.shali.boliboliapi.entity.VoiceMedia

interface VoiceMediaDao : JpaRepository<VoiceMedia, String> {
    fun findByVoice(voice: Voice): List<VoiceMedia>
}
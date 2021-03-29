package tech.shali.boliboliapi.pojo

import tech.shali.boliboliapi.entity.Voice
import tech.shali.boliboliapi.pojo.projections.SimpleVoiceMedia

class VoiceInfo(
    val voice: Voice,
    val medias: List<SimpleVoiceMedia>
)
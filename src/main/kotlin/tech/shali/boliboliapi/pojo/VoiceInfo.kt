package tech.shali.boliboliapi.pojo

import tech.shali.boliboliapi.entity.Voice
import tech.shali.boliboliapi.entity.base.MediaType

class VoiceInfo(
    val voice: Voice,
    val medias: List<SimpleVoiceMedia>
) {
    /**
     * media文件和所在
     */
    class SimpleVoiceMedia(
        val id: String,
        val filename: String,
        val type: MediaType,
        val size: Long,
        val trackLength: Long? = null,
        var folder: String = ""
    )
}
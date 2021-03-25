package tech.shali.boliboliapi.pojo

import tech.shali.boliboliapi.entity.VoiceTag
import tech.shali.boliboliapi.entity.base.MediaType
import java.util.*

class VoiceInfo(
    val id: String,
    val createDate: Date,
    val updateDate: Date,
    val title: String,
    val rjId: String,
    val mainImg: String,
    val url: String,
    val r18: Boolean = false,
    val tags: List<VoiceTag>,
    var medias: MutableMap<MediaType, MutableMap<String, MutableList<SimpleVoiceMedia>>> = EnumMap(MediaType::class.java)
) {
    /**
     * media文件和所在
     */
    class SimpleVoiceMedia(
        val id: String,
        val filename: String
    )
}
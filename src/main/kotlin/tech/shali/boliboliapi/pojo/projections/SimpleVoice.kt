package tech.shali.boliboliapi.pojo.projections

/**
 * Voice 的投影
 */
interface SimpleVoice {
    val id: String
    val title: String
    val rjId: String
    val mainImg: String
    val url: String
    val r18: Boolean

    interface SimpleVoiceTag {
        val key: String
        val values: List<String>
    }
}
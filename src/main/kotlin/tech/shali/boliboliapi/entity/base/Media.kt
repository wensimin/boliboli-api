package tech.shali.boliboliapi.entity.base

import tech.shali.boliboliapi.entity.base.MediaType.*
import javax.persistence.Column
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.MappedSuperclass



@MappedSuperclass
open class Media(
    @Column(nullable = true)
    var filename: String,
    @Column(nullable = true)
    var path: String,
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    var type: MediaType,
    @Column(nullable = true)
    var size: Long,
    /**
     * 播放长度
     */
    var trackLength: Long? = null,
) : Data() {
    companion object {
        private val IMAGE_EXTENSION: Regex = Regex("(png|jpg|gif|bmp|jpeg|PNG|JPG|GIF|BMP|JPEG)")
        private val VIDEO_EXTENSION: Regex = Regex("(mp4|MP4|rmvb|RMVB|mkv|MKV|wmv|WMV|AVI|avi)")
        private val AUDIO_EXTENSION: Regex = Regex("(mp3|wav|flac|MP3|WAV|FLAC)")
        private val TEXT_EXTENSION: Regex = Regex("(txt|TXT|pdf|PDF)")
        private val LRC_EXTENSION: Regex = Regex("(lrc|ass|LRC|ASS)")

        private val MEDIA_MATCHER: Map<MediaType, Regex> = mapOf(
            IMAGE to IMAGE_EXTENSION,
            VIDEO to VIDEO_EXTENSION,
            AUDIO to AUDIO_EXTENSION,
            TEXT to TEXT_EXTENSION,
            LRC to LRC_EXTENSION
        )

        /**
         * 获取扩展名对应的类型
         */
        fun getType(extension: String): MediaType? {
            MEDIA_MATCHER.forEach { (type, matcher) ->
                if (matcher.matches(extension)) {
                    return type
                }
            }
            return null
        }
    }


}


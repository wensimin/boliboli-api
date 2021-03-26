package tech.shali.boliboliapi.entity

import tech.shali.boliboliapi.entity.base.Media
import tech.shali.boliboliapi.entity.base.MediaType
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class VoiceMedia(
    @ManyToOne
    val voice: Voice,
    filename: String, path: String, type: MediaType, size: Long, trackLength: Long?,
    /**
     * 所在相对文件夹
     */
    var folder: String = ""
) : Media(filename, path, type, size, trackLength)
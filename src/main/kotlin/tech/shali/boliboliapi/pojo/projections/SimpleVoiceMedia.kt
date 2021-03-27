package tech.shali.boliboliapi.pojo.projections

import tech.shali.boliboliapi.entity.base.MediaType

/**
 * media文件和所在
 */
interface SimpleVoiceMedia {
    val id: String
    val filename: String
    val type: MediaType
    val size: Long
    val trackLength: Long?
    var folder: String?
}
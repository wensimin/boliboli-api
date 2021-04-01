package tech.shali.boliboliapi.pojo.projections

import tech.shali.boliboliapi.entity.base.MediaType

/**
 * voice media 投影
 */
interface SimpleVoiceMedia {
    val id: String
    val filename: String
    val type: MediaType
    val size: Long
    val trackLength: Long?
    var folder: String
}
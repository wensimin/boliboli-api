package tech.shali.boliboliapi.entity

import tech.shali.boliboliapi.entity.base.Data
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType

@Entity
class VoiceTag(
    @Column(nullable = false)
    val key: String,
    @ElementCollection(fetch = FetchType.EAGER)
    val values: Set<String>
) : Data()
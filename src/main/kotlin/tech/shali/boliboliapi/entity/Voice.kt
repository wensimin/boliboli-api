package tech.shali.boliboliapi.entity

import tech.shali.boliboliapi.entity.base.Resource
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany

@Entity
class Voice(
    @Column(nullable = false)
    val title: String,
    @Column(nullable = false, unique = true)
    val RJId: String,
    @Column(nullable = false)
    val mainImg: String,
    @OneToMany(fetch = FetchType.EAGER)
    val tags: Set<VoiceTag>
) : Resource() {
    init {
        var tagText = ""
        tags.forEach { tag ->
            tagText += tag.values.joinToString()
        }
        keyText = title + RJId + tagText
    }
}
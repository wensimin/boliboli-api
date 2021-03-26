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
    val rjId: String,
    @Column(nullable = false)
    val mainImg: String,
    @Column(nullable = false)
    val url: String,
    @OneToMany(fetch = FetchType.EAGER)
    val tags: List<VoiceTag>
) : Resource() {


    init {
        var tagText = ""
        tags.forEach { tag ->
            tagText += tag.values.joinToString()
        }
        // 关键字目前由title 和rj id 以及tags
        keyText = title + rjId + tagText
    }
}
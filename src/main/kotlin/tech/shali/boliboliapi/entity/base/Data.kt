package tech.shali.boliboliapi.entity.base

import tech.shali.boliboliapi.entity.listener.DataEntityListener
import java.util.*
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(DataEntityListener::class)
open class Data(
    @Id @Column(nullable = false)
    val id: String = UUID.randomUUID().toString(),
    @Column(nullable = false)
    val createDate: Date = Date(),
    @Column(nullable = false)
    var updateDate: Date = Date()
) {

    fun beforeUpdate() {
        updateDate = Date()
    }
}
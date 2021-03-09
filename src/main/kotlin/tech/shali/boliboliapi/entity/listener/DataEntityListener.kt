package tech.shali.boliboliapi.entity.listener

import tech.shali.boliboliapi.entity.Data
import javax.persistence.PreUpdate

class DataEntityListener {
    @PreUpdate
    fun methodExecuteBeforeUpdate(reference: Data) {
        reference.beforeUpdate()
    }

}

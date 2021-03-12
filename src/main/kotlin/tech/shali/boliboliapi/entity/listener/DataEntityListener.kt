package tech.shali.boliboliapi.entity.listener

import tech.shali.boliboliapi.entity.base.Data
import javax.persistence.PreUpdate

class DataEntityListener {
    @PreUpdate
    fun methodExecuteBeforeUpdate(reference: Data) {
        reference.beforeUpdate()
    }

}

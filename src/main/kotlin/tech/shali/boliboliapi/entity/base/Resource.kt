package tech.shali.boliboliapi.entity.base

import org.hibernate.annotations.Type
import javax.persistence.Lob
import javax.persistence.MappedSuperclass

/**
 * 所有resource公用属性
 */
@MappedSuperclass
open class Resource(
    var R18: Boolean = false,
    /**
     * 用于查询的keyText
     */
    @Type(type = "org.hibernate.type.TextType")
    @Lob
    var keyText: String = ""
) : Data()
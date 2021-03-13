package tech.shali.boliboliapi.entity.base

import org.hibernate.annotations.Type
import javax.persistence.Lob
import javax.persistence.MappedSuperclass

/**
 * resource公用属性
 * resource 的定义为一个单独的资源，如音声&视频合集之类
 * 与media为 @oneToMany 关系
 */
@MappedSuperclass
open class Resource(
    var R18: Boolean = false,
    /**
     * 用于查询的keyText
     */
    @Type(type = "org.hibernate.type.TextType")
    @Lob
    var keyText: String = "",
    /**
     * 文件目录的json tree
     */
    @Type(type = "org.hibernate.type.TextType")
    @Lob
    var fileTree: String = ""
) : Data()
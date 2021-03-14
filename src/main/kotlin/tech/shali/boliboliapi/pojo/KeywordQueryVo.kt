package tech.shali.boliboliapi.pojo

import tech.shali.boliboliapi.pojo.base.PageVo

data class KeywordQueryVo(
    val keyword: String = "",
    val r18: Boolean = false,
    val page: PageVo = PageVo()
)
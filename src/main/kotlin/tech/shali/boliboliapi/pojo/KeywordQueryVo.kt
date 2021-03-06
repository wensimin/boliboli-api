package tech.shali.boliboliapi.pojo

import tech.shali.boliboliapi.pojo.base.PageVo

data class KeywordQueryVo(
    val keyword: String = "",
    val needR18: Boolean = false,
    val page: PageVo = PageVo()
)
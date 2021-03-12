package tech.shali.boliboliapi.pojo.base

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort.Direction

/**
 * 用于分页参数
 */
data class PageVo(
    var page: Int = 0,
    var size: Int = 8,
    var direction: Direction = Direction.ASC,
    var properties: Set<String> = emptySet()
) {
    fun toPageRequest(): PageRequest {
        return if (properties.isEmpty()) {
            PageRequest.of(page, size)
        } else {
            PageRequest.of(page, size, direction, *properties.toTypedArray())
        }
    }
}
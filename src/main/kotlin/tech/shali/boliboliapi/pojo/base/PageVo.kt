package tech.shali.boliboliapi.pojo.base

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort.Direction

/**
 * 用于分页参数
 */
open class PageVo(
    private val page: Int = 0,
    private val size: Int = 8,
    private val direction: Direction = Direction.ASC,
    private val properties: Set<String> = emptySet()
) {
    fun toPageRequest(): PageRequest {
        return if (properties.isEmpty()) {
            PageRequest.of(page, size)
        } else {
            PageRequest.of(page, size, direction, *properties.toTypedArray())
        }
    }
}
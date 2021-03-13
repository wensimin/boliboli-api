package tech.shali.boliboliapi.pojo.exception

import tech.shali.boliboliapi.handler.ErrorType

class SystemException(
    override val message: String,
    val type: ErrorType = ErrorType.ERROR
) : RuntimeException()
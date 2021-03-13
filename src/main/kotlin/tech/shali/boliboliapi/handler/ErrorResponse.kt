package tech.shali.boliboliapi.handler

class ErrorResponse(val type: ErrorType, val message: String)
enum class ErrorType {
    ERROR, PARAM, AUTH
}

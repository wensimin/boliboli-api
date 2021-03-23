package tech.shali.boliboliapi.handler

class ErrorResponse(val error: ErrorType, val message: String)
enum class ErrorType {
    ERROR, PARAM, AUTH
}

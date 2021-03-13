package tech.shali.boliboliapi.config

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import tech.shali.boliboliapi.handler.ErrorType
import tech.shali.boliboliapi.pojo.exception.SystemException

/**
 * 检查token拥有指定权限
 */
fun JwtAuthenticationToken.hasAuth(auth: Auth): Boolean {
    return this.authorities.map { e -> e.authority }.contains(auth.name)
}

/**
 * 检查权限，没有权限时应该抛出认证异常
 *
 */
fun JwtAuthenticationToken.checkAuth(auth: Auth) {
    //TODO 权限异常&错误公共处理
    if (!hasAuth(auth)) throw SystemException("权限不足", ErrorType.AUTH)
}

enum class Auth {
    R18, ADMIN
}

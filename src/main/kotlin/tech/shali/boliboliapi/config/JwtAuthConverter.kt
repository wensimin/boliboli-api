package tech.shali.boliboliapi.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt

/**
 * 转换jwt的权限
 */
class JwtAuthConverter : Converter<Jwt, Collection<GrantedAuthority>> {
    override fun convert(source: Jwt): Collection<GrantedAuthority> {
        // scope & auth 本项目 混用
        val scope = source.getClaim<Collection<String>>("scope")
        val auth = source.getClaim<Collection<String>>("auth")
        return (scope + auth).map { s -> SimpleGrantedAuthority(s) }
    }
}
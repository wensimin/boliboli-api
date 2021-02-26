package tech.shali.boliboliapi.controller

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*
import tech.shali.boliboliapi.entity.SysUser
import tech.shali.boliboliapi.pojo.RegisterVo
import tech.shali.boliboliapi.service.SysUserService
import javax.validation.Valid

@RestController
@RequestMapping("public")
class PublicController(private val sysUserService: SysUserService) {

    @PostMapping("register")
    fun register(@RequestBody @Valid registerVo: RegisterVo): SysUser {
        return sysUserService.register(registerVo)
    }

    @GetMapping("test")
    fun test(token: JwtAuthenticationToken): String {
        return token.name
    }

    @GetMapping("admin")
    fun admin(): String {
        return "admin"
    }
}
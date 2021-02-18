package tech.shali.boliboliapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import tech.shali.boliboliapi.entity.SysUser
import tech.shali.boliboliapi.service.SysUserService
import java.security.Principal

/**
 * 用户controller
 */
@RestController("user")
class UserController(val SysUserService: SysUserService) {
    @GetMapping("info")
    fun info(principal: Principal): SysUser {
        return SysUserService.info(principal)
    }
}
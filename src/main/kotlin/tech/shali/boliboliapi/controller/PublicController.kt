package tech.shali.boliboliapi.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import tech.shali.boliboliapi.entity.SysUser
import tech.shali.boliboliapi.pojo.RegisterVo
import tech.shali.boliboliapi.service.SysUserService
import javax.validation.Valid

@RestController("public")
class PublicController(val sysUserService: SysUserService) {

    @PostMapping("register")
    fun register(@RequestBody @Valid registerVo: RegisterVo): SysUser {
        return sysUserService.register(registerVo)
    }
}
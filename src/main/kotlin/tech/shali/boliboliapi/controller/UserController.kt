package tech.shali.boliboliapi.controller

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("user")
class UserController {

    @GetMapping
    fun user(token: JwtAuthenticationToken): JwtAuthenticationToken {
        return token
    }
}
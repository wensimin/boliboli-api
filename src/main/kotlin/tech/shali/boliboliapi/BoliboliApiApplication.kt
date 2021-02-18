package tech.shali.boliboliapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer

// FIXME spring boot 2.0删除了老的authorization server 支持,但是没有提供新的.
//  因此在这里使用过期的原版库.最终将替换到新版
@Suppress("DEPRECATION")
@EnableAuthorizationServer
@SpringBootApplication
class BoliboliApiApplication

fun main(args: Array<String>) {
    runApplication<BoliboliApiApplication>(*args)
}

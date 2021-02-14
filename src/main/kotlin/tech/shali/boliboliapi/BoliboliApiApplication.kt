package tech.shali.boliboliapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import tech.shali.boliboliapi.entity.SysUser

@SpringBootApplication
class BoliboliApiApplication

fun main(args: Array<String>) {
    val a = SysUser("","")
    a.username="1";
    runApplication<BoliboliApiApplication>(*args)
}

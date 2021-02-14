package tech.shali.boliboliapi.dao

import org.springframework.data.jpa.repository.JpaRepository
import tech.shali.boliboliapi.entity.SysUser

interface SysUserDao : JpaRepository<SysUser, String> {
    fun findByUsername(username: String): SysUser?
}
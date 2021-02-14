package tech.shali.boliboliapi.service

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import tech.shali.boliboliapi.dao.SysUserDao
import tech.shali.boliboliapi.entity.SysUser
import tech.shali.boliboliapi.pojo.RegisterVo
import java.security.Principal

@Service
class SysUserService(
    private val userDao: SysUserDao,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    override fun loadUserByUsername(username: String): SysUser {
        return userDao.findByUsername(username) ?: throw UsernameNotFoundException("未找到用户")
    }

    fun register(vo: RegisterVo): SysUser {
        val user = SysUser(vo.username, vo.password)
        user.password = passwordEncoder.encode(user.password)
        return userDao.save(user)
    }

    fun info(principal: Principal): SysUser {
        return loadUserByUsername(principal.name)
    }

}

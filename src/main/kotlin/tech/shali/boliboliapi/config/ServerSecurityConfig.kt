package tech.shali.boliboliapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import tech.shali.boliboliapi.entity.SysRole
import tech.shali.boliboliapi.service.SysUserService


@Configuration
class ServerSecurityConfig (val userDetailsService: SysUserService): WebSecurityConfigurerAdapter() {


    @Bean
    override fun userDetailsService(): UserDetailsService? {
        return userDetailsService
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/public/register/").hasAuthority(SysRole.ADMIN.name)
            .antMatchers("/public/**").permitAll()
            .and().cors()
            .and().csrf().disable()
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}
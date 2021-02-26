package tech.shali.boliboliapi.config

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity
class ResourceServerConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .authorizeRequests { config ->
                config.mvcMatchers("public/admin").hasAuthority("ADMIN")
                config.anyRequest().authenticated()
            }
            .oauth2ResourceServer()
            .jwt()
        return http.build()
    }

}
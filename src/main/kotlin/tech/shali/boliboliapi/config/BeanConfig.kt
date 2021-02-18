package tech.shali.boliboliapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore

@Configuration
class BeanConfig(private val redisConnection: RedisConnectionFactory) {
    /**
     * 密码加密方式
     *
     * @return 密码加密器
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * token使用redis
     */
    @Bean
    fun tokenStore(): TokenStore {
        return RedisTokenStore(redisConnection);
    }
}
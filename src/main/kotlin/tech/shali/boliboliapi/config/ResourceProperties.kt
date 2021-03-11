package tech.shali.boliboliapi.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "resource.props")
data class ResourceProperties(
    val voicePaths: Set<String>,
    val proxy: Proxy
) {
    data class Proxy(val host: String, val port: Int)
}
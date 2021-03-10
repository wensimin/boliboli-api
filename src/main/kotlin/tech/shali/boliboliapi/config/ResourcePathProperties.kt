package tech.shali.boliboliapi.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "resource.path")
data class ResourcePathProperties(
    val voice: Set<String>
)
package tech.shali.boliboliapi.controller

import org.springframework.core.io.FileSystemResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.shali.boliboliapi.service.VoiceMediaService


@RestController
@RequestMapping("voice/media")
class VoiceMediaController(private val voiceMediaService: VoiceMediaService) {

    @GetMapping("{id}")
    fun get(@PathVariable id: String, token: JwtAuthenticationToken): ResponseEntity<FileSystemResource> {
        val file = voiceMediaService.get(id, token)
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(FileSystemResource(file))
    }
}
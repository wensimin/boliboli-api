package tech.shali.boliboliapi.controller

import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.shali.boliboliapi.service.VoiceMediaService
import java.io.FileInputStream


@RestController
@RequestMapping("voice/media")
class VoiceMediaController(private val voiceMediaService: VoiceMediaService) {

    @GetMapping("{id}")
    fun get(@PathVariable id: String, token: JwtAuthenticationToken): ResponseEntity<InputStreamResource> {
        val file = voiceMediaService.get(id, token)
        val resource = InputStreamResource(FileInputStream(file))
//        val filename = URLEncoder.encode(file.name, StandardCharsets.UTF_8.toString())
        return ResponseEntity.ok()
            //TODO 文件名乱码,注释的处理方式可能对浏览器有效,未测试
//            .header("Content-Disposition", "attachment; filename*=UTF-8''$filename")
            .contentLength(file.length())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource)
    }
}
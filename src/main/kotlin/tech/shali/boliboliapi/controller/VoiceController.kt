package tech.shali.boliboliapi.controller

import org.springframework.data.domain.Page
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.shali.boliboliapi.pojo.KeywordQueryVo
import tech.shali.boliboliapi.pojo.VoiceInfo
import tech.shali.boliboliapi.pojo.projections.SimpleVoice
import tech.shali.boliboliapi.service.VoiceService

@RestController
@RequestMapping("voice")
class VoiceController(private val voiceService: VoiceService) {

    @GetMapping
    fun search(keywordQueryVo: KeywordQueryVo, token: JwtAuthenticationToken): Page<List<SimpleVoice>> {
        return voiceService.findByKeyword(keywordQueryVo, token)
    }

    @GetMapping("{id}")
    fun info(@PathVariable id: String, token: JwtAuthenticationToken): VoiceInfo {
        return voiceService.info(id, token)
    }

}
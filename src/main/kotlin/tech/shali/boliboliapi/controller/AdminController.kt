package tech.shali.boliboliapi.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.shali.boliboliapi.service.LoadVoiceService

@RestController
@RequestMapping("admin")
class AdminController(private val loadVoiceService: LoadVoiceService) {

    /**
     * admin接口 从本地读取voice
     */
    @PostMapping("load/voice")
    fun loadVoice() {
        loadVoiceService.loadEntityByAllPath()
    }
}
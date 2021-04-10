package tech.shali.boliboliapi.task

import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import tech.shali.boliboliapi.service.LoadVoiceService


@Component
class ReadFileTask(
    private val loadVoiceService: LoadVoiceService
) {


    /**
     * 工程启动时读取file
     */
    @EventListener(ContextRefreshedEvent::class)
    fun readAllVoiceFile() {
        loadVoiceService.loadEntityByAllPath()
    }
}
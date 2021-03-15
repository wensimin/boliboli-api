package tech.shali.boliboliapi.task

import org.slf4j.Logger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import tech.shali.boliboliapi.service.LoadVoiceService


@Component
class ReadFileTask(
    private val loadVoiceService: LoadVoiceService,
    private val log: Logger
) {


    /**
     * 每60秒时读取file
     */
    @Scheduled(fixedRate = 60000)
    fun readAllVoiceFile() {
        log.debug("read all file")
        loadVoiceService.loadEntityByAllPath()
    }
}
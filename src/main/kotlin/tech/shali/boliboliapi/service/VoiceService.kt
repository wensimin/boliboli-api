package tech.shali.boliboliapi.service

import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import tech.shali.boliboliapi.config.ResourcePathProperties
import java.io.File

@Service
class VoiceService(private val resourcePathProperties: ResourcePathProperties) {

    /**
     * 读取配置path下的文件
     * 构建voice的entity
     */
    @EventListener(ContextRefreshedEvent::class)
    fun loadEntityByFile() {
        resourcePathProperties.voice.forEach(this::loadEntityByFile)
    }

    private fun loadEntityByFile(path: String) {
        val directoryPath = File(path)
        val list = directoryPath.list()
        list.forEach(System.out::println)
    }
}
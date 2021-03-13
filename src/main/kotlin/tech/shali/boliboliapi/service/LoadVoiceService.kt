package tech.shali.boliboliapi.service

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import tech.shali.boliboliapi.config.ResourceProperties
import java.io.File

//文件夹命名约定 来自dlsite的voice
private val DLSITE_REX: Regex = Regex("RJ[0-9]+")

@Service
class LoadVoiceService(
    private val voiceService: VoiceService,
    private val resourceProperties: ResourceProperties,
    private val log: Logger
) {

    /**
     * 读取配置path下的文件
     * 构建voice的entity
     */
    //TODO test 启动时测试
    @EventListener(ContextRefreshedEvent::class)
    fun loadEntityByFile() {
        resourceProperties.voicePaths.forEach(this::loadEntityByFile)
    }

    fun loadEntityByFile(path: String) {
        File(path).list()?.forEach { fileName ->
            val dlsiteId = DLSITE_REX.find(fileName)
            dlsiteId?.let {
                GlobalScope.launch {
                    try {
                        voiceService.loadEntityByDlsiteFile(dlsiteId.value, "$path/$fileName")
                    } catch (e: Exception) {
                        log.error("${dlsiteId.value} 发生错误 ${e.message}")
                    }
                }
            }
        }
    }
}
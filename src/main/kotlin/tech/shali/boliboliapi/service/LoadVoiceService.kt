package tech.shali.boliboliapi.service

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.springframework.stereotype.Service
import tech.shali.boliboliapi.config.ResourceProperties
import java.io.File



@Service
class LoadVoiceService(
    private val voiceService: VoiceService,
    private val resourceProperties: ResourceProperties,
    private val log: Logger
) {

    companion object {
        //文件夹命名约定 来自dlsite的voice
        private val DLSITE_REX: Regex = Regex("RJ[0-9]+")
    }

    /**
     * 读取配置path下的文件
     * 构建voice的entity
     */
    fun loadEntityByAllPath() {
        resourceProperties.voicePaths.forEach(this::loadEntityByPath)
    }

    /**
     * 读取指定path下的文件
     */
    fun loadEntityByPath(path: String) {
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
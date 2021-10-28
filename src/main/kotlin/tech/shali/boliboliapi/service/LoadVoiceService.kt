package tech.shali.boliboliapi.service

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
    private var reading: Boolean = false

    companion object {
        //文件夹命名约定 来自dlsite的voice
        private val DLSITE_REX: Regex = Regex("RJ[0-9]+")
    }

    /**
     * 读取配置path下的文件
     * 构建voice的entity
     */
    fun loadEntityByAllPath() {
        Thread {
            if (reading) {
                log.warn("已经有在扫描的工作")
                return@Thread
            }
            reading = true
            log.info("开始扫描全部音声")
            resourceProperties.voicePaths.forEach(this::loadEntityByPath)
            log.info("音声扫描结束")
            reading = false
        }.start()
    }

    /**
     * 读取指定path下的文件
     */
    fun loadEntityByPath(path: String) {
        log.debug("read $path file")
        File(path).list()?.forEach { fileName ->
            val dlsiteId = DLSITE_REX.find(fileName)
            dlsiteId?.let {
                try {
                    voiceService.loadEntityByDlsiteFile(dlsiteId.value, "$path/$fileName")
                } catch (e: Exception) {
                    log.error("${dlsiteId.value} 发生错误中断 ${e.message}")
                }
            }
        }
    }
}
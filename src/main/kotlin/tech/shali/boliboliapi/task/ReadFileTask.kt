package tech.shali.boliboliapi.task

import org.slf4j.Logger
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import tech.shali.boliboliapi.config.ResourceProperties
import tech.shali.boliboliapi.service.LoadVoiceService
import java.nio.file.FileSystems
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchKey


@Component
class ReadFileTask(
    private val loadVoiceService: LoadVoiceService,
    private val log: Logger,
    resourceProperties: ResourceProperties
) {
    private var watchService = FileSystems.getDefault().newWatchService()
    private val watchMap: MutableMap<WatchKey, String> = HashMap()

    init {
        resourceProperties.voicePaths.forEach {
            // 目前只监听新增
            watchMap[Paths.get(it).register(watchService, StandardWatchEventKinds.ENTRY_CREATE)] = it
        }
    }

    /**
     * 每分钟一次的读取voice file
     */
    @Scheduled(fixedRate = 60000)
    fun readVoiceFile() {
        watchService.poll()?.let {
            log.info("voice file added, start read")
            loadVoiceService.loadEntityByPath(watchMap[it]!!)
        }
    }

    /**
     * 工程启动时读取file
     */
    @EventListener(ContextRefreshedEvent::class)
    fun readAllVoiceFile() {
        log.info("read all file")
        loadVoiceService.loadEntityByAllPath()
    }
}
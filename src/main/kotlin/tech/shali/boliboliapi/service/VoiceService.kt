package tech.shali.boliboliapi.service

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import tech.shali.boliboliapi.config.ResourceProperties
import java.io.File

//文件夹命名约定 来自dlsite的voice
private const val DLSITE_REX: String = "[Rr][Jj][0-9]+"

@Service
class VoiceService(private val resourceProperties: ResourceProperties) {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 读取配置path下的文件
     * 构建voice的entity
     */
    //TODO test 启动时测试
    @EventListener(ContextRefreshedEvent::class)
    fun loadEntityByFile() {
        resourceProperties.voicePaths.forEach(this::loadEntityByFile)
    }

    private fun loadEntityByFile(path: String) {
        val directoryPath = File(path)
        val list = directoryPath.list()
        val regex = Regex(DLSITE_REX)
        list?.forEach { fileName ->
            val dlsiteId = regex.find(fileName)
            dlsiteId?.let {
                GlobalScope.launch {
                    try {
                        this@VoiceService.loadEntityByDlsiteFile(dlsiteId.value, "$path/$fileName")
                    } catch (e: Exception) {
                        log.error("${dlsiteId.value} 发生错误 ${e.message}")
                    }
                }
            }
        }
    }

    /**
     * dlsite 音声
     */
    private fun loadEntityByDlsiteFile(dlsiteId: String, fileName: String) {
        if (!this.isNeedLoadDLFile(dlsiteId)) return
        val doc = Jsoup.connect("https://www.dlsite.com/maniax/work/=/product_id/$dlsiteId.html/")
            .proxy(resourceProperties.proxy.host, resourceProperties.proxy.port).get()
        val tagsMap = this.getTagsMap(doc)
        val title = this.getTitle(doc)
        val mainImg = this.getMainImg(doc)
        log.info("id=$dlsiteId file=$fileName title=$title mainImg=$mainImg")
        log.info("id=$dlsiteId tags= $tagsMap")

    }

    private fun getMainImg(doc: Document): String {
        val src = doc.select(".slider_items li img").attr("src")
        // 添加协议头
        return "https:$src"
    }

    private fun getTitle(doc: Document): String {
        return doc.select("#work_name a").text()
    }

    /**
     * 获取所有tag
     */
    private fun getTagsMap(doc: Document): LinkedHashMap<String, List<String>> {
        // 获取标签
        val tags = doc.select("#work_outline tr")
        val tagsMap = LinkedHashMap<String, List<String>>()
        tags?.forEach { tag ->
            //key&value 将dl上分隔符' / '转换为 单一空格做分隔
            val kv = tag.text().replace(" / ", " ").trim().split(" ")
            val value = kv.subList(1, kv.size)
            tagsMap[kv.first()] = value
        }
        return tagsMap
    }

    /**
     * 检查文件是否需要读取
     */
    private fun isNeedLoadDLFile(dlsiteId: String): Boolean {
        //TODO 检查是否需要再读取数据
        return true
    }
}



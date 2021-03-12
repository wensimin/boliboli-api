package tech.shali.boliboliapi.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.shali.boliboliapi.config.ResourceProperties
import tech.shali.boliboliapi.dao.VoiceDao
import tech.shali.boliboliapi.dao.VoiceTagDao
import tech.shali.boliboliapi.entity.Voice
import tech.shali.boliboliapi.entity.VoiceTag
import java.util.*


@Service
class VoiceService(
    private val resourceProperties: ResourceProperties,
    private val objectMapper: ObjectMapper,
    private val voiceDao: VoiceDao,
    private val voiceTagDao: VoiceTagDao
) {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun findByKeyword(keyword: String): List<Voice> {
        return voiceDao.findByKeyTextLike("%$keyword%")
    }

    /**
     * dlsite 音声
     */
    @Transactional
    fun loadEntityByDlsiteFile(dlsiteId: String, path: String) {
        if (!this.isNeedLoadDLFile(dlsiteId)) return
        val doc = Jsoup.connect("https://www.dlsite.com/maniax/work/=/product_id/$dlsiteId.html/")
            .proxy(resourceProperties.proxy.host, resourceProperties.proxy.port).get()
        val tagMaps = this.getTagsMap(doc)
        val title = this.getTitle(doc)
        val mainImg = this.getMainImg(doc)
        log.info("id=$dlsiteId path=$path title=$title mainImg=$mainImg")
        log.info("id=$dlsiteId tags= $tagMaps")
        val tags = tagMaps.map { (k, v) -> VoiceTag(k, v) }.toSet()
        this.voiceTagDao.saveAll(tags)
        val voice = Voice(title, dlsiteId, mainImg, tags)
        voice.R18 = isR18(tagMaps)
        this.voiceDao.save(voice)
        //TODO JSON TREE
        objectMapper.createObjectNode()
    }

    private fun isR18(tagMaps: LinkedHashMap<String, Set<String>>): Boolean {
        return tagMaps["年齢指定"]?.first() == "18禁"
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
    private fun getTagsMap(doc: Document): LinkedHashMap<String, Set<String>> {
        // 获取标签
        val tags = doc.select("#work_outline tr")
        val tagsMap = LinkedHashMap<String, Set<String>>()
        tags?.forEach { tag ->
            //key&value 将dl上分隔符' / '转换为 单一空格做分隔
            val kv = tag.text().replace(" / ", " ").trim().split(" ")
            val value = kv.subList(1, kv.size).toSet()
            tagsMap[kv.first()] = value
        }
        return tagsMap
    }


    /**
     * 检查文件是否需要读取
     */
    private fun isNeedLoadDLFile(dlsiteId: String): Boolean {
        //TODO 检查是否需要再读取数据
        return voiceDao.findByRJId(dlsiteId) == null
    }
}



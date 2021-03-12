package tech.shali.boliboliapi.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.shali.boliboliapi.config.ResourceProperties
import tech.shali.boliboliapi.dao.VoiceDao
import tech.shali.boliboliapi.dao.VoiceTagDao
import tech.shali.boliboliapi.entity.Voice
import tech.shali.boliboliapi.entity.VoiceTag
import tech.shali.boliboliapi.pojo.KeywordQueryVo
import java.io.File
import java.util.*


@Service
class VoiceService(
    private val resourceProperties: ResourceProperties,
    private val objectMapper: ObjectMapper,
    private val voiceDao: VoiceDao,
    private val voiceTagDao: VoiceTagDao
) {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun findByKeyword(keywordQueryVo: KeywordQueryVo): Page<List<Voice>> {
        return voiceDao.findByKeyTextLike("%${keywordQueryVo.keyword}%", keywordQueryVo.page.toPageRequest())
    }

    /**
     * dlsite 音声
     */
    @Transactional
    fun loadEntityByDlsiteFile(dlsiteId: String, path: String) {
        // test
        if (!this.isNeedLoadDLFile(dlsiteId)) return
        val doc = Jsoup.connect("https://www.dlsite.com/maniax/work/=/product_id/$dlsiteId.html/")
            .proxy(resourceProperties.proxy.host, resourceProperties.proxy.port).get()
        val tagMaps = this.getTagsMap(doc)
        val title = this.getTitle(doc)
        val mainImg = this.getMainImg(doc)
        log.info("id=$dlsiteId path=$path title=$title mainImg=$mainImg")
        log.info("id=$dlsiteId tags= $tagMaps")
        val tags = tagMaps.map { (k, v) -> VoiceTag(k, v) }
        this.voiceTagDao.saveAll(tags)
        val voice = Voice(title, dlsiteId, mainImg, tags)
        voice.R18 = isR18(tagMaps)
        this.voiceDao.save(voice)
        // 获取文件树json
        val treeJson = this.readJsonByFile(path).toString()

    }

    /**
     * 递归把文件树转json Array
     */
    private fun readJsonByFile(startDir: String): ArrayNode {
        val dir = File(startDir)
        val array = objectMapper.createArrayNode()
        val files = dir.listFiles()
        files?.forEach { file ->
            if (file.isDirectory) {
                val node = array.addObject()
                node.putArray(file.name).addAll(readJsonByFile(file.absolutePath))
            } else {
                //TODO create file
                array.add(file.name)
            }
        }
        return array
    }


    private fun isR18(tagMaps: LinkedHashMap<String, List<String>>): Boolean {
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
        return voiceDao.findByRJId(dlsiteId) == null
    }
}



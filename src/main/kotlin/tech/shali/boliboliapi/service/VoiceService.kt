package tech.shali.boliboliapi.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.Logger
import org.springframework.data.domain.Page
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.shali.boliboliapi.config.Auth
import tech.shali.boliboliapi.config.ResourceProperties
import tech.shali.boliboliapi.config.checkAuth
import tech.shali.boliboliapi.dao.VoiceDao
import tech.shali.boliboliapi.dao.VoiceMediaDao
import tech.shali.boliboliapi.dao.VoiceTagDao
import tech.shali.boliboliapi.entity.Voice
import tech.shali.boliboliapi.entity.VoiceMedia
import tech.shali.boliboliapi.entity.VoiceTag
import tech.shali.boliboliapi.entity.base.Media
import tech.shali.boliboliapi.entity.base.MediaType
import tech.shali.boliboliapi.handler.ErrorType
import tech.shali.boliboliapi.pojo.KeywordQueryVo
import tech.shali.boliboliapi.pojo.VoiceInfo
import tech.shali.boliboliapi.pojo.exception.SystemException
import tech.shali.boliboliapi.pojo.projections.SimpleVoice
import tech.shali.boliboliapi.utils.copyPropsTo
import java.io.File
import java.util.*


@Service
class VoiceService(
    private val resourceProperties: ResourceProperties,
    private val objectMapper: ObjectMapper,
    private val voiceDao: VoiceDao,
    private val voiceTagDao: VoiceTagDao,
    private val voiceMediaDao: VoiceMediaDao,
    private val log: Logger
) {

    /**
     * 关键字查询
     */
    fun findByKeyword(keywordQueryVo: KeywordQueryVo, token: JwtAuthenticationToken): Page<List<SimpleVoice>> {
        if (keywordQueryVo.r18) token.checkAuth(Auth.R18)
        return voiceDao.findByKeyTextLikeAndR18(
            "%${keywordQueryVo.keyword}%",
            keywordQueryVo.r18,
            keywordQueryVo.page.toPageRequest()
        )
    }

    /**
     * id info
     */
    fun info(id: String, token: JwtAuthenticationToken): VoiceInfo {
        val voice = voiceDao.findById(id).or {
            throw SystemException("未找到音声", ErrorType.NOT_FOUND)
        }.get()
        return voice.copyPropsTo(VoiceInfo::class).apply {
            val node: ArrayNode = objectMapper.readTree(voice.fileTree) as ArrayNode
            fileTree2Medias(node, medias)
        }
    }

    /**
     * 把整个文件树转化成2层树
     *
     */
    private fun fileTree2Medias(
        node: JsonNode,
        medias: MutableMap<MediaType, MutableMap<String, MutableList<VoiceInfo.SimpleVoiceMedia>>>,
        parent: String = ""
    ) {
        if (node.isArray) {
            node.forEach {
                fileTree2Medias(it, medias, parent)
            }
        } else if (node.isObject) {
            // 拥有id的为left object
            if (node.has("id")) {
                readLeafObject(node, medias, parent)
            } else {
                node.fields().forEach {
                    val newParent = if (parent.isEmpty()) it.key else "$parent.${it.key}"
                    fileTree2Medias(it.value, medias, newParent)
                }
            }
        }
    }

    /**
     * 读取叶子节点
     */
    private fun readLeafObject(
        node: JsonNode,
        medias: MutableMap<MediaType, MutableMap<String, MutableList<VoiceInfo.SimpleVoiceMedia>>>,
        parent: String
    ) {
        val type = MediaType.valueOf(node["type"].asText())
        var typeMap = medias[type]
        if (typeMap == null) {
            typeMap = linkedMapOf()
            medias[type] = typeMap
        }
        var list = typeMap[parent]
        if (list == null) {
            list = LinkedList()
            typeMap[parent] = list
        }
        list.add(
            VoiceInfo.SimpleVoiceMedia(
                node["id"].asText(), node["name"].asText()
            )
        )
    }


    /**
     * dlsite 音声
     * voice主对象与文件树只创建一次
     *
     */
    @Transactional
    fun loadEntityByDlsiteFile(dlsiteId: String, path: String) {
        if (!isNeedLoadDLFile(dlsiteId)) {
            return
        }
        createVoice(dlsiteId).also {
            // 获取文件树json且保存
            val treeJson = this.readJsonByFileAndSave(it, path).toString()
            it.fileTree = treeJson
            this.voiceDao.save(it)
        }
    }

    /**
     * 读取dl上的voice数据创建voice
     */
    private fun createVoice(dlsiteId: String): Voice {
        val url = "https://www.dlsite.com/maniax/work/=/product_id/$dlsiteId.html/"
        val doc = Jsoup.connect(url)
            .proxy(resourceProperties.proxy.host, resourceProperties.proxy.port).get()
        val tagMaps = this.getTagsMap(doc)
        val title = this.getTitle(doc)
        val mainImg = this.getMainImg(doc)
        log.info("new voice id=$dlsiteId title=$title mainImg=$mainImg")
        val tags = tagMaps.map { (k, v) -> VoiceTag(k, v) }
        this.voiceTagDao.saveAll(tags)
        val voice = Voice(title, dlsiteId, mainImg, url, tags)
        voice.r18 = isR18(tagMaps)
        return voiceDao.save(voice)
    }

    /**
     * 递归把文件树转json Array
     * 这个过程中会创建相关的子media
     */
    private fun readJsonByFileAndSave(voice: Voice, startDir: String): ArrayNode {
        val dir = File(startDir)
        val array = objectMapper.createArrayNode()
        val files = dir.listFiles()
        files?.forEach { file ->
            if (file.isDirectory) {
                val node = array.addObject()
                node.putArray(file.name).addAll(readJsonByFileAndSave(voice, file.absolutePath))
            } else {
                // 处理需要处理的文件类型并且保存json
                val type = Media.getType(file.extension)
                if (type == null) {
                    log.warn("未处理 不支持的文件类型 ${file.name}")
                } else {
                    val media = VoiceMedia(voice, file.name, file.absolutePath, type, file.length())
                    voiceMediaDao.save(media)
                    array.addObject().apply {
                        put("name", media.filename)
                        put("id", media.id)
                        put("type", media.type.name)
                    }
                }
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
        tags?.forEach {
            //key&value 将dl上分隔符' / '转换为 单一空格做分隔
            val kv = it.text().replace(" / ", " ").trim().split(" ")
            val value = kv.subList(1, kv.size)
            tagsMap[kv.first()] = value
        }
        return tagsMap
    }

    /**
     * 检查文件是否需要读取
     * 目前实现为未存在该数据即进行读取
     */
    private fun isNeedLoadDLFile(dlsiteId: String): Boolean {
        return voiceDao.findByRjId(dlsiteId) == null
    }

}



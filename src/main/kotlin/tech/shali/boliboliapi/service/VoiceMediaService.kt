package tech.shali.boliboliapi.service

import it.sauronsoftware.jave.Encoder
import org.slf4j.Logger
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.shali.boliboliapi.config.Auth
import tech.shali.boliboliapi.config.checkAuth
import tech.shali.boliboliapi.dao.VoiceMediaDao
import tech.shali.boliboliapi.entity.Voice
import tech.shali.boliboliapi.entity.VoiceMedia
import tech.shali.boliboliapi.entity.base.Media
import tech.shali.boliboliapi.entity.base.MediaType
import tech.shali.boliboliapi.handler.ErrorType
import tech.shali.boliboliapi.pojo.exception.SystemException
import java.io.File


@Service
class VoiceMediaService(
    private val voiceMediaDao: VoiceMediaDao,
    private val log: Logger
) {
    /**
     * 根据id输出file
     */
    fun get(id: String, token: JwtAuthenticationToken): File {
        val media = voiceMediaDao.findById(id).or {
            throw SystemException("未找到媒体", ErrorType.NOT_FOUND)
        }.get()
        if (media.voice.r18) token.checkAuth(Auth.R18)
        return File(media.path)
    }


    /**
     * 根据文件建立medias
     */
    @Transactional
    fun createVoiceMedia(voice: Voice, startDir: String, parentFolder: String = "") {
        File(startDir).listFiles()?.forEach { file ->
            if (file.isDirectory) {
                val newParent = if (parentFolder.isEmpty()) file.name else "$parentFolder-${file.name}"
                createVoiceMedia(voice, file.absolutePath, newParent)
            } else {
                // 处理需要处理的文件类型并且保存json
                val type = Media.getType(file.extension)
                if (type == null) {
                    log.warn("未处理 不支持的文件类型 ${file.name}")
                    return@forEach
                }
                // 获取播放长度
                val trackLength =
                    if (type == MediaType.AUDIO || type == MediaType.VIDEO) this.countTrackLength(file) else null
                val media =
                    VoiceMedia(voice, file.name, file.absolutePath, type, file.length(), trackLength, parentFolder)
                voiceMediaDao.save(media)
            }
        }
    }

    /**
     * 计算音频文件的长度
     */
    private fun countTrackLength(file: File): Long? {
        return try {
            // 使用本地库ffmpeg进行长度测算
            Encoder().getInfo(file).duration
        } catch (e: Exception) {
            log.warn("${file.name} 读取播放长度失败: ${e.message}")
            null
        }
    }

    fun findByVoice(voice: Voice): List<VoiceMedia> {
        return voiceMediaDao.findByVoice(voice)
    }
}
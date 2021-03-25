package tech.shali.boliboliapi.service

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import tech.shali.boliboliapi.config.Auth
import tech.shali.boliboliapi.config.checkAuth
import tech.shali.boliboliapi.dao.VoiceMediaDao
import tech.shali.boliboliapi.handler.ErrorType
import tech.shali.boliboliapi.pojo.exception.SystemException
import java.io.File

@Service
class VoiceMediaService(private val voiceMediaDao: VoiceMediaDao) {
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
}
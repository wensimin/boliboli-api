package tech.shali.boliboliapi.dao

import org.springframework.data.jpa.repository.JpaRepository
import tech.shali.boliboliapi.entity.VoiceTag

interface VoiceTagDao : JpaRepository<VoiceTag, String>
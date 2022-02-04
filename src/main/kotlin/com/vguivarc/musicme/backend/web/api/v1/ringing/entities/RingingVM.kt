package com.vguivarc.musicme.backend.web.api.v1.ringing.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.models.ringing.Ringing
import com.vguivarc.musicme.backend.domain.models.ringing.RingingState
import com.vguivarc.musicme.backend.domain.models.song.Song
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.ZonedDateTime

@ApiModel(value = "JWT access token", description = "the jwt access token")
data class RingingVM(
    @JsonProperty("id")
    @ApiModelProperty(value = "the ringing id")
    val id: String? = null,
    @JsonProperty("song")
    @ApiModelProperty(value = "the ringing song")
    val song: Song? = null,
    @JsonProperty("dateSent")
    @ApiModelProperty(value = "the date when the ringing has been sent")
    val dateSent: ZonedDateTime? = null,
    @JsonProperty("listened")
    @ApiModelProperty(value = "is the ringing already listened")
    val listened: Boolean? = false,
    @JsonProperty("seen")
    @ApiModelProperty(value = "is the ringing already seen")
    val seen: Boolean? = false,
    @JsonProperty("receiverId")
    @ApiModelProperty(value = "the receiver id")
    val receiverId: String? = "",
    @JsonProperty("senderId")
    @ApiModelProperty(value = "the sender id")
    val senderId: String? = "",
    @JsonProperty("senderName")
    @ApiModelProperty(value = "the sender name")
    val senderName: String? = "",
    @JsonProperty("senderPictureUrl")
    @ApiModelProperty(value = "the sender picture url")
    val senderPictureUrl: String? = ""
) {
    companion object {
        fun fromRinging(ringing: Ringing, sender: Profile, receiver: Profile) = RingingVM(
            id = ringing.id,
            song = ringing.song,
            dateSent = ringing.createdDate,
            listened = (ringing.state == RingingState.LISTENED),
            seen = ringing.createdDate < receiver.dateLastSeen,
            receiverId = ringing.receiverId,
            senderId = ringing.senderId,
            senderName = sender.username,
            senderPictureUrl = sender.pictureUrl
        )
    }
}

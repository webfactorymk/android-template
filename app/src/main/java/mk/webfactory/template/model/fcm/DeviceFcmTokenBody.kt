package mk.webfactory.template.model.fcm

import com.google.gson.annotations.SerializedName

data class DeviceFcmTokenBody(

    //TODO: Rename to actual value

    @field:SerializedName("device_token") private val deviceToken: String
)
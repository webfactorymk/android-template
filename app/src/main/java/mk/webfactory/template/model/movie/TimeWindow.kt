package mk.webfactory.template.model.movie

import com.google.gson.annotations.SerializedName

enum class TimeWindow {
    @SerializedName("day") DAY,
    @SerializedName("week") WEEK,
}
package com.skyinu.classanalyze.model

import com.google.gson.annotations.SerializedName

class GraphNode {
    @SerializedName("id")
    var id: String = ""

    @SerializedName("label")
    var label: String = ""

    @SerializedName("color")
    var color = "#ff0"

    @SerializedName("size")
    var size = 0

    @SerializedName("x")
    var x = 0

    @SerializedName("y")
    var y = 0

    @SerializedName("debug_level")
    var level = 0

    @SerializedName("debug_angel")
    var angel = 0F

    @SerializedName("debug_seq")
    var seq = 0
}
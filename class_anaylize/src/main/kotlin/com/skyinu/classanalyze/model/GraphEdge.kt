package com.skyinu.classanalyze.model

import com.google.gson.annotations.SerializedName

class GraphEdge {
    @SerializedName("id")
    var id: String = ""

    @SerializedName("label")
    var label: String = ""

    @SerializedName("source")
    var source: String = ""

    @SerializedName("target")
    var target: String = ""

    @SerializedName("color")
    var color = "#f0f"
}
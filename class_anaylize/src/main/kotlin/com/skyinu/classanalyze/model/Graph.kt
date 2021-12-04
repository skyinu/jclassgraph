package com.skyinu.classanalyze.model

import com.google.gson.annotations.SerializedName

class Graph {
    @SerializedName("nodes")
    val nodes = arrayListOf<GraphNode>()

    @SerializedName("edges")
    val edges = arrayListOf<GraphEdge>()
}
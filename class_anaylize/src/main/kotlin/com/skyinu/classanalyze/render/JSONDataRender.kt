package com.skyinu.classanalyze.render

import com.google.gson.GsonBuilder
import com.skyinu.classanalyze.model.*
import com.skyinu.classanalyze.utils.FileUtil
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.*
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


class JSONDataRender(
    private val referenceHashMap: Map<String, ClassNode>,
    private val argsModel: ArgsModel
) {

    companion object {
        private const val FE_PROJECT_PATH = "fe"
        private const val FE_PROJECT_SCRIPT_PATH = "script"
        private const val OUT_JSON_NAME = "graph.js"
        private const val CENTER_X = 500F
        private const val CENTER_Y = 500F

        //https://uigradients.com/#Windy
        private val LINEAR_COLORS = arrayOf<Pair<String, String>>(
            Pair("#acb6e5", "#86fde8"),
            Pair("#f12711", "#f5af19"),
            Pair("#56ab2f", "#a8e063")
        )
    }

    private val graph = Graph()
    private val visitedHashMap = HashMap<String, Boolean>()
    private val visitQueue: Queue<VisitModel> = ArrayDeque<VisitModel>()
    fun render() {
        val rootNode = referenceHashMap[argsModel.rootClass]
        if (rootNode == null) {
            println("no class found here")
            return
        }
        visitQueue.offer(VisitModel(rootNode, 0, 0, 1))
        bfsVisit()
        extractFEResource()
        val graphJson = GsonBuilder().setPrettyPrinting().create().toJson(graph)
        val os = FileOutputStream(
            argsModel.outputDir.absolutePath +
                    File.separator + FE_PROJECT_PATH + File.separator +
                    FE_PROJECT_SCRIPT_PATH + File.separator + OUT_JSON_NAME
        )
        val bw = BufferedWriter(OutputStreamWriter(os))
        bw.write("let graphJson = $graphJson")
        bw.close()
    }

    private fun bfsVisit() {
        while (visitQueue.isNotEmpty()) {
            val visitModel = visitQueue.poll()
            val node = visitModel.node
            if (visitedHashMap.getOrDefault(node.nodeName(), false)) {
                continue
            }
            visitedHashMap[node.nodeName()] = true
            val graphNode = createGraphNode(node, visitModel.depth, visitModel.seq, visitModel.levelTotal)
            graph.nodes.add(graphNode)
            node.getOutClassList().forEachIndexed { index: Int, itemNode: ClassNode ->
                val graphEdge = createGraphEdge(node, itemNode, visitModel.depth, index, node.getOutClassList().size)
                graph.edges.add(graphEdge)
                visitQueue.offer(VisitModel(itemNode, visitModel.depth + 1, index, node.getOutClassList().size))
            }
        }
    }

    private fun createGraphNode(node: ClassNode, depth: Int, seq: Int, levelTotal: Int): GraphNode {
        val graphNode = GraphNode()
        graphNode.className = node.nodeName()
        graphNode.label = graphNode.className
        graphNode.size = depth + 1
        graphNode.level = depth
        if (depth != 0) {
            calculatePoint(graphNode, depth, 20, seq, levelTotal)
        } else {
            graphNode.x = CENTER_X.roundToInt()
            graphNode.y = CENTER_Y.roundToInt()
        }
        if (depth == 0) {
            graphNode.color = "#f00"
        }
        return graphNode
    }

    /**
     * x=a+r*cosθ,y=b+r*sinθ
     */
    private fun calculatePoint(graphNode: GraphNode, depth: Int, step: Int, seq: Int, levelTotal: Int) {
        val angel = seq * 1.0F / levelTotal * 360 * Math.PI / 180F
        graphNode.angel = seq * 1.0F / levelTotal * 360
        if (depth % 2 == 0) {
            graphNode.x = (CENTER_X + (depth * step) * cos(angel)).roundToInt()
            graphNode.y = (CENTER_X + (depth * step) * sin(angel)).roundToInt()
        } else {
            graphNode.y = (CENTER_X + (depth * step) * cos(angel)).roundToInt()
            graphNode.x = (CENTER_X + (depth * step) * sin(angel)).roundToInt()
        }
    }

    private fun createGraphEdge(parent: ClassNode, child: ClassNode, depth: Int, seq: Int, levelTotal: Int): GraphEdge {
        val graphEdge = GraphEdge()
        graphEdge.id = parent.nodeName() + child.nodeName()
        graphEdge.label = graphEdge.id
        graphEdge.source = parent.nodeName()
        graphEdge.target = child.nodeName()
        val colorPair = LINEAR_COLORS[depth % LINEAR_COLORS.size]
        graphEdge.color = getColor(colorPair.first, colorPair.second, seq * 1.0 / levelTotal)
        return graphEdge
    }

    private fun getColor(start: String, end: String, percentage: Double): String {
        var colorValue = ""
        val startColor = getRGB(start)
        val endColor = getRGB(end)
        colorValue = if (percentage < 100) {
            val range0 =
                (if (startColor[0] > endColor[0]) startColor[0] else endColor[0]) - if (startColor[0] < endColor[0]) startColor[0] else endColor[0]
            val range1 =
                (if (startColor[1] > endColor[1]) startColor[1] else endColor[1]) - if (startColor[1] < endColor[1]) startColor[1] else endColor[1]
            val range2 =
                (if (startColor[2] > endColor[2]) startColor[2] else endColor[2]) - if (startColor[2] < endColor[2]) startColor[2] else endColor[2]
            val r = (endColor[0] + Math.round(range0 * percentage / 100)).toInt()
            val g = (endColor[1] + Math.round(range1 * percentage / 100)).toInt()
            val b = (endColor[2] + Math.round(range2 * percentage / 100)).toInt()
            String.format("#%02x%02x%02x", r, g, b)
        } else {
            end
        }
        return colorValue
    }

    private fun getRGB(rgb: String): IntArray {
        val ret = IntArray(3)
        for (i in 0..2) {
            ret[i] = rgb.substring(i * 2 + 1, i * 2 + 2 + 1).toInt(16)
        }
        return ret
    }

    private fun extractFEResource() {
        FileUtil.loadRecourseFromJarByFolder(
            "/$FE_PROJECT_PATH",
            argsModel.outputDir.absolutePath,
            this.javaClass
        )
    }

    data class VisitModel(val node: ClassNode, val depth: Int, val seq: Int, val levelTotal: Int)

}
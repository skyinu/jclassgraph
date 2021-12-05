package com.skyinu.classanalyze.render

import com.google.gson.GsonBuilder
import com.skyinu.classanalyze.model.*
import com.skyinu.classanalyze.utils.FileUtil
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.*
import kotlin.collections.HashMap
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
        private const val IN_NODE_ID = "_in"
        private const val CENTER_X = 500F
        private const val CENTER_Y = 500F
        private const val MAX_EDGE_SIZE = 10F
        private const val MIN_EDGE_SIZE = 3F

        //https://uigradients.com/#Windy
        private val LINEAR_COLORS = arrayOf<Pair<String, String>>(
            Pair("#acb6e5", "#86fde8"),
            Pair("#f12711", "#f5af19"),
            Pair("#56ab2f", "#a8e063")
        )
    }

    private val graph = Graph()
    private val visitedHashMap = HashMap<String, Boolean>()
    private val levelNodeCount = HashMap<Int, Int>()
    private val levelNodeSeq = HashMap<Int, Int>()
    private val visitedNodeSeq = HashMap<String, Boolean>()
    private val visitQueue: Queue<VisitModel> = ArrayDeque<VisitModel>()
    fun render() {
        val rootNode = referenceHashMap[argsModel.rootClass]
        if (rootNode == null) {
            println("no class found here")
            return
        }

        bfsVisit(rootNode, true, true)
        bfsVisit(rootNode, true, false)
        bfsVisit(rootNode, false, true)
        bfsVisit(rootNode, false, false)
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

    private fun bfsVisit(rootNode: ClassNode, visitOut: Boolean, countMode: Boolean) {
        visitedHashMap.clear()
        visitQueue.clear()
        visitQueue.offer(VisitModel(rootNode, 0, 0))
        visitedNodeSeq.clear()
        if (countMode) {
            levelNodeSeq.clear()
            levelNodeCount.clear()
        }
        while (visitQueue.isNotEmpty()) {
            val visitModel = visitQueue.poll()
            val node = visitModel.node
            if (visitedHashMap.getOrDefault(node.nodeName(), false)) {
                continue
            }
            visitedHashMap[node.nodeName()] = true
            val levelCount = levelNodeCount.getOrDefault(visitModel.depth, 0)
            if (countMode) {
                levelNodeCount[visitModel.depth] = levelCount + 1
                levelNodeSeq[visitModel.depth] = levelCount + 1
            } else {
                val graphNode = createGraphNode(node, visitModel.depth, visitModel.seq, levelCount, visitOut)
                graph.nodes.add(graphNode)
            }
            val children = if (visitOut) {
                node.getOutClassList()
            } else {
                node.getInClassList()
            }
            innerVisit(node, visitModel, children, visitOut, countMode)
        }
    }

    private fun innerVisit(
        node: ClassNode,
        visitModel: VisitModel,
        children: List<ClassNode>,
        visitOut: Boolean,
        countMode: Boolean
    ) {
        val levelCount = levelNodeCount.getOrDefault(visitModel.depth, 0)
        children.forEachIndexed { index: Int, itemNode: ClassNode ->
            val nodeSeqAnchor = levelNodeSeq.getOrDefault(visitModel.depth, 0)
            val seq = levelCount - nodeSeqAnchor
            if (!countMode) {
                //avoid calculate duplicated
                if (!visitedNodeSeq.getOrDefault(itemNode.nodeName(), false)) {
                    levelNodeSeq[visitModel.depth] = nodeSeqAnchor - 1
                    visitedNodeSeq[itemNode.nodeName()] = true
                }
                val graphEdge =
                    createGraphEdge(node, itemNode, visitModel.depth, seq, levelCount, visitOut)
                graph.edges.add(graphEdge)
            }
            visitQueue.offer(VisitModel(itemNode, visitModel.depth + 1, seq))
        }
    }

    private fun createGraphNode(node: ClassNode, depth: Int, seq: Int, levelTotal: Int, visitOut: Boolean): GraphNode {
        val graphNode = GraphNode()
        graphNode.id = node.nodeName() + if (visitOut) {
            ""
        } else {
            IN_NODE_ID
        }
        graphNode.label = node.nodeName()
        graphNode.size = Math.abs(50 - depth * 10 * Math.random()).roundToInt()
        graphNode.level = depth
        graphNode.level_count = levelTotal
        graphNode.out = visitOut
        graphNode.seq = seq
        val step = if (visitOut) {
            25
        } else {
            45
        }
        val seqNumber = if (visitOut) {
            (seq + 1) % levelTotal
        } else {
            seq
        }
        if (depth != 0) {
            calculatePoint(graphNode, depth, step, seqNumber, levelTotal)
        } else {
            graphNode.x = CENTER_X.roundToInt()
            graphNode.y = CENTER_Y.roundToInt()
        }
        if (depth == 0) {
            if (visitOut) {
                graphNode.color = "#f00"
            } else {
                graphNode.color = "#00f"
            }
        } else {
            if (visitOut) {
                graphNode.color = "#f0f"
            } else {
                graphNode.color = "#0ff"
            }
        }
        return graphNode
    }

    /**
     * x=a+r*cosθ,y=b+r*sinθ
     */
    private fun calculatePoint(
        graphNode: GraphNode,
        depth: Int,
        step: Int,
        seq: Int,
        levelTotal: Int
    ) {
        val angel = seq * 1.0F / levelTotal * 360 * Math.PI / 180F
        graphNode.angel = seq * 1.0F / levelTotal * 360
        graphNode.cal_seq = seq
        if (depth % 2 == 0) {
            graphNode.x = (CENTER_X + (depth * step) * cos(angel)).roundToInt()
            graphNode.y = (CENTER_Y + (depth * step) * sin(angel)).roundToInt()
        } else {
            graphNode.y = (CENTER_Y + (depth * step) * cos(angel)).roundToInt()
            graphNode.x = (CENTER_X + (depth * step) * sin(angel)).roundToInt()
        }
    }

    private fun createGraphEdge(
        parent: ClassNode,
        child: ClassNode,
        depth: Int,
        seq: Int,
        levelTotal: Int,
        visitOut: Boolean
    ): GraphEdge {
        val graphEdge = GraphEdge()
        graphEdge.id = parent.nodeName() + child.nodeName()
        graphEdge.label = graphEdge.id
        val nodeSuffix = if (visitOut) {
            ""
        } else {
            IN_NODE_ID
        }
        if (visitOut) {
            graphEdge.source = parent.nodeName() + nodeSuffix
            graphEdge.target = child.nodeName() + nodeSuffix
        } else {
            graphEdge.source = child.nodeName() + nodeSuffix
            graphEdge.target = parent.nodeName() + nodeSuffix
        }
        graphEdge.size = MIN_EDGE_SIZE.coerceAtLeast(MAX_EDGE_SIZE - depth * 2)
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

}
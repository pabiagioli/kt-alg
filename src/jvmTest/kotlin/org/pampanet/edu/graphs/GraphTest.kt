package org.pampanet.edu.graphs

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*
import java.util.regex.Pattern
import org.pampanet.edu.scc.Graph
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


class GraphTest {

    @ExperimentalTime
    @Test
    fun testDFS(){
        val graph = Graph<Int>()
            .addEdge(1,3)
            .addEdge(1,5)
            .addEdge(3,5)
            .addEdge(5,8)
            .addEdge(5,9)
        println(graph)
        val resultMap = measureTimedValue { graph.dfsMap(1, linkedMapOf()) }
        val resultRec = measureTimedValue { graph.dfsTailRec(1) }
        val expected = measureTimedValue { listOf(1,3,5,8,9) }
        println("resultMap $resultMap resultRec $resultRec ")
        assert(resultRec.value == resultMap.value)
        assert(resultRec.value == expected.value)
    }

    @Test
    fun testTopoSort(){
        val graph = Graph<Int>()
            .addEdge(3,4)
            .addEdge(4,2)
            .addEdge(5,1)
            .addEdge(5,2)
            .addEdge(6,1)
            .addEdge(6,3)
        println(graph)
        val result = graph.topologicalSort()
        val expected = listOf(6,5,3,4,2,1)
        assert(result == expected)
    }

    @Test
    fun testTopoSort1(){
        val graph = Graph<Int>()
        .addEdge(5, 2)
        .addEdge(5, 0)
        .addEdge(4, 0)
        .addEdge(4, 1)
        .addEdge(2, 3)
        .addEdge(3, 1)
        println(graph)
        val result = graph.topologicalSort()
        val resultMap = graph.topologicalSortMap()
        val expected = listOf(5,4,2,3,1,0)
        assert(result == expected)
    }

    @Test
    fun testSCC(){
        val graph = Graph<Int>()
        .addEdge(1, 0)
        .addEdge(0, 2)
        .addEdge(2, 1)
        .addEdge(0, 3)
        .addEdge(3, 4)

        println(graph.sccRec().filterNot { it.isNullOrEmpty() })
    }

    @Test
    fun testSCC2(){
        println("graph read ...")
        val graph = loadGraph("scc/sccSample1.txt")
        val expected = listOf(434821,968,459,313,211)
        println("graph read successfully")
        println(graph.sccRec().map { it.size }.sortedDescending().take(5))
    }

    fun loadGraph(resourceFile: String): Graph<String> {
        var result = Graph<String>()
        ClassLoader.getSystemResourceAsStream(resourceFile)
            .use {
                val scanner = Scanner(it)
                while (scanner.hasNextBigInteger()) {
                    result = result.addEdge(scanner.nextBigInteger().toString(), scanner.nextBigInteger().toString())
                }
            }
        return result
    }

}
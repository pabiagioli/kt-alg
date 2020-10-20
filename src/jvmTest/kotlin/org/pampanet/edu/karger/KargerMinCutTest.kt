package org.pampanet.edu.karger

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.*
import java.util.regex.Pattern
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test Cases
 * https://github.com/beaunus/stanford-algs/tree/master/testCases/course1/assignment4MinCut
 */
class KargerMinCutTest {

    @ParameterizedTest
    @ValueSource(strings = ["karger/graphSample.txt"])
    fun testMinCuts(path: String){
        val graph = loadGraph(path)
        assert(graph.size == 25)
        val kargerMinCut = KargerMinCut()
        val minCut = kargerMinCut.kargerMinCut(graph)
        assertNotNull(minCut)
        assertEquals(minCut, 6)
    }

    fun loadGraph(resourceFile: String): Map<Int, List<Int>> {
        val result = mutableMapOf<Int, List<Int>>()
        ClassLoader.getSystemResourceAsStream(resourceFile)
            .use {
                val scanner = Scanner(it)
                while (scanner.hasNextLine()) {
                    val vertices = scanner.nextLine()
                        .split(Pattern.compile("\\s"))
                        .filterNot { num -> num == "" }
                        .map { num -> num.toInt() }
                    result[vertices[0]] = vertices.subList(1, vertices.size).toList()
                }
            }
        return result
    }
}
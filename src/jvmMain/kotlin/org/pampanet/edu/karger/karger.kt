package org.pampanet.edu.karger

class KargerMinCut {

    fun getRandomEdge(graph: Map<Int, List<Int>>): Pair<Int, Int> {
        val v1 = graph.keys.random()
        val v2 = graph[v1]?.random()
        return Pair(v1, v2!!)
    }

    fun mergeVertex(graph: Map<Int, List<Int>>, v1:Int, v2:Int): Map<Int, List<Int>> {
        //1. add all graph[v2] into graph[v1]
        val result = graph.toMutableMap()
        result[v1] = (graph[v1]!!) + (graph[v2]!!)
        //2. replace each ocurrence of v2 with v1
        graph[v2]?.forEach{i-> result[i] = result[i]?.map { if(it == v2) v1 else it } ?: emptyList()}
        //3. delete v2 entry in graph
        return result.filter { (k, _)-> k != v2 && !result[k].isNullOrEmpty()}
    }

    fun removeCycles(graph: Map<Int, List<Int>>, v1: Int, v2: Int): Map<Int, List<Int>> {
        val result = graph.toMutableMap()
        result[v1] = graph[v1]!!.filter { it != v1 }
        return result
    }


    fun kargerMinCut(graph: Map<Int, List<Int>>): Int? {
        val range = (0..graph.size)
        val result = range.flatMap { kargerContraction(graph)
            .map { (_,v)-> v.size }
            .toList() }.sorted().min()
        return result
    }

    tailrec fun kargerContraction(graph: Map<Int, List<Int>>): Map<Int, List<Int>> {
        return when {
            graph.size <= 2 -> {
                graph
            }
            else -> {
                val (v1,v2) = getRandomEdge(graph)
                kargerContraction(removeCycles(mergeVertex(graph, v1, v2),v1,v2))
            }
        }
    }

}


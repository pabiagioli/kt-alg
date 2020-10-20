package org.pampanet.edu.graphs


data class Node<T: Comparable<T>> (val name: T) {
    override fun equals(other: Any?): Boolean {
        return other?.equals(name) ?: false
    }
}

data class Graph<T: Comparable<T>> (val edges: MutableMap<T,MutableList<T>> = mutableMapOf()) {

    fun addEdge(v: T, w: T): Graph<T> {
        val e = edges

        e[v] = (edges[v]?: mutableListOf())
        e[v]!!.add(w)
        e[w] = e[w]?: mutableListOf()

        return this
    }

    fun reverse() : Graph<T> {
        val result = mutableMapOf<T,MutableList<T>>()
        edges.forEach { (k, v) ->
            v.forEach { node ->
                result[node] = (result[node]?: mutableListOf())
                result[node]!!.add(k)
            }
        }
        return Graph(result)
    }

    /*tailrec fun bfs(queue:List<Node<T>>, traversed: List<Node<T>>, layer: Int = 0): List<Node<T>> {
        val v = queue.firstOrNull()
        if(v == null){
            traversed.forEach { it.visited = false }
            return traversed
        }
        val q = edges[v]?.fold(queue.toMutableList()) {
                qtmp,w-> when {
                !w.visited -> {
                    w.visited = true
                    (qtmp + w).toMutableList()
                }
                else -> qtmp
            }
        }
        return bfs(q?:queue, traversed + v, layer + 1)
    }

    fun bfs(start: Node<T>): List<Node<T>> {
        start.visited = true
        return bfs(listOf(start), listOf(start))
    }

    tailrec fun shortestPath(start:Node<T>, end:Node<T>,
                     queue:List<Node<T>>,
                     distance: Int = 0, layer: Int = 0): Int {
        if(start == end)
            return distance
        val v = queue.firstOrNull()
        val q = edges[v]?.fold(
            Pair(queue.toMutableList(), distance)) {
                qtmp,w-> when {
                !w.visited -> {
                    w.visited = true
                    Pair((qtmp.first + w).toMutableList(), qtmp.second + 1)
                }
                else -> qtmp
            }
        }
        return shortestPath(start, end, q?.first?:queue,
            distance, layer + 1)
    }*/


    fun dfs(start: T, acc:MutableSet<T>): MutableSet<T> {
         val result = (edges[start]?: emptyList()).fold(acc){
            results, next ->
            when{
                results.contains(next) -> results
                else -> {
                    results.add(start)
                    dfs(next, results)
                }
            }
        }
        result.add(start)
        return result
    }

    fun dfsMap(start: T, acc:MutableMap<T, Boolean>): MutableMap<T, Boolean> {
        val result = (edges[start]?: emptyList()).fold(acc){
                results, next ->
            when{
                results[next] == true -> results
                else -> {
                    results[start] = true
                    dfsMap(next, results)
                }
            }
        }
        result[start] = true
        return result
    }

    fun dfsRec(start: T): List<T> {
        //tailrec fun helper(stack: List<T>, visited: MutableList<T>, acc: MutableList<T>) : List<T>{
        tailrec fun helper(stack: List<T>, visited: MutableMap<T,Boolean>, acc: MutableList<T>) : List<T>{
            return when{
                stack.isNullOrEmpty() -> acc
                else -> {
                    val head = stack.first()
                    val rest = stack.drop(1)
                    if(visited[head] == true){
                        helper(rest, visited, acc)
                    }else{
                        val expanded = edges[head]?: mutableListOf()
                        val unvisited = expanded.filterNot { visited[it] == true }.toMutableList()
                        unvisited.addAll(rest)
                        visited[head] = true
                        acc.add(head)
                        helper(unvisited, visited, acc)
                    }
                }
            }
        }
        return helper(listOf(start), mutableMapOf(), mutableListOf())
    }

    fun topologicalSort(): List<T> {
        //edges.keys.forEach { it.visited = false }
        return edges.keys.toList().sorted().fold(mutableMapOf<T,Boolean>()){
            tmp, w -> dfsMap(w, tmp)
        }.keys.toList()
    }

    fun sccRec(): MutableSet<MutableSet<T>> {
        val sortedComponents = reverse().topologicalSort().reversed()
        println("reverse().topologicalSort() finished with size: ${sortedComponents.size}")
        tailrec fun scchelper(sortedComponents:List<T>, index: Int, accum: MutableSet<MutableSet<T>>, visitedMap:MutableMap<T,Boolean> = mutableMapOf()): MutableSet<MutableSet<T>> {
            //print(".")
            if((index >= sortedComponents.size - 1) || sortedComponents[index] == null)
                return accum
            val next = sortedComponents[index]
            //val rest = sortedComponents.drop(1)
            val dfs = dfsMap(next, mutableMapOf())
            //print("dfsmap done ")
            //val filtered = dfs.filterNot { visitedMap[it] == true /*visited.contains(it)*/ }
            //print("filtered done ")
            visitedMap.putAll(dfs)
            //filtered.forEach { visitedMap[it] = true }
            //print("filtered forEach done ")
            val res = mutableSetOf(dfs.keys)
            res.addAll(accum)
            return scchelper(sortedComponents, index+1, res, visitedMap)
        }
        return scchelper(sortedComponents, 0, mutableSetOf(), mutableMapOf())
    }


    fun scc(): List<List<T>> {
        val sortedComponents = reverse().topologicalSort()
        println("reverse().topologicalSort() finished with size: ${sortedComponents.size}")
        //val visited = mutableSetOf<T>()
        val visitedMap = mutableMapOf<T,Boolean>()
        return sortedComponents.reversed().fold(mutableListOf()) { results,next ->
            if(visitedMap[next] == true)
                results
            else {
            print(".")
                val dfs = dfsMap(next, mutableMapOf()).keys
            print("dfsmap done ")
                val filtered = dfs.filterNot { visitedMap[it] == true /*visited.contains(it)*/ }
            print("filtering done")
                //println("node: $next dfs: $dfs visited: $visited")
                filtered.forEach { visitedMap[it] = true }
            print("filtering forEach done")
                //visited.addAll(dfs)
                //println("filtered: ${listOf(filtered) + results}")
                val res = mutableListOf(filtered)
                res.addAll(results)
                print(".")
                res
            }
        }
    }

}


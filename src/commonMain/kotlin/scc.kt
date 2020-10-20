package org.pampanet.edu.scc

operator fun <T> MutableList<T>.plus(other:MutableList<T>): MutableList<T> {
    this.addAll(other)
    return this
}

operator fun <T> LinkedHashSet<T>.plus(other:Collection<T>): LinkedHashSet<T> {
    this.addAll(other)
    return this
}

operator fun <T> LinkedHashSet<T>.plus(other:T): LinkedHashSet<T> {
    this.add(other)
    return this
}

operator fun <T, V> LinkedHashMap<T,V>.plus(other:Pair<T,V>): LinkedHashMap<T,V> {
    this[other.first] = other.second
    return this
}

operator fun <T, V> LinkedHashMap<T,V>.plus(other:LinkedHashMap<T,V>): LinkedHashMap<T,V> {
    this.putAll(other)
    return this
}

data class Node<T: Comparable<T>> (val name: T) {
    override fun equals(other: Any?): Boolean {
        return other?.equals(name) ?: false
    }
}

data class Graph<T: Comparable<T>> (val edges: MutableMap<T,MutableList<T>> = linkedMapOf()) {

    fun addEdge(v: T, w: T): Graph<T> {
        val e = edges

        e[v] = (edges[v]?: mutableListOf())
        e[v]!!.add(w)
        e[w] = e[w]?: mutableListOf()

        return this
    }

    fun reverse() : Graph<T> {
        val result = linkedMapOf<T,MutableList<T>>()
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


    fun dfsTailRec(start: T, visited:List<T> = emptyList(), reverse: Boolean = true): List<T>{
        tailrec fun helper(stack: LinkedHashSet<T>, visited: List<T>) : List<T> {
            return when {
                stack.isNullOrEmpty() -> visited
                else->{
                    val head = stack.first()
                    val tail = stack.drop(1)
                    val childrenNotSeen = edges[head]?.filterNotTo(linkedSetOf()) {
                        visited.contains(it)
                    }?: linkedSetOf()
                    helper(childrenNotSeen + tail, mutableListOf(head) + visited)
                }
            }
        }
        val result = helper(linkedSetOf(start), visited)
        return if(reverse) result.reversed() else result
    }


    //FIXME
    fun dfsMap(start: T, visited:LinkedHashMap<T, Boolean>): LinkedHashMap<T, Boolean> {
        tailrec fun helper(stack: LinkedHashSet<T>, visited: LinkedHashMap<T, Boolean>) : LinkedHashMap<T, Boolean> {
            return when {
                stack.isNullOrEmpty() -> visited
                else->{
                    val head = stack.first()
                    val tail = stack.drop(1)
                    val childrenNotSeen = edges[head]?.filterNotTo(linkedSetOf()) {
                        visited[it] == true
                    }?: linkedSetOf()
                    helper(childrenNotSeen+tail, linkedMapOf(Pair(head, true)) + visited)
                }
            }
        }
        return helper(linkedSetOf(start), visited)
    }

    fun topologicalSort(): List<T> {
        val result: LinkedHashSet<T> = edges.keys.sorted()
            .fold(linkedSetOf()) { tmp, w ->
                //println(tmp)
                tmp + (dfsTailRec(w, reverse = false))
            }
        return result.reversed()
    }

    fun topologicalSortMap(): List<T> {
        val result: LinkedHashMap<T,Boolean> = edges.keys.sorted()
            .fold(linkedMapOf()) { tmp, w ->
                //println(tmp)
                tmp + dfsMap(w, tmp)
            }
        return result.keys.reversed()
    }

    fun sccRec(): MutableSet<Map<T,Boolean>> {
        val gRev = reverse()
        println("graph reversed")
        val sortedComponents = gRev.topologicalSortMap()
        println("reverse().topologicalSort() finished")
        tailrec fun helper(
            sortedComponents: List<T>,
            accum: LinkedHashSet<Map<T, Boolean>>,
            visited: LinkedHashMap<T, Boolean> = linkedMapOf()
        ): MutableSet<Map<T,Boolean>> {
            if (sortedComponents.isNullOrEmpty())
                return accum
            val next = sortedComponents.first()
            val dfs = dfsMap(next, visited)
            val filtered = dfs.filterNot { visited[it.key] == true }
            return helper(sortedComponents.drop(1), accum + filtered, visited + dfs)
        }
        return helper(sortedComponents, linkedSetOf(), linkedMapOf())
    }


    /*fun scc(): List<List<T>> {
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
    }*/

}


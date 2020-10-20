import kotlinx.cinterop.*
import org.pampanet.edu.scc.Graph
import platform.posix.*
import kotlin.test.Test
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class GraphTests {

    @Test
    fun testGraphs(){
        try {
            println("load graph...")
            val graph = loadGraph(
                "/Users/pampanet/dev-folder/coursera/algorithms/src/nativeTest/resources/sccSample1.txt")
            println("graph loaded")
            println(graph.sccRec().map { it.size }.sortedDescending().take(5))
            //val result = mutableListOf(1) + mutableListOf(2)
        }catch (e: Exception){
            println(e)
        }
    }

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
        //val resultSet = measureTimedValue { graph.dfsSet(1, hashSetOf()) }
        val expected = measureTimedValue { listOf(1,3,5,8,9) }
        println("resultMap $resultMap resultRec $resultRec ")
        assert(resultRec.value == resultMap.value)
        assert(resultRec.value == expected.value)
    }

    fun loadGraph(path: String): Graph<String> = memScoped {
        val fp: CPointer<FILE>? = fopen(path, "r")
        val str = "".cstr.ptr
        fp?.let {fd->
            val graph = Graph<String>()
            while(fgets(str, 128, fd) != null){
                val (v,w) = str.toKStringFromUtf8()
                    .trim()
                    .split(" ")
                    //.map { it }
                graph.addEdge(v,w)
            }
            fclose(fd)
            return graph
        }
        error("Error reading file")
    }
}
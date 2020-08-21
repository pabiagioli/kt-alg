package org.pampanet.edu.mergesort

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigInteger
import java.util.*

class MergeSortTest {

    @ParameterizedTest
    @ValueSource(ints = [3])
    fun testInversionCountFixed(expectedCount: Int){
        val shuffled = listOf<Int>(3,1,4,2).map { it.toBigInteger() }
        //val shuffled = readList("mergesort/sampleList.txt")
        val mergeSort = MergeSort()
        val result = mergeSort.mergeSort(
            shuffled.toList())
        println(result)
        assert(result.second == expectedCount.toBigInteger())
    }

    @ParameterizedTest
    @ValueSource(ints = [954])
    fun testInversionCount(expectedCount: Int){
        //954
        val shuffled = readList("mergesort/sampleList.txt")
        val mergeSort = MergeSort()
        val result = mergeSort.mergeSort(
            shuffled.toList())
        println(result)
        assert(result.second == expectedCount.toBigInteger())
    }

    fun readList(path: String): Array<BigInteger>{
        val result = mutableListOf<BigInteger>()
        ClassLoader.getSystemResourceAsStream(path)
            .use {
                val scanner = Scanner(it)
                while(scanner.hasNextBigInteger()){
                    result.add(scanner.nextBigInteger())
                }
            }
        return result.toTypedArray()
    }
}
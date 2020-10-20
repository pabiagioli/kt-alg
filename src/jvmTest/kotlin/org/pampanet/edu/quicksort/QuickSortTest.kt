package org.pampanet.edu.quicksort

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigInteger
import java.util.*
import kotlin.test.assertEquals

/**
 * Test Cases
 * https://github.com/beaunus/stanford-algs/tree/master/testCases/course1/assignment3Quicksort
 */
class QuickSortTest{

    @ParameterizedTest
    @ValueSource(strings = ["first", "last", "median"])
    fun testQuickSortPivots(pivotType: String) {
        val pivotStrategy = QuickSort.PivotStrategy.valueOf(pivotType.toUpperCase())
        val shuffled = (0..100).shuffled()
            .map { it.toBigInteger() }
            .toTypedArray()
        val sorted = shuffled.sortedArray()
        val qsort = QuickSort()
        qsort.quickSort(shuffled,
            arrayOf(BigInteger.TWO),
            0,
            shuffled.size-1,
            pivotStrategy)
        assert(shuffled.contentEquals(sorted))
    }

    @ParameterizedTest
    @ValueSource(strings = ["first", "last", "median"])
    fun testQuickSortComparisons(pivotType: String) {
        val path = "quicksort/sampleList.txt"
        val pivotStrategy = QuickSort.PivotStrategy
            .valueOf(pivotType.toUpperCase())
        val shuffled = readList(path)
        val sorted = shuffled.sortedArray()
        val qsort = QuickSort()
        val comparisons = arrayOf(BigInteger.ZERO)
        qsort.quickSort(shuffled,
            comparisons,
            0,
            shuffled.size-1,
            pivotStrategy)
        assert(shuffled.contentEquals(sorted))

        when(pivotStrategy){
            QuickSort.PivotStrategy.FIRST->assertEquals(comparisons[0] , 21.toBigInteger())
            QuickSort.PivotStrategy.LAST->assertEquals(comparisons[0], 22.toBigInteger())
            QuickSort.PivotStrategy.MEDIAN->assertEquals(comparisons[0], 20.toBigInteger())
        }
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
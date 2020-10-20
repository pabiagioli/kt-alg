package org.pampanet.edu.quicksort

import java.math.BigInteger

class QuickSort {

    enum class PivotStrategy {
        FIRST,
        LAST, MEDIAN
    }

    private fun partition(list: Array<BigInteger>, pivot: BigInteger, ini: Int = 0, end: Int = list.size - 1): Int {
        var i = ini
        for (j in i + 1..end) {
            if (list[j] < pivot) {
                swap(list, ++i, j)
            }
        }
        swap(list, ini, i)
        return i
    }

    private fun choosePivot(list: Array<BigInteger>,
                            firstElementIndex: Int,
                            lastElementIndex: Int,
                            pivotStrategy: PivotStrategy
    ): BigInteger {
        when(pivotStrategy) {
            PivotStrategy.MEDIAN ->chooseMedianAsPivot(list, firstElementIndex, lastElementIndex)
            PivotStrategy.LAST ->swap(list, firstElementIndex, lastElementIndex)
        }
        return list[firstElementIndex]
    }

    private fun chooseMedianAsPivot(array: Array<BigInteger>, first: Int, last: Int) {
        val middle = (first + last) / 2

        if (array[middle] < array[first]) {
            if (array[middle] >= array[last])
                swap(array, first, middle)
            else if (array[last] < array[first])
                swap(array, first, last)
        } else if (array[middle] > array[first]) {
            if (array[middle] <= array[last])
                swap(array, first, middle)
            else if (array[last] > array[first])
                swap(array, first, last)
        }
    }

    private fun swap(list: Array<BigInteger>, a: Int, b: Int): Array<BigInteger> {
        val aElem = list[a]
        val bElem = list[b]
        list[a] = bElem
        list[b] = aElem
        return list
    }

    fun quickSort(list: Array<BigInteger>, accum: Array<BigInteger>, ini: Int, end: Int, pivotStrategy: PivotStrategy) {
        val n = end - ini + 1
        if (n <= 1) {
            return
        } else {
            val pivot = choosePivot(list, ini, end, pivotStrategy)
            val wallIndex = partition(list, pivot, ini, end)
            accum[0] += (end - ini).toBigInteger()
            quickSort(list, accum, ini, wallIndex - 1, pivotStrategy)
            quickSort(list, accum, wallIndex + 1, end, pivotStrategy)
        }
    }

}
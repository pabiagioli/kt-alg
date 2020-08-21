package org.pampanet.edu.mergesort

import java.math.BigInteger
import kotlin.math.ceil

class MergeSort {

    fun mergeSort(data: List<BigInteger>): Pair<List<BigInteger>,BigInteger> =
        if (data.size <= 1) Pair(data, BigInteger.ZERO)
        else data.chunked(
            ceil(data.size / 2.0).toInt()
        ).map{
            mergeSort(it)
        }.let {
            merge(it[0].first, it[1].first, emptyList(),
                it[0].second + it[1].second)
        }

    tailrec fun merge(l: List<BigInteger>, r: List<BigInteger>, res: List<BigInteger>, countSwaps: BigInteger): Pair<List<BigInteger>,BigInteger> = when {
        l.isEmpty() -> Pair(res + r, countSwaps)
        r.isEmpty() -> Pair(res + l, countSwaps)
        l.first() <= r.first() -> merge(l.drop(1), r, res + l.first(), countSwaps)
        else -> merge(l, r.drop(1), res + r.first(), countSwaps + l.size.toBigInteger())
    }

}
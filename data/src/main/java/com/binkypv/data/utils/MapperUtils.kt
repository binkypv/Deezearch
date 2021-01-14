package com.binkypv.data.utils

private const val DELIMITER = "="

object MapperUtils {
    fun getNextIndexFromUrl(next: String?) = next?.substringAfterLast(DELIMITER)?.toInt()
}
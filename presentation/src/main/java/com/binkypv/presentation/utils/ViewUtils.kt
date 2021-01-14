package com.binkypv.presentation.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.configurePaging(active: Boolean, loadFunction: () -> Unit) {
    if (active) {
        addOnScrollListener(object :
            ScrollPaginator(layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                loadFunction()
            }
        })
    } else {
        clearOnScrollListeners()
    }
}
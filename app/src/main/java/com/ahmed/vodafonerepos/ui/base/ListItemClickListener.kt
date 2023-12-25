package com.ahmed.vodafonerepos.ui.base

interface ListItemClickListener<in Item> {
    fun onItemClick(item: Item, position: Int)

}

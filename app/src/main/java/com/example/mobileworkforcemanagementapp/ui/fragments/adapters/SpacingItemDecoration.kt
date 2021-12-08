package com.example.mobileworkforcemanagementapp.ui.fragments.adapters

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecoration constructor(private val spacingVertical: Int = 0, private val spacingHorizontal: Int = 0): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position: Int = parent.getChildViewHolder(view).adapterPosition
        val itemCount = state.itemCount
        setSpacingForDirection(outRect, position, itemCount)
    }

    private fun setSpacingForDirection(
        outRect: Rect,
        position: Int,
        itemCount: Int
    ) {
        outRect.left = spacingHorizontal
        outRect.right = spacingHorizontal
        outRect.top = spacingVertical
        outRect.bottom = if (position == itemCount - 1) spacingVertical else 0
    }
}
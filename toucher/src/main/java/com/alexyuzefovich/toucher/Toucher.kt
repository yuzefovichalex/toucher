package com.alexyuzefovich.toucher

import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class Toucher {

    private var recyclerView: RecyclerView? = null

    private var gestureDetector: GestureDetector? = null

    private val gestureListener: GestureDetector.OnGestureListener =
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                recyclerView?.let {
                    val tappedView = it.findChildViewUnder(e.x, e.y) ?: return
                    val targetViewHolder = it.getChildViewHolder(tappedView) ?: return
                    Toast.makeText(it.context, targetViewHolder.adapterPosition.toString(), Toast.LENGTH_LONG).show()
                }
            }


        }

    private val onItemTouchListener: RecyclerView.OnItemTouchListener =
        object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                gestureDetector?.onTouchEvent(e)
            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                gestureDetector?.onTouchEvent(e)
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }
        }


    fun attachToRecyclerView(recyclerView: RecyclerView?) {
        this.recyclerView = recyclerView
        if (recyclerView != null) {
            gestureDetector = GestureDetector(recyclerView.context, gestureListener)
            recyclerView.addOnItemTouchListener(onItemTouchListener)
        }
    }

}
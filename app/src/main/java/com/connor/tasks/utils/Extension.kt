package com.connor.tasks.utils

import android.util.Log

private const val TAG = "TasksLog"

fun Any.logd(tab: String = TAG) {
    if (this is String) Log.d(tab, this) else Log.d(tab, this.toString())
}
package com.utsman.jokenorris.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.viewbinding.BuildConfig
import com.google.android.material.textfield.TextInputEditText
import com.utsman.jokenorris.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

typealias layoutRes = R.layout
typealias idRes = R.id
typealias styleRes = R.style

fun logD(msg: String?, tag: String = "JOKE_NORRIS") {
    debug {
        val maxLogSize = 1000
        val nullableMsg = msg ?: ""
        for (i in 0..nullableMsg.length / maxLogSize) {
            val start = i * maxLogSize
            var end = (i + 1) * maxLogSize
            end = if (end > nullableMsg.length) nullableMsg.length else end
            Log.d(tag, nullableMsg.substring(start, end))
        }
    }
}

inline fun debug(action: () -> Unit) {
    if (BuildConfig.DEBUG) {
        action.invoke()
    }
}

fun TextInputEditText.watcher(scope: CoroutineScope = MainScope(), result: (String) -> Unit) {
    val watcher = object : TextWatcher {
        private var searchFor = ""
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val searchText = s.toString().trim()
            if (searchText == searchFor)
                return

            searchFor = searchText

            scope.launch {
                delay(1000)
                if (searchText != searchFor)
                    return@launch

                if (searchText.length >= 2) {
                    result.invoke(searchText)
                }
            }
        }

        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    }

    addTextChangedListener(watcher)
}


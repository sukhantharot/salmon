package com.example.salmon.utils

import android.webkit.JavascriptInterface
import android.widget.TextView

class JavascriptFunction(private val textView: TextView) {

    private var messageFromWebView: String? = null

    @JavascriptInterface
    fun showMessageInNative(message: String) {
        // Received message from WebView in native
        messageFromWebView = message

        // Update the TextView with the received message
        updateTextView()
    }

    private fun updateTextView() {
        messageFromWebView?.let { message ->
            textView.text = message
        }
    }
}

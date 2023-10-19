package com.example.salmon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import com.example.salmon.utils.JavascriptFunction

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_about)

        val webView: WebView = findViewById(R.id.webView)
        val textView: TextView = findViewById(R.id.textView3)
        val webViewClient = WebViewClient()
        webView.webViewClient = webViewClient
        webView.settings.javaScriptEnabled = true

        val javascriptFunction = JavascriptFunction(textView)
        webView.addJavascriptInterface(javascriptFunction, "JavascriptFunction")

        webView.loadUrl("https://www.google.com")
    }
}


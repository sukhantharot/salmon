package com.example.salmon

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.salmon.utils.JavascriptFunction

class AboutActivity : AppCompatActivity() {
    lateinit var mEditText : EditText
    lateinit var mButtonSend : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_about)

        val webView: WebView = findViewById(R.id.webView)
        mEditText = findViewById(R.id.editInput)
        val webViewClient = WebViewClient()
        webView.webViewClient = webViewClient
        webView.settings.javaScriptEnabled = true
        webView.clearHistory();
        webView.clearFormData();
        webView.clearCache(true);
//        val javascriptFunction = JavascriptFunction(textView)
//        webView.addJavascriptInterface(javascriptFunction, "JavascriptFunction")
        webView.addJavascriptInterface(JSBridge(this,mEditText),"JSBridge")

        webView.loadUrl("https://sukhantharot.github.io/html-js/")
    }

    /**
     * Receive message from webview and pass on to native.
     */
    class JSBridge(val context: Context, val editTextInput: EditText){
        @JavascriptInterface
        fun showMessageInNative(message:String){
//            Toast.makeText(context,message, Toast.LENGTH_LONG).show()
            editTextInput.setText(message)
        }
    }

}


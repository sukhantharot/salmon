package com.example.salmon

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.salmon.activity.SelectBluetoothActivity
import com.example.salmon.databinding.ActivityMainBinding
import com.example.salmon.utils.Constant
import com.example.salmon.utils.UIUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import net.posprinter.POSConnect
import net.posprinter.POSConst
import net.posprinter.POSPrinter

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var pos = 2
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val mac = data.getStringExtra(SelectBluetoothActivity.INTENT_MAC)
                if (mac != null) {
                    binding.textView.text = mac
                }
            }
        }
    }
    private val printer = null
    private val REQUEST_BT_DEVICE = 1 // Define your request code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.Connect.setOnClickListener {
            connectBt()
        }
        binding.textView.setOnClickListener {
            launcher.launch(Intent(this, BluetoothActivity::class.java))
        }
        binding.printText.setOnClickListener {
            printText()
        }
        LiveEventBus.get<Boolean>(Constant.EVENT_CONNECT_STATUS).observeForever {
            binding.printText.isEnabled = it
        }
        binding.About.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        binding.btAcList.setOnClickListener {
            launcher.launch(Intent(this, ScanBlueToothActivity::class.java))
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun printText() {
        val printer = POSPrinter(App.get().curConnect)
        val str = "Welcome to the printer,this is print test content!\n"
        printer.printString(str)
            .printText("printText Demo\n", POSConst.ALIGNMENT_CENTER, POSConst.FNT_BOLD or POSConst.FNT_UNDERLINE, POSConst.TXT_1WIDTH or POSConst.TXT_2HEIGHT)
            .cutHalfAndFeed(1)
    }

    private fun connectBt() {
        val bleAddress = binding.textView.text.toString()
        if (bleAddress == getString(R.string.none_mac_address)) {
            UIUtils.toast(this, R.string.bt_select)
        } else {
            try {
                // Code that may throw exceptions
                App.get().connectBt(bleAddress)
            } catch (e: Exception) {
                // Log the error
                Log.e("MyApp", "An error occurred: " + e.message)
                e.printStackTrace() // Print the exception to logcat
                UIUtils.toast(this, "An error occurred: " + e.message)
            }
        }
    }

}
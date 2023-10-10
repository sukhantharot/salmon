package com.example.salmon

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.example.salmon.databinding.ActivityMainBinding
import net.posprinter.POSConnect
import net.posprinter.POSConst
import net.posprinter.POSPrinter

class MainActivity : AppCompatActivity() {
    private val printer = POSPrinter(App.get().curConnect)
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val entries = POSConnect.getSerialPort()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, entries)
        binding.serialPortNs.adapter = adapter
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        binding.fab.setOnClickListener {
            printText()
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun printText() {
        val str = "Welcome to the printer,this is print test content!\n"
        printer.printString(str)
            .printText("printText Demo\n", POSConst.ALIGNMENT_CENTER, POSConst.FNT_BOLD or POSConst.FNT_UNDERLINE, POSConst.TXT_1WIDTH or POSConst.TXT_2HEIGHT)
            .cutHalfAndFeed(1)
    }
}
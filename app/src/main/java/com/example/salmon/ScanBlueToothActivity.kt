package com.example.salmon

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.salmon.databinding.ActivityScanBlueToothBinding
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salmon.activity.BtAdapter
import com.example.salmon.utils.UIUtils
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class ScanBlueToothActivity : AppCompatActivity() {
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }
    private lateinit var binding: ActivityScanBlueToothBinding
    companion object {
        const val INTENT_MAC = "MAC"
    }
    data class BluetoothDeviceItem(
        val name: String,
        val address: String
    )
    private var btDevices: ArrayList<BtAdapter.Bean> = ArrayList()
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                searchDevices()
            } else {
                UIUtils.toast(this, R.string.request_permission_fail)
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_blue_tooth)
        binding = ActivityScanBlueToothBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btList.layoutManager = LinearLayoutManager(this)
        val adapter = BtAdapter(btDevices)
        adapter.itemClick = {
            val intent = Intent()
            intent.putExtra(INTENT_MAC, btDevices[it].mac)
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.btList.adapter = adapter

        if (bluetoothAdapter == null) {
            binding.btStatus.text = getString(R.string.bluetooth_is_not_available)
        } else {
            binding.btStatus.text = getString(R.string.bluetooth_is_available)
        }

        if (bluetoothAdapter.isEnabled) {
            binding.btEnable.text = getString(R.string.on)
        } else {
            binding.btEnable.text = getString(R.string.off)
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {

            val devices = bluetoothAdapter.bondedDevices ?: emptyList()
            devices.forEach {
                val name = it.name ?: "none"
                btDevices.add(BtAdapter.Bean(true, name, it.address))
            }
            binding.btList.adapter?.notifyDataSetChanged()
        }

        binding.refreshBt.setOnClickListener {
            setBluetooth()
        }
    }
    private var lastTime = 0L
    private fun setBluetooth() {
        if (System.currentTimeMillis() - lastTime < 1000) {
            return
        }
        lastTime = System.currentTimeMillis()
        if (!bluetoothAdapter.isEnabled) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            launcher.launch(intent)
        } else {
            searchDevices()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun searchDevices() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
            btDevices.clear()
            val device: Collection<BluetoothDevice> = bluetoothAdapter.bondedDevices ?: emptyList()
            device.forEach {
                val name = it.name ?: "none"
                btDevices.add(BtAdapter.Bean(true, name, it.address))
            }
            binding.btList.adapter?.notifyDataSetChanged()
            if (bluetoothAdapter.isDiscovering) {
                bluetoothAdapter.cancelDiscovery()
            }
            GlobalScope.launch {
                delay(300) // Delay for 300 milliseconds
                // This code will be executed after the delay
                bluetoothAdapter.startDiscovery()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (bluetoothAdapter.isDiscovering) {
                bluetoothAdapter.cancelDiscovery()
            }
            return
        }
    }
}
package com.example.salmon

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salmon.activity.BtAdapter
import com.example.salmon.activity.SelectBluetoothActivity
import com.example.salmon.databinding.ActivityBluetoothBinding
import com.example.salmon.utils.UIUtils

class BluetoothActivity : AppCompatActivity() {
    private lateinit var bind: ActivityBluetoothBinding
    private val BLUETOOTH_PERMISSION_REQUEST_CODE = 1
    private val datas = ArrayList<BtAdapter.Bean>()
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                checkBluetoothPermissions()
            } else {
                UIUtils.toast(this, R.string.request_permission_fail)
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)
        // Call this method to check Bluetooth permissions when the activity starts
        checkBluetoothPermissions()
//        bind.btList.layoutManager = LinearLayoutManager(this)
//        val adapter = BtAdapter(datas)
//        adapter.itemClick = {
//            val intent = Intent()
//            intent.putExtra(SelectBluetoothActivity.INTENT_MAC, datas[it].mac)
//            setResult(RESULT_OK, intent)
//            finish()
//        }
//        bind.btList.adapter = adapter

//        val intentFilter = IntentFilter()
//        intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
//        intentFilter.addAction(GPS_ACTION)
//        registerReceiver(mBroadcastReceiver, intentFilter)
    }
    // Check if Bluetooth permissions are granted when your activity starts
    private fun checkBluetoothPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH), BLUETOOTH_PERMISSION_REQUEST_CODE)
        } else {
            // Permission is already granted
            // You can proceed with Bluetooth operations
        }
    }

    // Handle permission request results
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                // You can proceed with Bluetooth operations
            } else {
                // Permission denied
                // Handle the case where the user denies Bluetooth permissions
            }
        }
    }

    // Call checkBluetoothPermissions when you need to request Bluetooth permissions
}
package com.a2m.loadconfig

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Telephony
import android.telephony.CarrierConfigManager
import android.telephony.CarrierConfigManager.KEY_APN_SETTINGS_DEFAULT_APN_TYPES_STRING_ARRAY
import android.telephony.TelephonyManager
import android.telephony.data.ApnSetting
import android.telephony.ims.ImsManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.a2m.loadconfig.ui.theme.LoadConfigTheme

class MainActivity : ComponentActivity() {
    companion object {
        private const val PERMISSION_CODE = 100
    }
    private val configText = mutableStateOf("config not loaded")
    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>;
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ANTONIO","----------------------------- oncreate");

        setContent {
            LoadConfigTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                        draw()
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun draw() {
        applicationContext
        Column(modifier = Modifier.padding(20.dp)) {
            Button(onClick = {
                checkPermission(Manifest.permission.READ_PHONE_STATE, PERMISSION_CODE)
                }) {
                Text("LoadConfig")
            }
        }
        Row( modifier = Modifier.padding(25.dp,100.dp)){
            val myText by configText
            Text(myText)
        }
    }

    @SuppressLint("MissingPermission", "WrongConstant")
    @RequiresApi(Build.VERSION_CODES.P)
    fun readConfig():String {
        Log.d("ANTONIO","----------------------------- read config"+System.currentTimeMillis())
        var txt:String="nothing read "+System.currentTimeMillis()
        var ccm =  application.getSystemService(Context.CARRIER_CONFIG_SERVICE) as CarrierConfigManager
        Log.d("ANTONIO","----------------------------- ccm obtained"+System.currentTimeMillis())
        var deb=ccm.config?.getBoolean(CarrierConfigManager.KEY_ALLOW_ADDING_APNS_BOOL)
        Log.d("ANTONIO", "---- KEY_ALLOW_ADDING_APNS_BOOL : $deb ;----------------");
        deb=ccm.config?.getBoolean(CarrierConfigManager.KEY_APN_EXPAND_BOOL)
        Log.d("ANTONIO", "---- KEY_APN_EXPAND_BOOL : $deb ;----------------");

        var deb3=ccm.config?.getString(CarrierConfigManager.KEY_CARRIER_CONFIG_VERSION_STRING)
        Log.d("ANTONIO", "---- KEY_CARRIER_CONFIG_VERSION_STRING : $deb3 ;----------------");
        deb=ccm.config?.getBoolean(CarrierConfigManager.KEY_CARRIER_DEFAULT_WFC_IMS_ENABLED_BOOL)
        Log.d("ANTONIO", "---- KEY_CARRIER_DEFAULT_WFC_IMS_ENABLED_BOOL : $deb ;----------------");

        //var ise = application.getSystemService(Context.)
        //ccm.config?.getPersistableBundle(CarrierConfigManager)

        return txt
    }

    // Function to check and request permission.
    @RequiresApi(Build.VERSION_CODES.P)
    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@MainActivity, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
        } else {
            Toast.makeText(this@MainActivity, "Permission already granted", Toast.LENGTH_SHORT).show()
            configText.value = readConfig()

        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configText.value = readConfig()
                Toast.makeText(this@MainActivity, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}




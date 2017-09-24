package com.tomtase.hotspotlogger

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.content.IntentFilter
import android.net.wifi.WifiManager
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by tsep on 23.9.2017.
 */
class WiFiService : Service(), IWiFiConnectionObserver {
    override fun wifiConnectionChanged(name: String) {
        writeToFile(name)

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        registerReceiver(WiFiBroadcastReceiver(this), intentFilter)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_REDELIVER_INTENT
    }

    private fun writeToFile(networkSsid: String) {
        val filename = "times.txt"
        val time = SimpleDateFormat("HH:mm").format(Date())
        val entry = "$time $networkSsid \n"

        try {
            val outputStream = openFileOutput(filename, Context.MODE_APPEND or Context.MODE_PRIVATE)
            outputStream.write(entry.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
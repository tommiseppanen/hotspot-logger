package com.tomtase.hotspotlogger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.net.NetworkInfo

private const val NO_NETWORK_STRING = ""

class WiFiBroadcastReceiver(val observer: IWiFiConnectionObserver) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == WifiManager.NETWORK_STATE_CHANGED_ACTION) {
            val manager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val networkInfo = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
            val state = networkInfo.state

            if (state == NetworkInfo.State.CONNECTED) {
                val connectingToSsid = manager.connectionInfo.ssid.replace("\"", "")
                observer.wifiConnectionChanged(connectingToSsid)
            }

            if (state == NetworkInfo.State.DISCONNECTED) {
                if (manager.isWifiEnabled) {
                    observer.wifiConnectionChanged(NO_NETWORK_STRING)
                }
            }
        }
    }
}
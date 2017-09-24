package com.tomtase.hotspotlogger

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.net.wifi.WifiManager
import android.content.IntentFilter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*
import android.content.Context.MODE_PRIVATE
import java.io.BufferedReader
import java.io.InputStreamReader
import android.content.Intent
import java.io.FileNotFoundException


class TimeListActivity : AppCompatActivity(), IWiFiConnectionObserver {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_list)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        readFile()

        val intent = Intent(baseContext, WiFiService::class.java)
        startService(intent)

        //val intentFilter = IntentFilter()
        //intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        //registerReceiver(WiFiBroadcastReceiver(this), intentFilter)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_time_list, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_refresh) {
            readFile()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun wifiConnectionChanged(name: String) {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        if (name.isEmpty())
            toolbar.title = getString(R.string.app_name)
        else
            toolbar.title = name

        val textview = findViewById(R.id.textview) as TextView
        val time = SimpleDateFormat("HH:mm").format(Date())
        textview.text = "$time $name \n${textview.text}"
    }

    private fun readFile() {
        try {
            val fis = openFileInput("times.txt")
            val isr = InputStreamReader(fis)
            val bufferedReader = BufferedReader(isr)
            val sb = StringBuilder()
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                sb.insert(0, "$line${System.getProperty("line.separator")}")
                line = bufferedReader.readLine()
            }
            val textview = findViewById(R.id.textview) as TextView
            textview.text = sb.toString()
        } catch (e: FileNotFoundException) {
            //no need to worry
        }

    }
}

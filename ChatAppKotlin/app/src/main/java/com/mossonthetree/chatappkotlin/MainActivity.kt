package com.mossonthetree.chatappkotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    val userNameReqCode = 1001

    lateinit var udpCon: UdpClient

    lateinit var receiveThread: Thread

    lateinit var prefs: SharedPreferences

    var clientName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        udpCon = UdpClient("192.168.1.100", 8081);

        receiveThread = Thread(Runnable {
            while(!udpCon.inError) {
                val appMessage = udpCon.receivedData()
                val clientMessage:ClientMessage = appMessage.data as ClientMessage
                runOnUiThread(Runnable {
                    when (appMessage.type) {
                        1 -> Toast.makeText(this, clientMessage.msg, Toast.LENGTH_LONG).show()
                    }
                })
            }
            println("mossonthetreeapp Ending receiving")
        })
        receiveThread.start()

        findViewById<Button>(R.id.btnSend).setOnClickListener {v ->
            Thread(Runnable {
                println("mossonthetreeapp Sending...")
                val recipient = findViewById<EditText>(R.id.edtName).text.toString()
                val msg = findViewById<EditText>(R.id.edtMsg).text.toString()
                if(msg.isNotEmpty()) {
                    val clientMessage = ClientMessage(recipient, msg)
                    udpCon.sendData(AppMessage(1, clientMessage))
                }
            }).start()
        }

        prefs = getSharedPreferences("chat", 0)
        clientName = prefs.getString("user_name", "")
        if(clientName.isNullOrEmpty()) {
            startActivityForResult(Intent(this, UserName::class.java), userNameReqCode)
        } else {
            connect(clientName!!)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == userNameReqCode && resultCode == Activity.RESULT_OK) {
            clientName = data?.getStringExtra("user_name")
            connect(clientName!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val thread = Thread(Runnable {
            udpCon.sendData(AppMessage(3, clientName))
            udpCon.close()
        })
        thread.start()
        thread.join()
    }

    private fun connect(userName: String): Unit {
        Thread(Runnable {
            udpCon.sendData(AppMessage(2, userName))
        }).start()
    }
}

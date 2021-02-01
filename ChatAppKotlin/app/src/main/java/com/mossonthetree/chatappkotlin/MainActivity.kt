package com.mossonthetree.chatappkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var udpCon: UdpClient

    lateinit var receiveThread: Thread

    var clientName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        udpCon = UdpClient("192.168.1.100", 8081);
        if(!udpCon.inError) {
            Toast.makeText(this, "Udp socket open", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show()
        }

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
        })

        receiveThread.start()

        findViewById<Button>(R.id.btnConnect).setOnClickListener{ v ->
            Thread(Runnable {
                if(clientName == null) {
                    clientName = findViewById<EditText>(R.id.edtName).text.toString()
                    udpCon.sendData(AppMessage(2, clientName))
                }
            }).start()
        }

        findViewById<Button>(R.id.btnSend).setOnClickListener { v ->
            Thread(Runnable {
                val recipient = findViewById<EditText>(R.id.edtName).text.toString()
                val msg = findViewById<EditText>(R.id.edtMsg).text.toString()
                if(msg.isNotEmpty()) {
                    val clientMessage = ClientMessage(recipient, msg)
                    udpCon.sendData(AppMessage(1, clientMessage))
                }
            }).start()
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
}

package com.mong.whenwillwemeet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import java.util.*

class MainActivity : AppCompatActivity() {
    var inpName = "";
    var personalID = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText : EditText = findViewById(R.id.main_et)
        val loginBtn : Button = findViewById(R.id.main_loginBtn)
        val _googleBtn : Button = findViewById(R.id.main_googleBtn)
        val _kakaoBtn : Button = findViewById(R.id.main_kakaoBtn)

        loginBtn.setOnClickListener {
            inpName = editText.text.toString()

            if(!inpName.equals("") && inpName != null) {
                personalID = createID()
                finish()
            }
        }
    }

    public final fun createID() : String{
        val alphabet : Array<String> = arrayOf("A","B","C","D","E"
            ,"F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")

        val random = Random()
        var result : String = ""
        var tmp : Int

        for(i in 0..9) {
            tmp = random.nextInt(26)
            result += alphabet[tmp]
        }

        return result
    }
}

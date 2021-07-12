package com.mong.whenwillwemeet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import java.util.*

class MainActivity : AppCompatActivity() {

    private val _editText : EditText = findViewById(R.id.main_et)
    private val _loginBtn : Button = findViewById(R.id.main_loginBtn)
    private val _googleBtn : Button = findViewById(R.id.main_googleBtn)
    private val _kakaoBtn : Button = findViewById(R.id.main_kakaoBtn)

    var inpName = "";
    var personalID = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _loginBtn.setOnClickListener {
            inpName = _editText.text.toString()

            if(!inpName.equals("")) {
                personalID = createID()
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

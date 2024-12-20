package com.scwang.wave.app.activity

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.scwang.wave.MultiWaveHeader
import com.scwang.wave.app.R
import com.scwang.wave.app.fragment.WaveConsoleFragment
import com.scwang.wave.app.util.StatusBarUtil
//import kotlinx.android.synthetic.main.activity_user_login.*


class UserLoginActivity : AppCompatActivity() {
    private val mVerifyMode by lazy { findViewById<TextView>(R.id.verify_mode) }
    private val mTouristMode by lazy { findViewById<TextView>(R.id.tourist_mode) }
    private val mRegister by lazy { findViewById<TextView>(R.id.register) }
    private val mLogin by lazy { findViewById<TextView>(R.id.login) }
    private val mForget by lazy { findViewById<TextView>(R.id.forget) }
    private val mDelete by lazy { findViewById<ImageView>(R.id.delete) }
    private val mPassword by lazy { findViewById<EditText>(R.id.password) }
    private val mUsername by lazy { findViewById<EditText>(R.id.username) }
    private val mLogo by lazy { findViewById<ImageView>(R.id.logo) }
    private val mWaveHeader by lazy { findViewById<MultiWaveHeader>(R.id.waveHeader) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)

        mLogin.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
            FragmentActivity.start(this, WaveConsoleFragment::class.java)
        }

    }
}

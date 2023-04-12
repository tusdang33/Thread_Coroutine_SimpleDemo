package com.kaizm.learnthread

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kaizm.learnthread.databinding.ActivityMainBinding

const val TAG = "AAA"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.thread, ThreadFragment())
            .addToBackStack(null).commit()
        supportFragmentManager.beginTransaction().replace(R.id.coroutine, CoroutineFragment())
            .addToBackStack(null).commit()


    }
}
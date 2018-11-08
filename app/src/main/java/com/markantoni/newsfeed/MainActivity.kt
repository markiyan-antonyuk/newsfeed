package com.markantoni.newsfeed

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import com.markantoni.newsfeed.ui.fragments.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.findFragmentById(R.id.fragmentContainer) ?: run {
            supportFragmentManager.transaction {
                replace(R.id.fragmentContainer, HomeFragment())
            }
        }
    }
}
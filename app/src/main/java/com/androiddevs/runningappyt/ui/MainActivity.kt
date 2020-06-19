package com.androiddevs.runningappyt.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.db.RunDAO
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var runDAO: RunDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}

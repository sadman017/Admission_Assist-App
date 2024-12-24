package com.souravpalitrana.androiduicomponents.viewmodel

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.souravpalitrana.androiduicomponents.R

class TennisMatch : AppCompatActivity() {
    private lateinit var tvTeam1: TextView
    private lateinit var tvTeam2: TextView
    private var teamS1 = 0
    private var teamS2 = 0
    private var w1 =0
    private var w2 =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tennis_match)
        tvTeam1 = findViewById(R.id.tvScore1)
        tvTeam2 = findViewById(R.id.tvScore2)
        savedInstanceState?.let {
            teamS1 = it.getInt("teamOnescore")
            teamS2 = it.getInt("teamTwoscore")
            tvTeam1.text = teamS1.toString()
            tvTeam2.text = teamS2.toString()
        }
        findViewById<Button>(R.id.add1).setOnClickListener {
            teamS1++
            tvTeam1.text = teamS1.toString()
            checkWInner()
        }
        findViewById<Button>(R.id.add2).setOnClickListener{
            teamS2++
            tvTeam2.text = teamS2.toString()
            checkWInner()
        }
    }
    private fun checkWInner(){
        if(teamS1>=5){
            w1++
            if(w1>=2) {
                Toast.makeText(this, "Final Winner: Federer", Toast.LENGTH_LONG).show()
                w1 = 0
                w2 = 0
                return
            }
            Toast.makeText(this, "Federer is the Winner.", Toast.LENGTH_LONG).show()
            teamS1=0
            teamS2=0
        }else if (teamS2>=5){
            w2++
            if(w2>=2){
                Toast.makeText(this, "Final Winner: Nadal", Toast.LENGTH_LONG).show()
                w1 = 0
                w2 = 0
                return
            }
            Toast.makeText(this, "Nadal is the Winner.", Toast.LENGTH_LONG).show()
            teamS1=0
            teamS2=0
        }
        tvTeam1.text = teamS1.toString()
        tvTeam2.text = teamS2.toString()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("teamOnescore",teamS1)
        outState.putInt("teamTwoscore",teamS2)
    }

}
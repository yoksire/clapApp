package com.example.clapapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private  var mediaPlayer:MediaPlayer?=null//we make it nullable to restart the player when music stops
    private lateinit var seekBar:SeekBar
    private lateinit var runnable: Runnable
    private lateinit var handler:Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBar=findViewById(R.id.sbclapping)
        handler=Handler(Looper.getMainLooper())

        val play=findViewById<FloatingActionButton>(R.id.fabPlay)
        play.setOnClickListener {
            if(mediaPlayer==null){
                mediaPlayer=MediaPlayer.create(this,R.raw.appa)
                intializeSeekBar()
            }
            mediaPlayer?.start()
        }

        val pause=findViewById<FloatingActionButton>(R.id.fabPause)
        pause.setOnClickListener {
            mediaPlayer?.pause()
        }

        val stop=findViewById<FloatingActionButton>(R.id.fabStop)
        stop.setOnClickListener {
            mediaPlayer?.stop()// after stoping the media player
            mediaPlayer?.reset()//we release to the player to avoid unnecessary consumption
            mediaPlayer?.release()
            mediaPlayer=null
            handler.removeCallbacks(runnable)
            seekBar.progress=0
        }

    }
    private fun intializeSeekBar(){
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

        })
        val tvPlayed=findViewById<TextView>(R.id.tvPlay)
        val tvDue=findViewById<TextView>(R.id.tvDue)
        seekBar.max =mediaPlayer!!.duration //seekbar length to media player duration

        runnable= Runnable {
            seekBar.progress=mediaPlayer!!.currentPosition //seekbar pointer to media's current position
            val playedTime=mediaPlayer!!.currentPosition/1000
            tvPlayed.text="$playedTime secs"
            val duration =mediaPlayer!!.duration/1000
            val dueTime=duration-playedTime
            tvDue.text="$dueTime secs"
            handler.postDelayed(runnable,1000)


        }
        handler.postDelayed(runnable,1000)
    }
}



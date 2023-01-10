package mateusz.oleksik.smb_5

import android.app.job.JobParameters
import android.app.job.JobService
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.widget.Toast
import java.io.IOException

class MusicPlayerService : JobService() {
    private var cancel = false
    private val songs = arrayOf(
        Uri.parse("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"),
        Uri.parse("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"),
        Uri.parse("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3"),
        Uri.parse("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3")
    )

    companion object{
        var mediaPlayer: MediaPlayer? = null
        var isInitialized: Boolean = false
        var currentTrackIndex: Int = 0
    }

    override fun onStartJob(parameters: JobParameters?): Boolean {
        var extras = parameters!!.extras
        var actionFlag = extras.getString(Constants.JOB_TYPE_BUNDLE_KEY, Constants.MUSIC_JOB_PLAY_OR_PAUSE_SONG_FLAG)

        if (!isInitialized){
            mediaPlayerInitialization()
        }

        when (actionFlag){
            Constants.MUSIC_JOB_PLAY_OR_PAUSE_SONG_FLAG -> playOrPauseMusic()
            Constants.MUSIC_JOB_NEXT_SONG_FLAG -> nextSong()
            Constants.MUSIC_JOB_PREVIOUS_SONG_FLAG -> previousSong()
        }

        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        cancel = true
        return true
    }

    private fun playOrPauseMusic(){
        if (mediaPlayer?.isPlaying == true){
            mediaPlayer?.pause()
        }else{
            mediaPlayer?.start()
        }
    }

    private fun nextSong(){
        var songIndex = currentTrackIndex + 1
        if (songIndex > songs.size - 1){
            songIndex = 0
        }

        mediaPlayer?.reset()
        try{
            mediaPlayer?.setDataSource(applicationContext, songs[songIndex])
        } catch(e: IOException){
            e.printStackTrace()
        }

        mediaPlayer?.prepare()
        mediaPlayer?.start()

        Toast.makeText(applicationContext, "Currently playing: ${songs[songIndex].pathSegments.last()}", Toast.LENGTH_LONG).show()
        Log.i("MUSIC_PLAYER","NEXT SONG ACTION: Currently playing: ${songs[songIndex].pathSegments.last()}")
        currentTrackIndex = songIndex
    }

    private fun previousSong(){
        var songIndex = currentTrackIndex - 1
        if (songIndex < 0){
            songIndex = songs.lastIndex
        }

        mediaPlayer?.reset()
        try{
            mediaPlayer?.setDataSource(applicationContext, songs[songIndex])
        } catch(e: IOException){
            e.printStackTrace()
        }

        mediaPlayer?.prepare()
        mediaPlayer?.start()

        Toast.makeText(applicationContext, "Currently playing: ${songs[songIndex].pathSegments.last()}", Toast.LENGTH_LONG).show()
        Log.i("MUSIC_PLAYER","PREVIOUS SONG ACTION: Currently playing: ${songs[songIndex].pathSegments.last()}")
        currentTrackIndex = songIndex
    }

    private fun mediaPlayerInitialization(){

        val mp = MediaPlayer()
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        mp.setAudioAttributes(audioAttributes)

        try{
            mp.setDataSource(applicationContext, songs.first())
        } catch(e: IOException){
            e.printStackTrace()
        }

        mp.prepare()
        isInitialized = true
        mediaPlayer = mp
    }
}
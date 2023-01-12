package mateusz.oleksik.smb_5

import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PersistableBundle
import android.widget.RemoteViews
import android.widget.Toast

class CustomWidget : AppWidgetProvider() {

    private var requestCode = 0

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, requestCode++)
        }
    }

    override fun onEnabled(context: Context) {
        Toast.makeText(context, "On enabled" , Toast.LENGTH_LONG).show()
    }

    override fun onDisabled(context: Context) {}

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        //WEBPAGE
        if(intent?.action.equals(Constants.WEBPAGE_ACTION)){
            Toast.makeText(context, "Performing action: ${Constants.WEBPAGE_ACTION}", Toast.LENGTH_SHORT).show()

            val intent2 = Intent(Intent.ACTION_VIEW, Uri.parse("https://pja.edu.pl/"))
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            //intent2.`package` = "com.android.chrome"
            context?.startActivity(intent2)
        }

        //MUSIC
        if(intent?.action.equals(Constants.PLAY_MUSIC_ACTION)){
            Toast.makeText(context, "Performing action: ${Constants.PLAY_MUSIC_ACTION}", Toast.LENGTH_SHORT).show()
            scheduleMusicPlayerJob(context!!, Constants.MUSIC_JOB_PLAY_OR_PAUSE_SONG_FLAG)
        }

        if(intent?.action.equals(Constants.NEXT_SONG_MUSIC_ACTION)){
            Toast.makeText(context, "Performing action: ${Constants.NEXT_SONG_MUSIC_ACTION}", Toast.LENGTH_SHORT).show()
            scheduleMusicPlayerJob(context!!, Constants.MUSIC_JOB_NEXT_SONG_FLAG)
        }

        if(intent?.action.equals(Constants.PREVIOUS_SONG_MUSIC_ACTION)){
            Toast.makeText(context, "Performing action: ${Constants.PREVIOUS_SONG_MUSIC_ACTION}", Toast.LENGTH_SHORT).show()
            scheduleMusicPlayerJob(context!!, Constants.MUSIC_JOB_PREVIOUS_SONG_FLAG)
        }

        if(intent?.action.equals(Constants.STOP_SONG_MUSIC_ACTION)){
            Toast.makeText(context, "Performing action: ${Constants.STOP_SONG_MUSIC_ACTION}", Toast.LENGTH_SHORT).show()
            scheduleMusicPlayerJob(context!!, Constants.MUSIC_JOB_STOP_SONG_FLAG)
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    requestCode: Int
) {
    val views = RemoteViews(context.packageName, R.layout.custom_widget)

    //Webpage intent
    val openWebPageIntent = Intent(Constants.WEBPAGE_ACTION)
    openWebPageIntent.`package` = context.packageName
    val openWebPagePendingIntent = PendingIntent.getBroadcast(
        context,
        appWidgetId,
        openWebPageIntent,
        PendingIntent.FLAG_MUTABLE
    )
    views.setOnClickPendingIntent(R.id.button_webpage, openWebPagePendingIntent)

    //Play or Pause music intent
    val playMusicIntent = Intent(Constants.PLAY_MUSIC_ACTION)
    playMusicIntent.`package` = context.packageName
    val playMusicPendingIntent = PendingIntent.getBroadcast(
        context,
        appWidgetId,
        playMusicIntent,
        PendingIntent.FLAG_MUTABLE
    )
    views.setOnClickPendingIntent(R.id.button_play, playMusicPendingIntent)

    //Next song music intent
    val nextSongMusicIntent = Intent(Constants.NEXT_SONG_MUSIC_ACTION)
    nextSongMusicIntent.`package` = context.packageName
    val nextSongMusicPendingIntent = PendingIntent.getBroadcast(
        context,
        appWidgetId,
        nextSongMusicIntent,
        PendingIntent.FLAG_MUTABLE
    )
    views.setOnClickPendingIntent(R.id.button_next_song, nextSongMusicPendingIntent)

    //Previous song music intent
    val previousSongMusicIntent = Intent(Constants.PREVIOUS_SONG_MUSIC_ACTION)
    previousSongMusicIntent.`package` = context.packageName
    val previousSongMusicPendingIntent = PendingIntent.getBroadcast(
        context,
        appWidgetId,
        previousSongMusicIntent,
        PendingIntent.FLAG_MUTABLE
    )
    views.setOnClickPendingIntent(R.id.button_previous_song, previousSongMusicPendingIntent)

    //Stop song music intent
    val stopSongMusicIntent = Intent(Constants.STOP_SONG_MUSIC_ACTION)
    stopSongMusicIntent.`package` = context.packageName
    val stopSongMusicPendingIntent = PendingIntent.getBroadcast(
        context,
        appWidgetId,
        stopSongMusicIntent,
        PendingIntent.FLAG_MUTABLE
    )
    views.setOnClickPendingIntent(R.id.button_stop, stopSongMusicPendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

internal fun scheduleMusicPlayerJob(context: Context, jobTypeFlag: String){
    val bundle = PersistableBundle()
    bundle.putString(Constants.JOB_TYPE_BUNDLE_KEY, jobTypeFlag)

    val cn = ComponentName(context, MusicPlayerService::class.java)
    val info = JobInfo.Builder(80, cn)
        .setRequiresBatteryNotLow(true)
        .setPersisted(true)
        .setExtras(bundle)
        .setPeriodic(15*60*1000)
        .build()

    val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    scheduler.schedule(info)
}
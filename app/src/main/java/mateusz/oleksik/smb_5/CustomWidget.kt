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

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        Toast.makeText(context, "On enabled" , Toast.LENGTH_LONG).show()
    }

    override fun onDisabled(context: Context) {}

    companion object {
        var currentImage = "picture_1"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        //WEBPAGE
        if(intent?.action.equals(Constants.WEBPAGE_ACTION)){
            Toast.makeText(context, "Performing action: ${Constants.WEBPAGE_ACTION}", Toast.LENGTH_SHORT).show()

            val intent2 = Intent(Intent.ACTION_VIEW, Uri.parse("https://pja.edu.pl/"))
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent2)
        }

        //IMAGE
        if(intent?.action.equals(Constants.SWITCH_IMAGE_ACTION)){
            Toast.makeText(context, "Performing action: ${Constants.SWITCH_IMAGE_ACTION}", Toast.LENGTH_SHORT).show()
            val remoteViews = RemoteViews(context!!.packageName, R.layout.custom_widget)

            var drawableId: Int = R.drawable.picture_1 //default
            when (currentImage){
                "picture_1" -> {
                    drawableId = R.drawable.picture_2
                    currentImage = "picture_2"
                }
                "picture_2" -> {
                    drawableId = R.drawable.picture_3
                    currentImage = "picture_3"
                }
                "picture_3" -> {
                    drawableId = R.drawable.picture_1
                    currentImage = "picture_1"
                }
            }
            remoteViews.setImageViewResource(R.id.imageView, drawableId)

            val manager = AppWidgetManager.getInstance(context)
            manager.updateAppWidget(ComponentName(context, CustomWidget::class.java), remoteViews)
        }

        //MUSIC
        if(intent?.action.equals(Constants.PLAY_MUSIC_ACTION)){
            //Toast.makeText(context, "Performing action: ${Constants.PLAY_MUSIC_ACTION}", Toast.LENGTH_SHORT).show()
            scheduleMusicPlayerJob(context!!, Constants.MUSIC_JOB_PLAY_OR_PAUSE_SONG_FLAG)
        }

        if(intent?.action.equals(Constants.NEXT_SONG_MUSIC_ACTION)){
            //Toast.makeText(context, "Performing action: ${Constants.NEXT_SONG_MUSIC_ACTION}", Toast.LENGTH_SHORT).show()
            scheduleMusicPlayerJob(context!!, Constants.MUSIC_JOB_NEXT_SONG_FLAG)
        }

        if(intent?.action.equals(Constants.PREVIOUS_SONG_MUSIC_ACTION)){
            //Toast.makeText(context, "Performing action: ${Constants.PREVIOUS_SONG_MUSIC_ACTION}", Toast.LENGTH_SHORT).show()
            scheduleMusicPlayerJob(context!!, Constants.MUSIC_JOB_PREVIOUS_SONG_FLAG)
        }

        if(intent?.action.equals(Constants.STOP_SONG_MUSIC_ACTION)){
            //Toast.makeText(context, "Performing action: ${Constants.STOP_SONG_MUSIC_ACTION}", Toast.LENGTH_SHORT).show()
            scheduleMusicPlayerJob(context!!, Constants.MUSIC_JOB_STOP_SONG_FLAG)
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
) {
    val views = RemoteViews(context.packageName, R.layout.custom_widget)

    //Webpage intent
    val openWebPagePendingIntent = getPendingIntentForAction(context, appWidgetId, Constants.WEBPAGE_ACTION)
    views.setOnClickPendingIntent(R.id.button_webpage, openWebPagePendingIntent)

    //Switch image
    val switchImagePendingIntent = getPendingIntentForAction(context, appWidgetId, Constants.SWITCH_IMAGE_ACTION)
    views.setOnClickPendingIntent(R.id.button_image_switch, switchImagePendingIntent)

    //Play or Pause music intent
    val playMusicPendingIntent = getPendingIntentForAction(context, appWidgetId, Constants.PLAY_MUSIC_ACTION)
    views.setOnClickPendingIntent(R.id.button_play, playMusicPendingIntent)

    //Next song music intent
    val nextSongMusicPendingIntent = getPendingIntentForAction(context, appWidgetId, Constants.NEXT_SONG_MUSIC_ACTION)
    views.setOnClickPendingIntent(R.id.button_next_song, nextSongMusicPendingIntent)

    //Previous song music intent
    val previousSongMusicPendingIntent = getPendingIntentForAction(context, appWidgetId, Constants.PREVIOUS_SONG_MUSIC_ACTION)
    views.setOnClickPendingIntent(R.id.button_previous_song, previousSongMusicPendingIntent)

    //Stop song music intent
    val stopSongMusicPendingIntent = getPendingIntentForAction(context, appWidgetId, Constants.STOP_SONG_MUSIC_ACTION)
    views.setOnClickPendingIntent(R.id.button_stop, stopSongMusicPendingIntent)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}

//Creates pending intent for action name provided in argument
internal fun getPendingIntentForAction(context: Context, id: Int, actionName: String): PendingIntent {
    val intent = Intent(actionName)
    intent.`package` = context.packageName
    return PendingIntent.getBroadcast(
        context,
        id,
        intent,
        PendingIntent.FLAG_MUTABLE
    )
}

//Schedules job for service responsible for playing music
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
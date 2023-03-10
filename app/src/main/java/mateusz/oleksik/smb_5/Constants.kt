package mateusz.oleksik.smb_5

class Constants {
    companion object{
        //Actions send by CustomWidget
        const val WEBPAGE_ACTION = "mateusz.oleksik.smb_5.OPEN_WEB_PAGE"
        const val SWITCH_IMAGE_ACTION = "mateusz.oleksik.smb_5.SWITCH_IMAGE"
        const val PLAY_MUSIC_ACTION = "mateusz.oleksik.smb_5.PLAY_MUSIC"
        const val NEXT_SONG_MUSIC_ACTION = "mateusz.oleksik.smb_5.NEXT_SONG"
        const val PREVIOUS_SONG_MUSIC_ACTION = "mateusz.oleksik.smb_5.PREVIOUS_SONG"
        const val STOP_SONG_MUSIC_ACTION = "mateusz.oleksik.smb_5.STOP_SONG"

        //MusicPlayerService job flags
        const val JOB_TYPE_BUNDLE_KEY = "Job_type"
        const val MUSIC_JOB_PLAY_OR_PAUSE_SONG_FLAG = "PLAY_OR_PAUSE_SONG"
        const val MUSIC_JOB_NEXT_SONG_FLAG = "NEXT_SONG"
        const val MUSIC_JOB_PREVIOUS_SONG_FLAG = "PREVIOUS_SONG"
        const val MUSIC_JOB_STOP_SONG_FLAG = "STOP_SONG"
    }
}
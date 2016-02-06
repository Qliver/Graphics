package com.example.oli.graphics;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Oli on 06.02.2016.
 */
public class MainActivity extends AppCompatActivity {

    AnimationDrawable oliAnimation;

    boolean test = false;
    Context mContext;
    AudioManager am;
    MediaPlayer mySound;
    int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        ImageView oliImage = (ImageView) findViewById(R.id.imgDrawable);
        oliImage.setBackgroundResource(R.drawable.animation_list);
        oliAnimation = (AnimationDrawable) oliImage.getBackground();

        mContext = this.getApplicationContext();
        am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mySound = MediaPlayer.create(this,R.raw.kalimba);

        // Request audio focus for playback
        result = am.requestAudioFocus(afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

    }

    AudioManager.OnAudioFocusChangeListener afChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                        mySound.pause();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        mySound.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        am.abandonAudioFocus(afChangeListener);
                        mySound.stop();
                    }
                }
            };

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (test == false) {
                oliAnimation.start();
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback.
                    mySound.start();
                }
                test = true;

            } else {
                oliAnimation.stop();
                mySound.pause();
                test = false;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    protected void onPause() {
        super.onPause();
        oliAnimation.stop();
        mySound.pause();
        test = false;
    }

    protected void onStop() {
        super.onStop();
        //am.abandonAudioFocus(afChangeListener);
        mySound.pause();
        oliAnimation.stop();
        test = false;
    }

    protected void onStart(){
        super.onStart();
        // Request audio focus for playback
        result = am.requestAudioFocus(afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);
    }


}

package info.kimjihyok.ripplesoundplayerproject;

import android.Manifest;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import info.kimjihyok.ripplesoundplayer.RippleStatusBar;
import info.kimjihyok.ripplesoundplayer.RippleStatusBarView;
import info.kimjihyok.ripplesoundplayer.SoundPlayerView;

public class MainActivity extends AppCompatActivity {
  private SoundPlayerView soundPlayerView;

  private String[] permissions = {Manifest.permission.RECORD_AUDIO};
  private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.obama);
    soundPlayerView = (SoundPlayerView) findViewById(R.id.sound_player_view);
    soundPlayerView.setRippleColor(Color.BLUE);
    soundPlayerView.setPlayStopListener(new SoundPlayerView.OnMediaControlListener() {
      @Override
      public void onPlay() {

      }

      @Override
      public void onStop() {

      }
    });

    soundPlayerView.setMediaPlayer(mediaPlayer);
    showSoundPlayerActionButton(false);
    ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
  }

  private void showSoundPlayerActionButton(boolean shouldShow) {
    if (shouldShow) {
      soundPlayerView.setActionButtonTextColor(Color.BLUE);
      soundPlayerView.setActionButtonText("Request Now");
      soundPlayerView.enableAction(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          // do something when clicked
        }
      });
    } else {
      soundPlayerView.disableAction();
    }
  }


  @Override
  protected void onStop() {
    super.onStop();
    soundPlayerView.onStop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    soundPlayerView.onDestroy();
  }
}



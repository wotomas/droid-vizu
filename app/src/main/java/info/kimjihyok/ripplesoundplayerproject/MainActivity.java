package info.kimjihyok.ripplesoundplayerproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import info.kimjihyok.ripplesoundplayer.RippleVisualizerView;
import info.kimjihyok.ripplesoundplayer.SoundPlayerView;
import info.kimjihyok.ripplesoundplayer.renderer.BarRenderer;
import info.kimjihyok.ripplesoundplayer.renderer.ColorfulBarRenderer;
import info.kimjihyok.ripplesoundplayer.renderer.LineRenderer;
import info.kimjihyok.ripplesoundplayer.util.PaintUtil;

public class MainActivity extends AppCompatActivity {
  private String[] permissions = {Manifest.permission.RECORD_AUDIO};
  private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

  private SoundPlayerView soundPlayerView;
  private TextView titleText;

  // Demo Purpose
  private RippleVisualizerView renderDemoView;
  private RendererMode currentRenderMode;

  private enum RendererMode {
    LINE, BAR_GRAPH, COLORFUL_BAR_GRAPH
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    renderDemoView = (RippleVisualizerView) findViewById(R.id.line_renderer_demo);
    titleText = (TextView) findViewById(R.id.title_text);
    ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    switch (requestCode) {
      case REQUEST_RECORD_AUDIO_PERMISSION: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.obama);
          soundPlayerView = (SoundPlayerView) findViewById(R.id.sound_player_view);
          soundPlayerView.setMediaPlayer(mediaPlayer);
          soundPlayerView.setPlayStopListener(new SoundPlayerView.OnMediaControlListener() {
            @Override
            public void onPlay() {
              renderDemoView.play();
            }

            @Override
            public void onStop() {
              renderDemoView.stop();
            }
          });
          showSoundPlayerActionButton(true);

          currentRenderMode = RendererMode.LINE;
          setRenderer();
          renderDemoView.setMediaPlayer(mediaPlayer);

//          soundPlayerView.setContainerBackground(ContextCompat.getDrawable(this, R.drawable.border_background));
        }
      }
    }
  }


  private void showSoundPlayerActionButton(boolean shouldShow) {
    if (shouldShow) {
      soundPlayerView.setActionButtonTextColor(Color.BLUE);
      soundPlayerView.setActionButtonText("Change Mode");
      soundPlayerView.enableAction(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          switch (currentRenderMode) {
            case LINE:
              currentRenderMode = RendererMode.BAR_GRAPH;
              titleText.setText("Bar Renderer Mode");
              break;
            case BAR_GRAPH:
              currentRenderMode = RendererMode.COLORFUL_BAR_GRAPH;
              titleText.setText("Colorful Bar Renderer Mode");
              break;
            case COLORFUL_BAR_GRAPH:
              currentRenderMode = RendererMode.LINE;
              titleText.setText("Line Renderer Mode");
              break;
          }
          setRenderer();
        }
      });
    } else {
      soundPlayerView.disableAction();
    }
  }

  private void setRenderer() {
    switch (currentRenderMode) {
      case LINE:
        renderDemoView.setCurrentRenderer(new LineRenderer(PaintUtil.getLinePaint(Color.BLACK)));
        break;
      case BAR_GRAPH:
        renderDemoView.setCurrentRenderer(new BarRenderer(16, PaintUtil.getBarGraphPaint(Color.BLUE)));
        break;
      case COLORFUL_BAR_GRAPH:
        renderDemoView.setCurrentRenderer(new ColorfulBarRenderer(8, PaintUtil.getBarGraphPaint(Color.BLUE)
            , Color.parseColor("#FF0033")
            , Color.parseColor("#801AB3"))
        );
        break;
    }
  }


  @Override
  protected void onStop() {
    super.onStop();
    soundPlayerView.onStop();
    renderDemoView.stop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    soundPlayerView.onDestroy();
    renderDemoView.destroy();
  }
}



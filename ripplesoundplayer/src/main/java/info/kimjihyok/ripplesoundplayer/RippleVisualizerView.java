package info.kimjihyok.ripplesoundplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.view.View;

import info.kimjihyok.ripplesoundplayer.renderer.Renderer;

/**
 * Created by jihyokkim on 2017. 8. 30..
 */

public class RippleVisualizerView extends View {
  private static final String TAG = "RippleStatusBarView";
  private byte[] data;
  private MediaPlayer mediaPlayer;
  private Visualizer audioVisualizer;
  private Renderer currentRenderer;

  public RippleVisualizerView(Context context) {
    super(context);
  }

  public RippleVisualizerView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RippleVisualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setCurrentRenderer(Renderer currentRenderer) {
    this.currentRenderer = currentRenderer;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (data == null) return;

    currentRenderer.render(canvas, data, getWidth(), getHeight());
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int minw =  getPaddingLeft() + getPaddingRight();
    int w = resolveSizeAndState(minw, widthMeasureSpec, 0);

    int minh =  getPaddingBottom() + getPaddingTop();
    int h = resolveSizeAndState(minh, heightMeasureSpec, 0);

    setMeasuredDimension(w, h);
  }

  public void setRippleColor(int color) {
    currentRenderer.changeColor(color);
    invalidate();
  }

  public void setMediaPlayer(MediaPlayer player) {
    this.mediaPlayer = player;
    audioVisualizer = new Visualizer(mediaPlayer.getAudioSessionId());
    audioVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
    audioVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
      @Override
      public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int i) {
        if (!currentRenderer.isFFTDataRequired()) {
          updateVisualizer(bytes);
        }

      }

      @Override
      public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) {
        if (currentRenderer.isFFTDataRequired()) {
          updateVisualizer(bytes);
        }
      }
    }, Visualizer.getMaxCaptureRate() / 2, true, true);

    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        audioVisualizer.setEnabled(false);
        mediaPlayFinishCallback.finished();

      }
    });
  }

  private SoundPlayerView.OnMediaPlayFinishCallback mediaPlayFinishCallback;

  public void setOnMediaPlayFinishCallbackk(SoundPlayerView.OnMediaPlayFinishCallback mediaPlayFinishCallback) {
    this.mediaPlayFinishCallback = mediaPlayFinishCallback;
  }

  public void play() {
    if (mediaPlayer == null) {
      throw new IllegalStateException("Media Player is not ready! Please call setMediaPlayer(MediaPlayer player) before calling play()");
    }

    audioVisualizer.setEnabled(true);
    mediaPlayer.start();
  }

  private void updateVisualizer(byte[] bytes) {
    this.data = bytes;
    invalidate();
  }

  public void stop() {
    audioVisualizer.setEnabled(false);
    mediaPlayer.pause();
    mediaPlayer.seekTo(0);
  }

  public void destroy() {
    mediaPlayer.stop();
  }

  public void setAmplitudePercentage(double ampValue) {
    currentRenderer.setAmpValue(ampValue);
    invalidate();
  }
}

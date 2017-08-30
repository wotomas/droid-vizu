package info.kimjihyok.ripplesoundplayer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by jihyokkim on 2017. 8. 30..
 */

public class RippleStatusBarView extends View implements RippleStatusBar {
  private static final String TAG = "RippleStatusBarView";
  private byte[] data;
  private MediaPlayer mediaPlayer;
  private float[] points;
  private Visualizer audioVisualizer;

  // Paint parameters
  @ColorInt
  private int rippleColor;
  private Paint ripplePaint;

  public RippleStatusBarView(Context context) {
    super(context);
    init(context, null);
  }

  public RippleStatusBarView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public RippleStatusBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    rippleColor = Color.BLUE;
    setupPaint();
  }

  private void setupPaint() {
    ripplePaint = new Paint();
    ripplePaint.setStyle(Paint.Style.STROKE);
    ripplePaint.setColor(rippleColor);
    ripplePaint.setAntiAlias(true);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (data == null) return;

    if (points == null || points.length < data.length * 4) {
      points = new float[data.length * 4];
    }

    int w = getWidth();
    int h = getHeight();

    for (int i = 0; i < data.length - 1; i++) {
      points[i * 4] = w * i / (data.length - 1);
      points[i * 4 + 1] = h / 2 + ((byte) (data[i] + 128)) * (h / 2) / 128;
      points[i * 4 + 2] = w * (i + 1) / (data.length - 1);
      points[i * 4 + 3] = h / 2 + ((byte) (data[i + 1] + 128)) * (h / 2) / 128;
    }

    canvas.drawLines(points, ripplePaint);
  }


  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int minw =  getPaddingLeft() + getPaddingRight();
    int w = resolveSizeAndState(minw, widthMeasureSpec, 0);

    int minh =  getPaddingBottom() + getPaddingTop();
    int h = resolveSizeAndState(minh, heightMeasureSpec, 0);

    setMeasuredDimension(w, h);
  }

  @Override
  public void setRippleColor(@ColorInt int color) {
    this.rippleColor = color;
    setupPaint();
    invalidate();
  }

  @Override
  public void setMediaPlayer(MediaPlayer player) {
    this.mediaPlayer = player;
    audioVisualizer = new Visualizer(mediaPlayer.getAudioSessionId());
    audioVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
    audioVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
      @Override
      public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int i) {
        updateVisualizer(bytes);
      }

      @Override
      public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) {
        // do nothing
      }
    }, Visualizer.getMaxCaptureRate() / 2, true, true);

    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        audioVisualizer.setEnabled(false);
      }
    });
  }

  @Override
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

  @Override
  public void stop() {
    audioVisualizer.setEnabled(false);
    mediaPlayer.pause();
    mediaPlayer.seekTo(0);
  }

  @Override
  public void destory() {
    mediaPlayer.stop();
  }

  public MediaPlayer getMediaPlayer() {
    return mediaPlayer;
  }
}

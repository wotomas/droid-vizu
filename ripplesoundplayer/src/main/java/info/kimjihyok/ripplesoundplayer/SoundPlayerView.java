package info.kimjihyok.ripplesoundplayer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import info.kimjihyok.ripplesoundplayer.renderer.LineRenderer;
import info.kimjihyok.ripplesoundplayer.renderer.Renderer;
import info.kimjihyok.ripplesoundplayer.util.PaintUtil;

/**
 * Created by jihyokkim on 2017. 8. 30..
 */

public class SoundPlayerView extends LinearLayout {
  private TextView mediaLengthTextView;
  private ImageView mediaControlButton;
  private RippleVisualizerView rippleVisualizerView;
  private TextView actionButton;
  private Drawable playingDrawable;
  private Drawable pauseDrawable;

  private OnMediaControlListener listener;

  enum State {
    PLAYING, PAUSED
  }

  public interface OnMediaControlListener {
    void onPlay();
    void onStop();
  }

  private State currentMediaState = State.PAUSED;

  private int rippleColor;
  private boolean enableActionButton;
  private int mediaLengthInMilliseconds;


  public SoundPlayerView(Context context) {
    super(context);
    init(context, null);
  }

  public SoundPlayerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public SoundPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SoundPlayerView, 0, 0);
    try {
      rippleColor = a.getColor(R.styleable.SoundPlayerView_SoundPlayerView_rippleColor, Color.GRAY);
      enableActionButton = a.getBoolean(R.styleable.SoundPlayerView_SoundPlayerView_enableActionButton, false);
      mediaLengthInMilliseconds = a.getInt(R.styleable.SoundPlayerView_SoundPlayerView_mediaFileLength, 0);
    } finally {
      a.recycle();
    }

    inflate(context, R.layout.view_sound_player, this);

    mediaLengthTextView = (TextView) findViewById(R.id.total_media_length_textview);
    mediaControlButton = (ImageView) findViewById(R.id.media_control_button);
    rippleVisualizerView = (RippleVisualizerView) findViewById(R.id.ripple_status_view);
    rippleVisualizerView.setCurrentRenderer(new LineRenderer(PaintUtil.getLinePaint(Color.BLUE)));
    actionButton = (TextView) findViewById(R.id.action_button);

    setSecondToFirstDecimalPoint(mediaLengthInMilliseconds);

    setMediaControlDrawable(getResources().getDrawable(R.drawable.ic_voice_play), getResources().getDrawable(R.drawable.ic_voice_pause));
    setMediaButtonClickListener();

    setRippleColor(rippleColor);
    if (enableActionButton) {
      enableAction(null);
      setActionButtonText("Request Now");
    } else {
      disableAction();
    }
  }

  public void setContainerBackground(Drawable drawable){
    if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
      setBackgroundDrawable(drawable);
    } else {
      setBackground(drawable);
    }
  }

  public void setRenderer(Renderer renderer) {
    rippleVisualizerView.setCurrentRenderer(renderer);
  }


  public void setPlayStopListener(OnMediaControlListener listener) {
    this.listener = listener;
  }

  private void setMediaButtonClickListener() {
    mediaControlButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        switch (currentMediaState) {
          case PLAYING:
            currentMediaState = State.PAUSED;
            rippleVisualizerView.stop();
            if (listener != null) listener.onStop();
            break;
          case PAUSED:
            currentMediaState = State.PLAYING;
            rippleVisualizerView.play();
            if (listener != null) listener.onPlay();
            break;
        }

        updateControlButton();
      }
    });
  }

  public void disableAction() {
    actionButton.setVisibility(GONE);
    actionButton.setOnClickListener(null);
  }

  public void enableAction(OnClickListener listener) {
    actionButton.setVisibility(VISIBLE);
    if (listener != null) {
      actionButton.setOnClickListener(listener);
    }
  }

  public void setActionButtonText(String text) {
    actionButton.setText(text);
  }

  public void setRippleColor(int color) {
    rippleVisualizerView.setRippleColor(color);
  }

  private void setSecondToFirstDecimalPoint(int mediaLengthInMilliseconds) {
    double seconds = mediaLengthInMilliseconds / 1000.0;
    String duration = new DecimalFormat("0.0").format(seconds) + "sec";
    mediaLengthTextView.setText(duration);
  }


  public void setMediaControlDrawable(Drawable playButton, Drawable pauseButton) {
    playingDrawable = playButton;
    pauseDrawable = pauseButton;

    updateControlButton();
  }

  private void updateControlButton() {
    if (pauseDrawable == null || playingDrawable == null) {
      throw new IllegalStateException("You need to declare setMediaControlDrawables() before updating control button");
    }

    switch (currentMediaState) {
      case PLAYING:
        mediaControlButton.setImageDrawable(pauseDrawable);
        break;
      case PAUSED:
        mediaControlButton.setImageDrawable(playingDrawable);
        break;
    }
  }

  protected interface OnMediaPlayFinishCallback {
    void finished();
  }

  public void setMediaPlayer(MediaPlayer player) {
    rippleVisualizerView.setMediaPlayer(player);
    int durationMilliseconds = player.getDuration();
    if (durationMilliseconds == -1) {
      durationMilliseconds = 0;
    }

    setSecondToFirstDecimalPoint(durationMilliseconds);
    rippleVisualizerView.setOnMediaPlayFinishCallbackk(new OnMediaPlayFinishCallback() {
      @Override
      public void finished() {
        currentMediaState = State.PAUSED;
        updateControlButton();
      }
    });
  }

  public void setActionButtonTextColor(int actionButtonTextColor) {
    actionButton.setTextColor(actionButtonTextColor);
  }

  public void onStop() {
    currentMediaState = State.PAUSED;
    updateControlButton();
    rippleVisualizerView.stop();
  }

  public void onDestroy() {
    rippleVisualizerView.destroy();
  }
}

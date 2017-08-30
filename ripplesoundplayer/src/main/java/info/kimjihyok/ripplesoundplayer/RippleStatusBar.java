package info.kimjihyok.ripplesoundplayer;

import android.media.MediaPlayer;
import android.support.annotation.ColorInt;

/**
 * Created by jihyokkim on 2017. 8. 30..
 */

interface RippleStatusBar {
  void setRippleColor(@ColorInt int color);
  void setMediaPlayer(MediaPlayer player);

  void play();
  void stop();
  void destroy();
}

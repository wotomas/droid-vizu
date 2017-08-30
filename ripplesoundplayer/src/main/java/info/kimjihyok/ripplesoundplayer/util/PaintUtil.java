package info.kimjihyok.ripplesoundplayer.util;

import android.graphics.Paint;
import android.support.annotation.ColorInt;

/**
 * Created by jkimab on 2017. 8. 30..
 */

public class PaintUtil {
  public static Paint getLinePaint(@ColorInt int color) {
    Paint paint = new Paint();
    paint.setColor(color);
    paint.setStyle(Paint.Style.STROKE);
    paint.setAntiAlias(true);
    return paint;
  }
}

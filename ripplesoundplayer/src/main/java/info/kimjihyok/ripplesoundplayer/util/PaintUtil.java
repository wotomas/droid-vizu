package info.kimjihyok.ripplesoundplayer.util;

import android.graphics.Paint;

/**
 * Created by jkimab on 2017. 8. 30..
 */

public class PaintUtil {
  public static Paint getLinePaint(int color) {
    Paint paint = new Paint();
    paint.setColor(color);
    paint.setStyle(Paint.Style.STROKE);
    paint.setAntiAlias(true);
    return paint;
  }

  public static Paint getBarGraphPaint(int color) {
    Paint paint = new Paint();
    paint.setStrokeWidth(50f);
    paint.setAntiAlias(true);
    paint.setColor(color);
    return paint;
  }
}

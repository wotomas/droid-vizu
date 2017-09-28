package info.kimjihyok.ripplesoundplayer.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by jkimab on 2017. 8. 30..
 */

public class LineRenderer extends Renderer {
  private static final String TAG = "LineRenderer";
  private Paint paint;

  public LineRenderer(Paint paint) {
    this.paint = paint;
  }

  @Override
  public void render(Canvas canvas, byte[] data, int w, int h) {
    super.render(canvas, data, w, h);

    for (int i = 0; i < data.length - 1; i++) {
      points[i * 4] = w * i / (data.length - 1);
      points[i * 4 + 1] = (float) (h / 2 + ((byte) (data[i] + 128) * ampValue) * (h / 2) / 128);
      points[i * 4 + 2] = w * (i + 1) / (data.length - 1);
      points[i * 4 + 3] = (float) (h / 2 + ((byte) (data[i + 1] + 128) * ampValue) * (h / 2) / 128);
    }

    canvas.drawLines(points, paint);
  }

  @Override
  public void changeColor(int color) {
    paint.setColor(color);
  }

  @Override
  public boolean isFFTDataRequired() {
    return false;
  }
}

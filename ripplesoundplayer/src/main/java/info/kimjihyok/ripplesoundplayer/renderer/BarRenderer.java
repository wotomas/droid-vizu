package info.kimjihyok.ripplesoundplayer.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * ported version of package com.pheelicks.visualizer.renderer;
 */
public class BarRenderer extends Renderer {
  private static final String TAG = "BarRenderer";
  private int divisions;
  private Paint paint;


  /**
   * Renders data as a series of lines, in histogram form
   * divisions - must be a power of 2. Controls how many lines to draw
   * paint - Paint to draw lines with
   */
  public BarRenderer(int divisions, Paint paint) {
    this.divisions = divisions;
    this.paint = paint;
  }

  @Override
  public void render(Canvas canvas, byte[] data, int width, int height) {
    super.render(canvas, data, width, height);

    for (int i = 0; i < data.length / divisions; i++) {
      points[i * 4] = i * 4 * divisions;
      points[i * 4 + 2] = i * 4 * divisions;

      byte rfk = data[divisions * i];
      byte ifk = data[divisions * i + 1];

      float magnitude = (rfk * rfk + ifk * ifk);
      int dbValue = (int) ((int) (10 * Math.log10(magnitude)) * ampValue);

      points[i * 4 + 1] = height;
      points[i * 4 + 3] = height - (dbValue * 2 - 10);
    }

    canvas.drawLines(points, paint);
  }

  @Override
  public void changeColor(int color) {
    paint.setColor(color);
  }

  @Override
  public boolean isFFTDataRequired() {
    return true;
  }
}

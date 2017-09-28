package info.kimjihyok.ripplesoundplayer.renderer;

import android.animation.ArgbEvaluator;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by jkimab on 2017. 8. 31..
 */

public class ColorfulBarRenderer extends Renderer {
  private static final String TAG = "ColorfulBarRenderer";
  private int divisions;
  private Paint paint;
  private float colorInterpolatePercentage;

  private int startColor;

  private int endColor;
  private ArgbEvaluator rgbEvaluator;


  /**
   * Renders data as a series of lines, in histogram form
   * divisions - must be a power of 2. Controls how many lines to draw
   * paint - Paint to draw lines with
   * startColor - color to start interpolation with
   * endCOlor - color to end interpolate with
   */
  public ColorfulBarRenderer(int divisions, Paint paint, int startColor, int endColor) {
    this.divisions = divisions;
    this.paint = paint;
    this.startColor = startColor;
    this.endColor = endColor;
    this.colorInterpolatePercentage = 0;
    this.rgbEvaluator = new ArgbEvaluator();
  }

  @Override
  public void render(Canvas canvas, byte[] data, int width, int height) {
    super.render(canvas, data, width, height);
    if (colorInterpolatePercentage > 1.0) {
      colorInterpolatePercentage = 0;
      int tempColor = startColor;
      startColor = endColor;
      endColor = tempColor;
    }

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

    changeColor(interpolateColor(startColor, endColor, colorInterpolatePercentage));
    canvas.drawLines(points, paint);
    colorInterpolatePercentage += 0.05;
  }

  private int interpolateColor(int firstColor, int secondColor, float proportion) {
    return (int) rgbEvaluator.evaluate(proportion, firstColor, secondColor);
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

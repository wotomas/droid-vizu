package info.kimjihyok.ripplesoundplayer.renderer;

import android.graphics.Canvas;

/**
 * Created by jkimab on 2017. 8. 30..
 */

public abstract class Renderer {
  protected float[] points;
  protected double ampValue = 1.0;

  public void render(Canvas canvas, byte[] data, int width, int height){
    if (points == null || points.length < data.length * 4) {
      points = new float[data.length * 4];
    }

  }

  public abstract void changeColor(int color);
  public abstract boolean isFFTDataRequired();

  public void setAmpValue(double ampValue) {
    this.ampValue = ampValue;
  }
}

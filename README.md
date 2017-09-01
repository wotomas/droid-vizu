# Droid-vizu
![API](https://img.shields.io/badge/API-15%2B-blue.svg?style=flat) ![Download](https://api.bintray.com/packages/wotomas/maven/droid-vizu/images/download.svg)

# About
**Droid-vizu** aims to provide cool visualization effects for any Android audio project. Maintainable modular design allows users to easily swap Renderer class to get corresponding effects.

# Demo (click for audio)
[<img src="media/color_bar_renderer.gif" width="250" />](https://youtu.be/cfoTQodcp9U)
[<img src="media/bar_renderer.gif" width="250" />](https://youtu.be/cfoTQodcp9U)
[<img src="media/line_renderer.gif" width="250" />](https://youtu.be/cfoTQodcp9U)


# Implement
```gradle
repositories {
  jcenter()
}

dependencies {
  compile 'info.kimjihyok:droid-vizu:${droid-vizu}'
}
```

## XML Setup
```XML
    <!-- Vizualizer -->
    <info.kimjihyok.ripplesoundplayer.RippleVisualizerView
        android:id="@+id/renderer_demo"
        android:layout_margin="16dp"
        android:layout_width="match_parent"
        android:layout_height="50dp"/>
        
    <!-- Sample Player -->
    <info.kimjihyok.ripplesoundplayer.SoundPlayerView
        android:id="@+id/sound_player_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"/>    
```

## Code Usage
Simply Provide RippleVisualizerViw with an prepared Android MediaPlayer and set with a corresponding renderer

```java
    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sound_file);  // or any other way to initialize media player are prepare()
    soundPlayerView = (SoundPlayerView) findViewById(R.id.sound_player_view);
    soundPlayerView.setMediaPlayer(mediaPlayer);
    soundPlayerView.setPlayStopListener(new SoundPlayerView.OnMediaControlListener() {
        @Override
        public void onPlay() {
          renderDemoView.play();
        }

        @Override
        public void onStop() {
          renderDemoView.stop();
        }
      });
      
    renderDemoView = (RippleVisualizerView) findViewById(R.id.renderer_demo);
    renderDemoView.setMediaPlayer(mediaPlayer);
    renderDemoView.setCurrentRenderer(renderer);
```

## Renderer Information
Detailed information about parameters could be found in the source code! 

### ColorBarRenderer
[<img src="media/color_bar_renderer.gif" width="500" />]() 
```java
  renderDemoView.setCurrentRenderer(new ColorfulBarRenderer(8, PaintUtil.getBarGraphPaint(Color.BLUE)
            , Color.parseColor("#FF0033")
            , Color.parseColor("#801AB3"))
```

### BarRenderer
[<img src="media/bar_renderer.gif" width="500" />]() 
```java
  new BarRenderer(16, PaintUtil.getBarGraphPaint(Color.WHITE));
```

### LineRenderer
[<img src="media/line_renderer.gif" width="500" />]() 
```java
  new LineRenderer(PaintUtil.getLinePaint(Color.YELLOW));
```


## Stop and Destory
```java
// It is required to stop at onStop and to destory at onDestory to prevent memory leak and unexpected events
@Override
protected void onStop() {
  super.onStop();
  soundPlayerView.onStop();
  renderDemoView.stop();
}
 
@Override
protected void onDestroy() {
  super.onDestroy();
  soundPlayerView.onDestroy();
  renderDemoView.destroy();
}
```

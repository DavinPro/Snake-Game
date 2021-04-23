package com.github.davinpro;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public abstract class SoundManager {

  public enum SoundType {
    MUSIC,
    SFX;

    private double volume = 0.5;
    public double getVolume() { return this.volume; }
  }

  public enum Sound {
    MENU_MUSIC(SoundType.MUSIC,"menuMusic.wav"),
    BUTTON_PRESS(SoundType.SFX,"buttonPress.wav"),
    EAT_FRUIT(SoundType.SFX,"eatFruit.wav"),
    GAME_OVER(SoundType.SFX,"gameOver.wav");

    private final MediaPlayer mediaPlayer;
    private final SoundType type;

    Sound(SoundType type, String filename) {
      this.type = type;
      this.mediaPlayer = new MediaPlayer(new Media(getClass().getResource("/com/github/davinpro/audio/").toString() + filename));
      this.mediaPlayer.setVolume(type.volume);

      if (type == SoundType.MUSIC) {
        this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
      }
    }

    private void play() {
        mediaPlayer.seek(Duration.ZERO);
        mediaPlayer.play();
    }
  }

  public static void play(Sound sound) {
    sound.play();
  }

  public static void stop(Sound sound) {
    sound.mediaPlayer.stop();
  }

  public static void delayPlay(Sound sound, long delay) {
    Task<Void> sleeper = new Task<>() {
      @Override
      protected Void call() {
        try {
          Thread.sleep(delay);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        return null;
      }
    };

    sleeper.setOnSucceeded(event -> sound.mediaPlayer.play());

    new Thread(sleeper).start();
  }

  public static void fadeInPlay(Sound sound, double fadeDuration) {
    sound.mediaPlayer.setVolume(0);
    sound.play();
    Timeline timeline = new Timeline(
        new KeyFrame(Duration.seconds(fadeDuration),
            new KeyValue(sound.mediaPlayer.volumeProperty(), sound.type.volume)));
    timeline.play();
  }

  public static void changeVolume(SoundType soundType, double value) {
    soundType.volume = value;

    for (Sound sound : Sound.values()) {
      if (sound.type == soundType) {
        sound.mediaPlayer.setVolume(value);

        // Pause sounds when volume is set to 0
        if (value == 0) {
          sound.mediaPlayer.pause();
        } else if (sound.mediaPlayer.getStatus() == Status.PAUSED) {
          sound.mediaPlayer.play();
        }
      }
    }
  }
}

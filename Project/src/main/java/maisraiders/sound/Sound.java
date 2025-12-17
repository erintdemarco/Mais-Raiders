package maisraiders.sound;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import java.net.URL;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * A class to play sound effects during the game
 */
public class Sound {
    Clip clip; // to open audio files
    Clip musicClip;
    URL soundURL[] = new URL[15];
    // load the sound files into the array
    public Sound() {
        this.clip = null;
        this.musicClip = null;
        soundURL[0] = getClass().getResource("/sound/maintheme.wav"); // main game theme
        soundURL[1] = getClass().getResource("/sound/collect.wav"); // collecting corn
        soundURL[2] = getClass().getResource("/sound/blocked.wav"); //walking into barrier
        soundURL[3] = getClass().getResource("/sound/steps.wav"); // stepping through mud
        soundURL[4] = getClass().getResource("/sound/parry.wav"); // avoiding alien
        soundURL[5] = getClass().getResource("/sound/gameover.wav"); // game over
        soundURL[6] = getClass().getResource("/sound/fanfare.wav"); // game won
        soundURL[7] = getClass().getResource("/sound/cursor.wav"); // cursor
        soundURL[8] = getClass().getResource("/sound/powerup.wav"); // collecting pitchfork
        soundURL[9] = getClass().getResource("/sound/doorclose.wav"); // door sound (closed)
    }

    public  void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }catch (IOException | LineUnavailableException | UnsupportedAudioFileException e){
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    public void playMusic(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            musicClip = AudioSystem.getClip();
            musicClip.open(ais);
            FloatControl gainControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-10.0f); // Reduce volume by 10 decibels.
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);  // Loop the music indefinitely
            musicClip.start();
        }catch (IOException | LineUnavailableException | UnsupportedAudioFileException e){
            e.printStackTrace();
        }
    }
    public void stopMusic(){
        if (musicClip != null) {
            musicClip.stop();
        }
    }
    public void playSE(int i) {
        setFile(i);
        play();

    }
}

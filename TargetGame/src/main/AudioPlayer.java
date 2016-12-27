package main;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
	
	private Clip clip;

	public AudioPlayer(String s) {

		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource(s));
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}
	
	public void play() {
		
		if(clip == null) {return;}
		stop();
		clip.setFramePosition(0);
		clip.start();
		
	}
	
	public void stop() {
		if(clip.isRunning()) clip.stop();
	}
	public void close() {
		stop();
		clip.close();
		
	}

}

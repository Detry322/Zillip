package logic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer extends Thread{
	
	private byte[] soundData;
	private boolean doesLoop;
	private boolean stopped = false;
	private boolean soundOff = false;
	private int EXTERNAL_BUFFER_SIZE = 4096; // 128Kb
	
	public SoundPlayer(byte[] x, boolean loop) {
		soundData = x.clone();
		doesLoop = loop;
		this.start();
	}
	
	public void run() {
		do {
			try {
				play();
				if (isStopped()) break;
			} catch (Exception e) {
			}
		} while (doesLoop);
	}
	
	public void play() throws Exception {
		 
		AudioInputStream audioInputStream = null;
		try {
		    audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(soundData));
		} catch (UnsupportedAudioFileException e1) {
		    throw new Exception(e1);
		} catch (IOException e1) {
		    throw new Exception(e1);
		}
		 
		// Obtain the information about the AudioInputStream
		AudioFormat audioFormat = audioInputStream.getFormat();
		EXTERNAL_BUFFER_SIZE = audioFormat.getSampleSizeInBits()*3072;
		Info info = new Info(SourceDataLine.class, audioFormat);
		
		// opens the audio channel
		SourceDataLine dataLine = null;
		try {
		    dataLine = (SourceDataLine) AudioSystem.getLine(info);
		    dataLine.open(audioFormat, EXTERNAL_BUFFER_SIZE);
		} catch (LineUnavailableException e1) {
		    throw new Exception(e1);
		}
	 
		// Starts the music 
		dataLine.start();
	 
		int readBytes = 0;
		byte[] audioBuffer = new byte[EXTERNAL_BUFFER_SIZE];
		 
		try {
		    while (readBytes != -1) {
				readBytes = audioInputStream.read(audioBuffer, 0,
					audioBuffer.length);
				if (readBytes >= 0){
					if (Assets.soundOn() && !isSoundOff() && !isStopped())
						dataLine.write(audioBuffer, 0, readBytes);
						dataLine.drain();
				}
		    }
		} catch (IOException e1) {
		    throw new Exception(e1);
		} finally {
		    // plays what's left and and closes the audioChannel
		    dataLine.drain();
		    dataLine.close();
		}
		
	}

	public synchronized void stopPlaying() {
		stopped = true;
	}
	
	public synchronized boolean isStopped() {
		return stopped;
	}
	
	public synchronized boolean isSoundOff() {
		return soundOff;
	}

	public synchronized void setSoundOff(boolean soundOff) {
		this.soundOff = soundOff;
	}
		
}
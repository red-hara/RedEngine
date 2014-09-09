/**
 * Do what you want 'cause a pirate is free, You are a pirate! This work is licensed under the
 * Creative Commons Attribution 4.0 International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative Commons, 171 Second
 * Street, Suite 300, San Francisco, California, 94105, USA.
 */
package Engine.system;

import Engine.RedBasic;
import Engine.RedGame;
import Engine.RedSound;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author hara
 */
public class RedSoundSystem extends RedBasic {

	public byte[] soundData;
	public SourceDataLine sourceDataLine = null;
	public Thread thread;
	public int loopsLeft = 0;
	public long lastUpdateTime;
	public float volume = 1;
	public boolean ready = false;
	private boolean forceFinish = false;
	public LinkedList soundsList = new LinkedList();
	public int BUFFER_SIZE = 512;

	public RedSoundSystem() {

		AudioFormat audioFormat;

		soundData = new byte[BUFFER_SIZE];
		audioFormat = new AudioFormat((float) 44100.0, 16, 2, true, false);

		try {
			sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
			sourceDataLine.open(audioFormat);
		} catch (LineUnavailableException ex) {
			Logger.getLogger(RedSoundSystem.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public void start() {
		init();
	}

	public void start(int Loops) {
		init();
		loopsLeft = Loops;
	}

	@Override
	public void destroy() {
		soundData = null;
		forceFinish = true;
	}

	public void init() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					sourceDataLine.open();
				} catch (LineUnavailableException ex) {
					Logger.getLogger(RedSoundSystem.class.getName()).log(Level.SEVERE, null, ex);
				}
				sourceDataLine.start();
				boolean pause = false;

				byte[] gotData = null;

				for (; !forceFinish;) {
//					BUFFER_SIZE = sourceDataLine.available();
					if (RedGame.isPaused && !pause) {
						sourceDataLine.stop();
						pause = true;
					}
					if (!RedGame.isPaused && pause) {
						sourceDataLine.start();
						pause = false;
					}
					if (!pause) {
						soundData = new byte[BUFFER_SIZE];
						for (int i = 0; i < soundsList.size(); i++) {
							if (soundsList.get(i) != null) {
								gotData = ((RedSound) soundsList.get(i)).updateSound();

								for (int j = 0; j < BUFFER_SIZE; j++) {
									soundData[j] = (byte) Math.min(((soundData[j] + gotData[j])), 255);
								}
							}
						}
						sourceDataLine.write(soundData, 0, BUFFER_SIZE);
					}
				}
				sourceDataLine.close();
			}
		};

		thread = new Thread(runnable);
		thread.setName("Audio system");
		thread.setDaemon(true);
		thread.start();
	}
}

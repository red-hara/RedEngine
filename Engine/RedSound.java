/**
 * Do what you want 'cause a pirate is free, You are a pirate!
 * This work is licensed under the Creative Commons Attribution 4.0 International
 * License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative
 * Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 */

package Engine;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author hara
 */
public class RedSound extends RedObject {

	public byte[] soundData;
	public int position;
	public float volume = 1;
	public int loopsLeft = 0;

	public RedSound(String SoundPath) {
		super(0, 0, 0, 0);

		if (!"".equals(SoundPath)) {
			soundData = getAudioFileData(SoundPath);
		} else {
			soundData = new byte[RedGame.sound.BUFFER_SIZE];
		}
	}

	public RedSound(byte[] SoundData) {
		super(0, 0, 0, 0);
		soundData = new byte[SoundData.length];
		System.arraycopy(SoundData, 0, soundData, 0, SoundData.length);

	}

	public static byte[] getAudioFileData(final String Path) {
		byte[] data = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			BufferedInputStream in = new BufferedInputStream(RedSound.class.getResourceAsStream(Path));

			int read;
			byte[] buff = new byte[1024];
			while ((read = in.read(buff)) > 0) {
				out.write(buff, 0, read);
			}
			out.flush();
			data = out.toByteArray();
		} catch (IOException Exception) {
			System.err.println(Exception);
		}
		return data;
	}

	public void start(int Loop) {
		loopsLeft = Loop;
		init();
	}

	public void start() {
		loopsLeft = 0;
		init();
	}

	private void init() {
		RedGame.sound.soundsList.add(this);
	}

	@Override
	public void destroy() {
		super.destroy();
		RedGame.sound.soundsList.remove(this);
	}

	public byte[] updateSound() {
		byte[] result = new byte[RedGame.sound.BUFFER_SIZE];
		for (int i = 0; i < Math.min(RedGame.sound.BUFFER_SIZE, soundData.length - position); i++) {
			result[i] = (byte) (soundData[i + position] * volume);
		}
		position += RedGame.sound.BUFFER_SIZE;
		if (position >= soundData.length) {
			if (loopsLeft > 0) {
				loopsLeft--;
				position = 0;
			} else if (loopsLeft == -1) {
				position = 0;
			} else {
				destroy();
			}
		}
		return result;
	}
}

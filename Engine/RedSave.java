/**
 * Do what you want 'cause a pirate is free, You are a pirate! This work is licensed under the
 * Creative Commons Attribution 4.0 International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative Commons, 171 Second
 * Street, Suite 300, San Francisco, California, 94105, USA.
 */
package Engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Red Hara (rtc6fg4.fejg2@gmail.com)
 */
public class RedSave {

	public FileOutputStream saveFile;

	public RedSave(String FilePath) throws FileNotFoundException, IOException {
		new File(FilePath.substring(0, FilePath.lastIndexOf('/'))).mkdir();

		saveFile = new FileOutputStream(FilePath);
	}

	public void close() throws IOException {
		saveFile.close();
	}
}

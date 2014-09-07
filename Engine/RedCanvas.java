/**
 * Do what you want 'cause a pirate is free, You are a pirate! This work is licensed under the
 * Creative Commons Attribution 4.0 International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative Commons, 171 Second
 * Street, Suite 300, San Francisco, California, 94105, USA.
 */
package Engine;

/**
 * Canvas to draw on.
 * @author Red Hara (rtc6fg4.fejg2@gmail.com)
 */
public class RedCanvas extends RedImage {

	/**
	 * Offset used to draw on canvas.
	 */
	public RedPoint offset = new RedPoint(0, 0);

	/**
	 * Zoom of objects drawn on canvas.
	 */
	public double zoom = 1;

	/**
	 *Constructor, sets width, height and image type of canvas.
	 * @param Width Width of new canvas.
	 * @param Height Height of new canvas.
	 * @param ImageType Type of canvas (I prefer TYPE_INT_ARGB).
	 */
	public RedCanvas(int Width, int Height, int ImageType) {
		super(Width, Height, ImageType);
	}
}

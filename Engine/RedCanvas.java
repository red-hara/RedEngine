/**
 * Do what you want 'cause a pirate is free, You are a pirate!
 * This work is licensed under the Creative Commons Attribution 4.0 International
 * License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative
 * Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 */

package Engine;

/**
 *
 * @author Red Hara (rtc6fg4.fejg2@gmail.com)
 */
public class RedCanvas extends RedImage {
    
    public RedPoint offset = new RedPoint(0, 0);
    public double zoom = 1;
    
    public RedCanvas(int Width, int Height, int ImageType) {
        super(Width, Height, ImageType);
    }  
}

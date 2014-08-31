/**
 * Do what you want 'cause a pirate is free, You are a pirate!
 * This work is licensed under the Creative Commons Attribution 4.0 International
 * License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative
 * Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 */

package Engine;

/**
 * Rectangle with coordinates, width and height.
 *
 * @author red__hara
 */
public class RedRect {
    /**
     * X coordinate of left upper corner.
     */
    public double x;
    /**
     * Y coordinate of left upper corner.
     */
    public double y;
    /**
     * Width of rectangle.
     */
    public double width;
    /**
     * Height of rectangle.
     */
    public double height;
    
    public RedRect( double X, double Y, double Width, double Height ) {
        x = X;
        y = Y;
        width = Width;
        height = Height;
    }
    
    public RedRect getCopy() {
        return new RedRect(x, y, width, height);
    }
}

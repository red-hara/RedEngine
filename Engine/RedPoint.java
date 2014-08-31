/**
 * Do what you want 'cause a pirate is free, You are a pirate!
 * This work is licensed under the Creative Commons Attribution 4.0 International
 * License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative
 * Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 */

package Engine;

/**
 * Just a point with two coordinates.
 *
 * @author red__hara
 */
public class RedPoint
{
    public double x;
    public double y;
    
    /**
     * Constructor.
     * @param X X position of point.
     * @param Y Y position of point.
     */
    public RedPoint( double X, double Y )
    {
        x = X;
        y = Y;
    }
    
    public RedPoint getCopy() {
        return new RedPoint(x, y);
    }
}


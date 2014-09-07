/**
 * Do what you want 'cause a pirate is free, You are a pirate!
 * This work is licensed under the Creative Commons Attribution 4.0 International
 * License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative
 * Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 */

package Engine;

/**
 * Class for random useful stuff.
 *
 * @author Red Hara (rtc6fg4.fejg2@gmail.com)
 */
public class RedU {

    public static double computeVelocity(double Velocity, double Acceleration, double Drag, double Max) {
        if (Acceleration != 0) {
            Velocity += Acceleration * RedG.elapsed;
        } else if (Drag != 0) {
            double drag = Drag * RedG.elapsed;
            if (Velocity - drag > 0) {
                Velocity = Velocity - drag;
            } else if (Velocity + drag < 0) {
                Velocity += drag;
            } else {
                Velocity = 0;
            }
        }
        if ((Velocity != 0) && (Max != -1)) {
            if (Velocity > Max) {
                Velocity = Max;
            } else if (Velocity < -Max) {
                Velocity = -Max;
            }
        }
        return Velocity;
    }
}

/**
 * Do what you want 'cause a pirate is free, You are a pirate!
 * This work is licensed under the Creative Commons Attribution 4.0 International
 * License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative
 * Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 */

package Engine;

/**
 * Class extends
 * <code>RedBasic</code>. Has movement system.
 *
 * @author Red Hara (rtc6fg4.fejg2@gmail.com)
 */
public class RedObject extends RedBasic {

    /**
     * X position of upper left corner.
     */
    public double x;
    /**
     * Y position of upper left corner.
     */
    public double y;
    /**
     * The width of object.
     */
    public double width;
    /**
     * The height of object.
     */
    public double height;
    /**
     * Control of auto
     * <code>UpdateMoution()</code> call.
     */
    public boolean moves;
    /**
     * The ability to move of object.
     */
    public boolean immovable;
    public int allowCollisions;
    public int touching;
    public int wasTouching;
    public double mass;
    public double elasticity;
    /**
     * The basic speed of object.
     */
    public RedPoint velocity;
    /**
     * The basic acceleration of object.
     */
    public RedPoint acceleration;
    /**
     * Speed decreasing with
     * <code>Acceleration</code> equal to zero.
     */
    public RedPoint drag;
    /**
     * Help for capping the speed with
     * <code>Acceleration</code>.
     */
    public RedPoint maxVelocity;
    /**
     * Last coordinates of object.
     */
    public RedPoint last;
    public int facing;
    public double angle = 0;

    /**
     * @param X The x coordinate.
     * @param Y The y coordinate.
     * @param Width The width of object.
     * @param Height The height of object.
     */
    public RedObject(double X, double Y, double Width, double Height) {
        x = X;
        y = Y;
        width = Width;
        height = Height;
        last = new RedPoint(x, y);

        moves = true;
        immovable = false;
        allowCollisions = RedG.ANY;
        touching = 0;
        wasTouching = 0;
        mass = 1;
        elasticity = 0;
        velocity = new RedPoint(0, 0);
        acceleration = new RedPoint(0, 0);
        drag = new RedPoint(0, 0);
        maxVelocity = new RedPoint((double) -1, (double) -1);

    }

    @Override
    public void preUpdate() {
        last.x = x;
        last.y = y;
    }

    @Override
    public void postUpdate() {
        if (moves) {
            updateMoution();
        }
        wasTouching = touching;
        touching = RedG.NONE;
    }

    @Override
    public void update() {
    }

    public void updateMoution() {
        double delta;
        double velocityDelta;

        velocityDelta = (RedU.computeVelocity(velocity.x, acceleration.x, drag.x, maxVelocity.x) - velocity.x) / 2;
        velocity.x += velocityDelta;
        delta = velocity.x * RedG.elapsed;
        velocity.x += velocityDelta;
        x += delta;

        velocityDelta = (RedU.computeVelocity(velocity.y, acceleration.y, drag.y, maxVelocity.y) - velocity.y) / 2;
        velocity.y += velocityDelta;
        delta = velocity.y * RedG.elapsed;
        velocity.y += velocityDelta;
        y += delta;
    }

    public void teleport(double X, double Y) {
        last.x = x = X;
        last.y = y = Y;
    }

    public boolean isTouching(int Direction) {
        return (touching & Direction) == Direction;
    }

    public boolean wasTouching(int Direction) {
        return (wasTouching & Direction) == Direction;
    }
}

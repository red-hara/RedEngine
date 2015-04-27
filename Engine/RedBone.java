/**
 * Do what you want 'cause a pirate is free, You are a pirate! This work is licensed under the
 * Creative Commons Attribution 4.0 International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative Commons, 171 Second
 * Street, Suite 300, San Francisco, California, 94105, USA.
 */
package Engine;

/**
 *
 * @author Red Hara (rtc6fg4.fejg2@gmail.com)
 */
public class RedBone extends RedObject {

	public RedBone parent;
	public double length;
	public double offsetAngle;

	public RedBone(RedBone Parent, double Length, double OffsetAngle) {
		super(Parent.x + Parent.width,
				Parent.y + Parent.height,
				Length * Math.cos(Parent.angle + OffsetAngle),
				Length * Math.sin(Parent.angle + OffsetAngle));
		parent = Parent;
		length = Length;
		offsetAngle = OffsetAngle;

		angle = parent.angle + offsetAngle;
	}
	
	public RedBone(double X, double Y, double Length, double OffsetAngle) {
		super(X,
				Y,
				Length * Math.cos(OffsetAngle),
				Length * Math.sin(OffsetAngle));
		length = Length;
		offsetAngle = OffsetAngle;
		angle = offsetAngle;
	}

	@Override
	public void update() {
		super.update();
		if (parent != null) {
			x = parent.x + parent.height;
			y = parent.y + parent.width;
			angle = parent.angle + offsetAngle;
		} else {
			angle = offsetAngle;
		}
		width = length * Math.sin(angle);
		height = length * Math.cos(angle);
	}
}

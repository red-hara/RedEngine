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
public class RedFlesh extends RedSprite {

	public RedBone bone;

	public RedFlesh(RedBone Bone, double Width, double Height) {
		super(Bone.x, Bone.y, Width, Height);
		bone = Bone;
		angle = bone.angle;
	}

	@Override
	public void update() {
		super.update();
		x = bone.x;
		y = bone.y;
		angle = bone.angle;
	}

}

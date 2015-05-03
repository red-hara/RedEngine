/**
 * Do what you want 'cause a pirate is free, You are a pirate! This work is licensed under the
 * Creative Commons Attribution 4.0 International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative Commons, 171 Second
 * Street, Suite 300, San Francisco, California, 94105, USA.
 */
package Engine;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Red Hara (rtc6fg4.fejg2@gmail.com)
 */
public class RedSkeleton extends RedGroup {

	public Map<String, double[][]> angles;
	public Map<String, double[][]> lengths;
	public Map<String, double[]> delays;
	public String animationCurrent;
	public int animationFrame;
	public int[] drawOrder;

	public RedMethod animationMethod = new RedMethod() {
		@Override
		public void execute() {
			RedBone bone;
			double[][] a = angles.get(animationCurrent);
			double[][] l = lengths.get(animationCurrent);
			for (int i = 0; i < members.size(); i++) {
				bone = (RedBone) members.get(i);
				if (a[animationFrame].length > 0) {
					bone.offsetAngle = a[animationFrame][i];
				}
				if (l[animationFrame].length > 0) {
					bone.length = l[animationFrame][i];
				}
			}

			animationTimer.start(delays.get(animationCurrent)[animationFrame], 0);

			animationFrame = (animationFrame < angles.get(animationCurrent).length - 1) ? animationFrame + 1 : 0;
		}
	};

	public RedTimer animationTimer = new RedTimer(animationMethod);

	public RedSkeleton() {
		super();
		angles = new HashMap<>();
		lengths = new HashMap<>();
		delays = new HashMap<>();
	}

	public void addAnimation(double[][] Angles, double[][] Lengths, double[] Delays, String AnimationName) {
		angles.put(AnimationName, Angles);
		lengths.put(AnimationName, Lengths);
		delays.put(AnimationName, Delays);
	}

	public void playAnimation(String AnimationName) {
		animationCurrent = AnimationName;
		animationFrame = angles.get(AnimationName).length - 1;
		animationMethod.execute();
	}

	@Override
	public void update() {
		super.update();
		animationTimer.update();

		if (animationCurrent != null) {
			double t = 1 - animationTimer.getTimeLeft() / animationTimer.time;
			RedBone bone;
			double[][] a = angles.get(animationCurrent);
			double[][] l = lengths.get(animationCurrent);
			double startAngle;
			double deltaAngle;
			double startLength;
			double deltaLength;
			int prev = (animationFrame + angles.get(animationCurrent).length - 1) % angles.get(animationCurrent).length;
			for (int i = 0; i < members.size(); i++) {
				bone = (RedBone) members.get(i);
				if (a[animationFrame].length > 0) {
					startAngle = a[prev][i];
					deltaAngle = a[animationFrame][i] - startAngle;
					bone.offsetAngle = startAngle + deltaAngle * t;
				}
				if (l[animationFrame].length > 0) {
					startLength = l[prev][i];
					deltaLength = l[animationFrame][i] - startLength;
					bone.length = startLength + deltaLength * t;
				}
			}
		}
	}

	@Override
	public void draw(RedCanvas Canvas) {
		if (drawOrder == null) {
			super.draw(Canvas);
		} else {
			for (int i : drawOrder) {
				members.get(i).draw(Canvas);
			}
		}
	}
}

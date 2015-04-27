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

	public RedMethod animationMethod = new RedMethod() {
		@Override
		public void execute() {
			RedBone bone;
			double[][] a = angles.get(animationCurrent);
			double[][] l = angles.get(animationCurrent);
			for (int i = 0; i < members.size(); i++) {
				bone = (RedBone) members.get(i);
				if (a[animationFrame].length > i) {
					bone.offsetAngle = a[animationFrame][i];
				}
				if (l[animationFrame].length > i) {
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
		animationTimer.update();
	}
}

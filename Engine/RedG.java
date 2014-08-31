/**
 * Do what you want 'cause a pirate is free, You are a pirate! This work is licensed under the
 * Creative Commons Attribution 4.0 International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative Commons, 171 Second
 * Street, Suite 300, San Francisco, California, 94105, USA.
 */
package Engine;

import Engine.system.RedKeyboard;
import Engine.system.RedMouse;
import java.awt.GraphicsEnvironment;

/**
 * Useful class for Global usage.
 *
 * @author red__hara
 */
public class RedG {

	public static final byte UP = 0b1000;
	public static final byte RIGHT = 0b0100;
	public static final byte DOWN = 0b0010;
	public static final byte LEFT = 0b0001;
	public static final byte NONE = 0b0000;
	public static final byte ANY = 0b1111;
	public static final byte OVERLAP_BIAS = 4;
	public static double globalSeed;
	/**
	 * Represents time passed since last frame.
	 */
	public static double elapsed;
	/**
	 * Background color of canvas.
	 */
	public static int bgColor = 0xff000000;
	/**
	 * Position of screen left upper corner in world.
	 */
	public static RedPoint screen = new RedPoint(0, 0);
	/**
	 * Reference to <code>RedKeyboard</code>.
	 */
	public static RedKeyboard keys = new RedKeyboard();
	/**
	 * Reference to <code>RedMouse</code>.
	 */
	public static RedMouse mouse = new RedMouse();

	/**
	 * Function for switching to another state.
	 *
	 * @param State State to switch to.
	 */
	public static void switchState(RedGroup State) {
		RedGame.state = null;
		State.create();
		RedGame.state = State;
	}

	public static boolean collide(RedBasic Basic1, RedBasic Basic2) {
		boolean collided = false;

		if (Basic1 instanceof RedGroup) {
			RedGroup group;
			group = (RedGroup) Basic1;
			for (RedBasic member : group.members) {
				collided = collide(member, Basic2) || collided;
			}
		} else if (Basic2 instanceof RedGroup) {
			collided = collide(Basic2, Basic1);
		} else if (Basic1 instanceof RedTilemap) {
			collided = ((RedTilemap) Basic1).collide((RedObject) Basic2);
		} else if (Basic2 instanceof RedTilemap) {
			collided = ((RedTilemap) Basic2).collide((RedObject) Basic1);
		} else {
			boolean separatedX = separateX((RedObject) Basic1, (RedObject) Basic2);
			boolean separatedY = separateY((RedObject) Basic1, (RedObject) Basic2);
			collided = separatedX || separatedY;
		}
		return collided;
	}

	/**
	 *
	 * @param Basic1 First basic (<code>RedBasic</code>, <code>RedGroup</code> and etc.).
	 * @param Basic2 Second basic (<code>RedBasic</code>, <code>RedGroup</code> and etc.).
	 * @return if they overlaps anywhere.
	 */
	public static boolean overlap(RedBasic Basic1, RedBasic Basic2) {
		boolean overlapFound = false;
		if (Basic2 instanceof RedGroup) {
			overlapFound = overlap(Basic2, Basic1);
		} else if (Basic1 instanceof RedGroup) {
			RedGroup group;
			group = (RedGroup) Basic1;
			for (RedBasic member : group.members ) {
				if (overlap(Basic2, member)) {
					overlapFound = true;
				}
			}
		} else if (Basic1 instanceof RedTilemap) {
			overlapFound = ((RedTilemap) Basic1).overlap((RedObject) Basic2);
		} else if (Basic2 instanceof RedTilemap) {
			overlapFound = ((RedTilemap) Basic2).overlap((RedObject) Basic1);
		} else if (Basic1.exist && Basic2.exist) {
			RedObject object1, object2;
			object1 = (RedObject) Basic1;
			object2 = (RedObject) Basic2;
			boolean overlapX, overlapY;
			overlapX = overlapY = false;
			if (object1.x > object2.x) {
				if (object1.x < object2.x + object2.width) {
					overlapX = true;
				}
			} else if (object1.x + object1.width > object2.x) {
				overlapX = true;
			}

			if (object1.y > object2.y) {
				if (object1.y < object2.y + object2.height) {
					overlapY = true;
				}
			} else if (object1.y + object1.height > object2.y) {
				overlapY = true;
			}

			overlapFound = overlapX && overlapY;
		}

		return overlapFound;
	}

	private static boolean separateX(RedObject Object1, RedObject Object2) {

		boolean obj1immovable = Object1.immovable;
		boolean obj2immovable = Object2.immovable;
		if (Object1.immovable && Object2.immovable) {
			return false;
		}

		double overlap = 0;
		double obj1delta = Object1.x - Object1.last.x;
		double obj2delta = Object2.x - Object2.last.x;

		if (obj1delta != obj2delta) {
			double obj1deltaAbs = Math.abs(obj1delta);
			double obj2deltaAbs = Math.abs(obj2delta);
			RedRect obj1rect = new RedRect(Object1.x - ((obj1delta > 0) ? obj1delta : 0), Object1.last.y, Object1.width + ((obj1delta > 0) ? obj1delta : -obj1delta), Object1.height);
			RedRect obj2rect = new RedRect(Object2.x - ((obj2delta > 0) ? obj2delta : 0), Object2.last.y, Object2.width + ((obj2delta > 0) ? obj2delta : -obj2delta), Object2.height);
			if ((obj1rect.x + obj1rect.width > obj2rect.x) && (obj1rect.x < obj2rect.x + obj2rect.width) && (obj1rect.y + obj1rect.height > obj2rect.y) && (obj1rect.y < obj2rect.y + obj2rect.height)) {
				double maxOverlap = obj1deltaAbs + obj2deltaAbs + OVERLAP_BIAS;

				if (obj1delta > obj2delta) {
					overlap = Object1.x + Object1.width - Object2.x;

					if ((overlap > maxOverlap) || (Object1.allowCollisions & RIGHT) != RIGHT || (Object2.allowCollisions & LEFT) != LEFT) {
						overlap = 0;
					} else {
						Object1.touching |= RIGHT;
						Object2.touching |= LEFT;
					}
				} else if (obj1delta < obj2delta) {

					overlap = Object1.x - Object2.width - Object2.x;
					if ((-overlap > maxOverlap) || (Object1.allowCollisions & LEFT) != LEFT || (Object2.allowCollisions & RIGHT) != RIGHT) {
						overlap = 0;
					} else {
						Object1.touching |= LEFT;
						Object2.touching |= RIGHT;
					}
				}
				if (overlap != 0) {
					double obj1v = Object1.velocity.x;
					double obj2v = Object2.velocity.x;

					if (!obj1immovable && !obj2immovable) {
						overlap *= 0.5;
						Object1.x = Object1.x - overlap;
						Object2.x += overlap;

						double obj1velocity = Math.sqrt((obj2v * obj2v * Object2.mass) / Object1.mass) * ((obj2v > 0) ? 1 : -1);
						double obj2velocity = Math.sqrt((obj1v * obj1v * Object1.mass) / Object2.mass) * ((obj1v > 0) ? 1 : -1);
						double average = (obj1velocity + obj2velocity) / 2;
						obj1velocity -= average;
						obj2velocity -= average;
						Object1.velocity.x = average + obj1velocity * Object1.elasticity;
						Object2.velocity.x = average + obj2velocity * Object2.elasticity;
					} else if (!obj1immovable) {
						Object1.x = Object1.x - overlap;
						Object1.velocity.x = obj2v - obj1v * Object1.elasticity;
					} else if (!obj2immovable) {
						Object2.x += overlap;
						Object2.velocity.x = obj1v - obj2v * Object2.elasticity;
					}
					return true;
				} else {
					return false;
				}
			}
		}

		return false;
	}

	private static boolean separateY(RedObject Object1, RedObject Object2) {
		boolean obj1immovable = Object1.immovable;
		boolean obj2immovable = Object2.immovable;
		if (Object1.immovable && Object2.immovable) {
			return false;
		}

		double overlap = 0;
		double obj1delta = Object1.y - Object1.last.y;
		double obj2delta = Object2.y - Object2.last.y;
		if (obj1delta != obj2delta) {
			double obj1deltaAbs = Math.abs(obj1delta);
			double obj2deltaAbs = Math.abs(obj2delta);
			RedRect obj1rect = new RedRect(Object1.x, Object1.y - ((obj1delta > 0) ? obj1delta : 0), Object1.width, Object1.height + obj1deltaAbs);
			RedRect obj2rect = new RedRect(Object2.x, Object2.y - ((obj2delta > 0) ? obj2delta : 0), Object2.width, Object2.height + obj2deltaAbs);
			if ((obj1rect.x + obj1rect.width > obj2rect.x) && (obj1rect.x < obj2rect.x + obj2rect.width) && (obj1rect.y + obj1rect.height > obj2rect.y) && (obj1rect.y < obj2rect.y + obj2rect.height)) {
				double maxOverlap = obj1deltaAbs + obj2deltaAbs + OVERLAP_BIAS;

				//If they did overlap (and can), figure out by how much and flip the corresponding flags
				if (obj1delta > obj2delta) {
					overlap = Object1.y + Object1.height - Object2.y;
					if ((overlap > maxOverlap) || (Object1.allowCollisions & DOWN) != DOWN || (Object2.allowCollisions & UP) != UP) {
						overlap = 0;
					} else {
						Object1.touching |= DOWN;
						Object2.touching |= UP;
					}
				} else if (obj1delta < obj2delta) {
					overlap = Object1.y - Object2.height - Object2.y;
					if ((-overlap > maxOverlap) || (Object1.allowCollisions & UP) != UP || (Object2.allowCollisions & DOWN) != DOWN) {
						overlap = 0;
					} else {
						Object1.touching |= UP;
						Object2.touching |= DOWN;
					}
				}
			}
		}

		if (overlap != 0) {
			double obj1v = Object1.velocity.y;
			double obj2v = Object2.velocity.y;

			if (!obj1immovable && !obj2immovable) {
				overlap *= 0.5;
				Object1.y = Object1.y - overlap;
				Object2.y += overlap;

				double obj1velocity = Math.sqrt((obj2v * obj2v * Object2.mass) / Object1.mass) * ((obj2v > 0) ? 1 : -1);
				double obj2velocity = Math.sqrt((obj1v * obj1v * Object1.mass) / Object2.mass) * ((obj1v > 0) ? 1 : -1);
				double average = (obj1velocity + obj2velocity) * 0.5;
				obj1velocity -= average;
				obj2velocity -= average;
				Object1.velocity.y = average + obj1velocity * Object1.elasticity;
				Object2.velocity.y = average + obj2velocity * Object2.elasticity;
			} else if (!obj1immovable) {
				Object1.y = Object1.y - overlap;
				Object1.velocity.y = obj2v - obj1v * Object1.elasticity;
				//This is special case code that handles cases like horizontal moving platforms you can ride
				if (Object2.moves && (obj1delta > obj2delta)) {
					Object1.x += Object2.x - Object2.last.x;
				}
			} else if (!obj2immovable) {
				Object2.y += overlap;
				Object2.velocity.y = obj1v - obj2v * Object2.elasticity;
				//This is special case code that handles cases like horizontal moving platforms you can ride
				if (Object1.moves && (obj1delta < obj2delta)) {
					Object2.x += Object1.x - Object1.last.x;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public static double srandom(double Seed) {
		return Math.abs(((1013904223 * (Seed + 1)) % 1664525) / 1664525);
	}

	public static double random() {
		globalSeed = srandom(globalSeed);
		return globalSeed;
	}

	public static void setFullscreen(boolean Fullscreen) {
		if (Fullscreen) {
			RedGame.getWindow().dispose();
			RedGame.getWindow().setUndecorated(true);
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(RedGame.getWindow());
			RedGame.getWindow().x = RedGame.getWindow().y = 0;
		} else {
			RedGame.getWindow().dispose();
			RedGame.setWindow(
					(int) (RedGame.width * RedGame.getWindow().canvas.zoom),
					(int) (RedGame.height * RedGame.getWindow().canvas.zoom),
					RedGame.getWindow().getTitle(),
					true,
					RedGame.getWindow().getBackground().getRGB() + 0x1000000 * RedGame.getWindow().getBackground().getAlpha(),
					RedGame.getWindow().displyasZoom);
		}
		RedGame.getWindow().requestFocus();
	}

	public static void setWindowBg(int Color) {
		RedGame.getWindow().setBackground(new java.awt.Color(Color, true));
		RedGame.getWindow().requestFocus();
	}
}

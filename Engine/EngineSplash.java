/**
 * Do what you want 'cause a pirate is free, You are a pirate! This work is licensed under the
 * Creative Commons Attribution 4.0 International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative Commons, 171 Second
 * Street, Suite 300, San Francisco, California, 94105, USA.
 */
package Engine;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Class for displaying splash.
 * Override <code>stateSwitch()</code> to change state (add <code>RedG.switchState()</code> for
 * example).
 *
 * @author Red Hara (rtc6fg4.fejg2@gmail.com)
 */
public class EngineSplash extends RedGroup {

	/**
	 * Sprite to display stylish engine symbol.
	 */
	public RedSprite engineSymbol = new RedSprite(8, 8, 40, 35);
	/**
	 * Sprite to display stylish engine name.
	 */
	public RedSprite engineName = new RedSprite(181, 73, 72, 52);
	/**
	 * Counter to work with blackout.
	 */
	public double timer = 0;
	/**
	 * Duration of blackout in seconds.
	 */
	public double darknessDuration = 1;
	/**
	 * Darkness level (from 0 to 1).
	 */
	public double darkness = 1;
	/**
	 * Color of darkness in hex.
	 */
	public int darknessColor = 0x000000;
	/**
	 * Duration of splash without darkness.
	 */
	public double clearDuration = 3;
	/**
	 * 
	 */

	/**
	 * Constructor.
	 */
	public EngineSplash() {
		super();
		RedG.bgColor = 0xffcc0000;

		engineName.loadImage(RedImage.loadImage("/Engine/system/data/EngineName.png"), 72, 52);
		engineSymbol.loadImage(RedImage.loadImage("/Engine/system/data/EngineSymbol.png"), 40, 35);
		engineSymbol.addAnimation(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, 0);
		engineSymbol.playAnimation(0, 0.1);
		add(engineSymbol);
		add(engineName);
		if (256 / RedGame.width > 128 / RedGame.height) {
			engineSymbol.zoom = engineName.zoom = (double) RedGame.getWindow().width / RedGame.width;
		} else {
			engineSymbol.zoom = engineName.zoom = (double) RedGame.getWindow().height / RedGame.height;
		}
		engineName.x = RedGame.width / engineName.zoom * RedGame.getWindow().canvas.zoom * RedGame.getWindow().displayZoom - engineName.width;
		engineName.y = RedGame.height / engineName.zoom * RedGame.getWindow().canvas.zoom * RedGame.getWindow().displayZoom - engineName.height;
	}

	/**
	 * Update of splash. Will call <code>stateSwitch</code> when <code>timer</code> is more than
	 * <code>2 * darknessDuration + clearDuration</code>.
	 */
	@Override
	public void update() {
		super.update();

		timer += RedG.elapsed;
		if (timer < darknessDuration) {
			darkness = 1 - timer / darknessDuration;
		}
		if (timer > darknessDuration && timer < darknessDuration + clearDuration) {
			darkness = 0;
		}
		if (timer > darknessDuration + clearDuration) {
			darkness = (timer - darknessDuration - clearDuration) / darknessDuration;
		}
		if (timer >= 2 * darknessDuration + clearDuration) {
			stateSwitch();
		}

		if (timer < darknessDuration + clearDuration && (RedG.keys.justPressed(' ') || RedG.keys.justPressed('\n') || RedG.keys.justPressed(RedG.keys.ESC))) {
			timer = darknessDuration + clearDuration;
		}

		engineSymbol.getFrame().noiseHSB(0, 0, engineSymbol.frameWidth, engineSymbol.frameHeight, 0.025f, 0, 0, RedG.random());

	}

	/**
	 * Draws splash.
	 * @param Canvas <code>RedCanvas</code> to draw on.
	 */
	@Override
	public void draw(RedCanvas Canvas) {
		super.draw(Canvas);
		Graphics g = Canvas.getGraphics();
		g.setColor(new Color((((int) (0xff * Math.min(darkness, 1))) * 0x1000000 + darknessColor), true));
		g.fillRect(0, 0, Canvas.width, Canvas.height);
	}

	/**
	 * Method is called when <code>timer</code> is more than <code>2 * darknessDuration +
	 * clearDuration</code>.
	 * Override this to switch to new state.
	 */
	public void stateSwitch() {

	}
}

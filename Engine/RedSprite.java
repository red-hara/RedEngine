/**
 * Do what you want 'cause a pirate is free, You are a pirate! This work is licensed under the
 * Creative Commons Attribution 4.0 International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative Commons, 171 Second
 * Street, Suite 300, San Francisco, California, 94105, USA.
 */
package Engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Class extends <code>RedObject</code>. Has image and animation.
 *
 * @author Red Hara (rtc6fg4.fejg2@gmail.com)
 */
public class RedSprite extends RedObject {

	public RedImage finalFrame;
	public RedImage currentFrame;
	public RedImage frames;
	public Map<String, int[]> animations;
	public String animationCurrent;
	public int animationFrame;
	public int frameWidth;
	public int frameHeight;
	public RedPoint offset;
	public RedPoint scrollFactor;
	public RedPoint canvasDelta;
	public RedPoint origin;
	public double zoom = 0;
	public double displayZoom = 1;
	public float hue = 0;
	public float brightness = 0;
	public float saturation = 0;
	public boolean dirty = false;
	public RedMethod animationMethod = new RedMethod() {
		@Override
		public void execute() {
			currentFrame = frames.getSubImage(frameWidth * animations.get(animationCurrent)[animationFrame], 0, frameWidth, frameHeight);
			animationFrame = (animationFrame < animations.get(animationCurrent).length - 1) ? animationFrame + 1 : 0;
			dirty = true;
		}
	};
	public RedTimer animationTimer = new RedTimer(animationMethod);

	public RedSprite(double X, double Y, double Width, double Height) {
		super(X, Y, Width, Height);
		currentFrame = new RedImage((int) Math.max(Width, 1), (int) Math.max(Height, 1), RedImage.TYPE_INT_ARGB);
		animations = new HashMap<>();
		offset = new RedPoint(0, 0);
		scrollFactor = new RedPoint(1, 1);
		canvasDelta = new RedPoint(0, 0);
		dirty = true;
	}

	@Override
	public void update() {
		super.update();
		animationTimer.update();
	}

	@Override
	public void draw(RedCanvas Canvas) {
		if (visible) {
			if (dirty) {
				updateFrame();
			}
			double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
			int w = (int) (finalFrame.width + 2 * Math.abs(-origin.x + finalFrame.width / 2));
			int h = (int) (finalFrame.height + 2 * Math.abs(-origin.y + finalFrame.height / 2));
			int newW = (int) Math.floor(w * cos + h * sin);
			int newH = (int) Math.floor(h * cos + w * sin);

			RedPoint leftUpperCorner = new RedPoint((int) (Math.floor((x - RedG.screen.x * scrollFactor.x + offset.x + Canvas.offset.x + canvasDelta.x - (newW - w) / 2)) * ((zoom == 0) ? Canvas.zoom : zoom)),
					(int) (Math.floor((y - RedG.screen.y * scrollFactor.y + offset.y + Canvas.offset.y + canvasDelta.y - (newH - h) / 2)) * ((zoom == 0) ? Canvas.zoom : zoom)));
			RedPoint size = new RedPoint((int) (Math.floor(newW * ((zoom == 0) ? Canvas.zoom : zoom))),
					(int) (Math.floor(newH * ((zoom == 0) ? Canvas.zoom : zoom))));

			if (leftUpperCorner.x < Canvas.width
					&& leftUpperCorner.y < Canvas.height
					&& leftUpperCorner.x + size.x > 0
					&& leftUpperCorner.y + size.y > 0) {

				GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
						.getDefaultScreenDevice()
						.getDefaultConfiguration();

				BufferedImage result = gc.createCompatibleImage(newW, newH, Transparency.TRANSLUCENT);
				Graphics2D g = (Graphics2D) result.getGraphics();
				g.translate((newW - origin.x * 2) / 2, (newH - origin.y * 2) / 2);
				g.rotate(angle, origin.x, origin.y);
				g.drawRenderedImage(finalFrame, null);
				g.dispose();
				Canvas.getGraphics().drawImage(result,
						(int) (leftUpperCorner.x + size.x / 2 - size.x / 2 * displayZoom),
						(int) (leftUpperCorner.y + size.y / 2 - size.y / 2 * displayZoom),
						(int) (size.x * displayZoom),
						(int) (size.y * displayZoom),
						null);
				if (RedGame.debug) {
					Graphics debugGraphics = Canvas.getGraphics();
					debugGraphics.setColor(new Color(0x80ff0000, true));
					debugGraphics.drawRect(
							(int) (leftUpperCorner.x + size.x / 2 - size.x / 2 * displayZoom),
							(int) (leftUpperCorner.y + size.y / 2 - size.y / 2 * displayZoom),
							(int) (size.x * displayZoom),
							(int) (size.y * displayZoom)
					);
					debugGraphics.setColor(new Color(0x8000ff00, true));
					debugGraphics.drawRect(
							(int) (Math.floor((x - RedG.screen.x * scrollFactor.x))),
							(int) (Math.floor((y - RedG.screen.y * scrollFactor.y))),
							(int) (Math.floor(width * Canvas.zoom + 0.5)),
							(int) (Math.floor(height * Canvas.zoom + 0.5))
					);

				}
			}
		}
	}

	public void loadImage(RedImage Image, int Width, int Height) {
		frameWidth = Width;
		frameHeight = Height;
		frames = new RedImage(Image.getWidth(), Image.getHeight(), RedImage.TYPE_INT_ARGB);
		frames.setData(Image.getRaster());
		currentFrame = new RedImage(Width, Height, RedImage.TYPE_INT_ARGB);
		currentFrame = Image.getSubImage(0, 0, Width, Height);
		origin = new RedPoint(Width / 2, Height / 2);
		updateFrame();
	}

	public void addAnimation(int[] AnimationCode, String AnimationName) {

		animations.put(AnimationName, AnimationCode);
	}

	public void playAnimation(String AnimationName, double Delay) {
		if (AnimationName != animationCurrent) {
			animationFrame = 0;
			animationCurrent = AnimationName;
			animationTimer.method.execute();
			dirty = true;
		}
		if (animationTimer.getDelay() != Delay) {
			animationTimer.start(Delay, -1);
			dirty = true;
		}
	}

	public void makeGraphic(int Width, int Height, int Color) {
		frameWidth = Width;
		frameHeight = Height;
		if (currentFrame == null) {
			currentFrame = new RedImage(Width, Height, RedImage.TYPE_INT_ARGB);
		}
		if (currentFrame.getWidth() != Width || currentFrame.getHeight() != Height) {
			currentFrame = new RedImage(Width, Height, RedImage.TYPE_INT_ARGB);
		}
		((RedImage) currentFrame).fillRect(0, 0, Width, Height, Color);
		updateFrame();
		origin = new RedPoint(Width / 2, Height / 2);
	}

	public void updateFrame() {
		finalFrame = currentFrame.getCopy();
		finalFrame.setHSB(0, 0, finalFrame.width, finalFrame.height, hue, saturation, brightness);
		dirty = false;
	}

	public RedImage getFrame() {
		dirty = true;
		return currentFrame;
	}
}

/**
 * Do what you want 'cause a pirate is free, You are a pirate! This work is licensed under the
 * Creative Commons Attribution 4.0 International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative Commons, 171 Second
 * Street, Suite 300, San Francisco, California, 94105, USA.
 */
package Engine;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 * I suggest you use this code only for a limited canvas works. For full screen use
 * <code>RedGroup</code> instead.
 *
 * @author Red Hara (rtc6fg4.fejg2@gmail.com)
 */
public class RedState extends RedGroup {

	public double x;
	public double y;
	public double width;
	public double height;
	public double angle;
	public RedPoint scrollFactor = new RedPoint(0, 0);
	public double zoom = 0;
	public int bgColor = 0x0;
	public RedCanvas stateCanvas;

	public RedState(double X, double Y, double Width, double Height, double Zoom) {
		x = X;
		y = Y;
		width = Width;
		height = Height;
		stateCanvas = new RedCanvas((int) (Width * Zoom), (int) (Height * Zoom), RedImage.TYPE_INT_ARGB);
		stateCanvas.zoom = Zoom;
	}

	@Override
	public void draw(RedCanvas Canvas) {
		drawBg();
		drawOnCanvas();
		drawState(Canvas);
	}

	public void drawBg() {
//		Graphics graphics = stateCanvas.getGraphics();
//		graphics.setColor(new Color(bgColor, true));
//		graphics.fillRect(0, 0, (int) (stateCanvas.width), (int) (stateCanvas.height));
		stateCanvas.fillRect(0, 0, stateCanvas.width, stateCanvas.height, bgColor);

	}

	public void drawOnCanvas() {
		super.draw(stateCanvas);
	}

	public void drawState(RedCanvas Canvas) {
		
		double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
		int w = stateCanvas.getWidth();
		int h = stateCanvas.getHeight();
		int newW = (int) Math.floor(w * cos + h * sin);
		int newH = (int) Math.floor(h * cos + w * sin);
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice()
				.getDefaultConfiguration();

		BufferedImage result = gc.createCompatibleImage(newW, newH, Transparency.TRANSLUCENT);
		Graphics2D g = result.createGraphics();
		g.translate((newW - w) / 2, (newH - h) / 2);
		g.rotate(angle, w / 2, h / 2);
		g.drawRenderedImage(stateCanvas, null);
		g.dispose();

		RedPoint leftUpperCorner = new RedPoint((int) (Math.floor((x - RedG.screen.x * scrollFactor.x + Canvas.offset.x - (newW - w) / 2)) * ((zoom == 0) ? Canvas.zoom : zoom)),
				(int) (Math.floor((y - RedG.screen.y * scrollFactor.y + Canvas.offset.y - (newH - h) / 2)) * ((zoom == 0) ? Canvas.zoom : zoom)));
		RedPoint size = new RedPoint((int) (Math.floor(result.getWidth() * ((zoom == 0) ? Canvas.zoom : zoom))),
				(int) (Math.floor(result.getHeight() * ((zoom == 0) ? Canvas.zoom : zoom))));
		
		Canvas.getGraphics().drawImage(result,
				(int) leftUpperCorner.x,
				(int) leftUpperCorner.y,
				(int) (size.x),
				(int) (size.y),
				null);
	}
}

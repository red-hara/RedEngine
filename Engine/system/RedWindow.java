/**
 * Do what you want 'cause a pirate is free, You are a pirate! This work is licensed under the
 * Creative Commons Attribution 4.0 International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative Commons, 171 Second
 * Street, Suite 300, San Francisco, California, 94105, USA.
 */
package Engine.system;

import Engine.RedCanvas;
import Engine.RedG;
import Engine.RedGame;
import Engine.RedImage;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

/**
 *
 * @author Red Hara (rtc6fg4.fejg2@gmail.com)
 */
public class RedWindow extends JFrame {

	/**
	 * Size of the window. Contains both width and height.
	 */
	public Dimension size;

	public double x;
	public double y;
	public double width;
	public double height;
	public double proportion;
	public double displayZoom;
	public RedCanvas canvas;
	public boolean resizableCanvas = true;
	public BufferStrategy bufferStrategy;

	public RedWindow(int Width, int Height, double Zoom, String Title, boolean Decorated, int WindowColor, double DisplayZoom) {
		setUndecorated(!Decorated);
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		if (Decorated) {
			size = new Dimension((int) (Width * Zoom * DisplayZoom + getInsets().left + getInsets().right), (int) (Height * Zoom * DisplayZoom + getInsets().top + getInsets().bottom));
		} else {
			size = new Dimension((int) (Width * Zoom * DisplayZoom), (int) (Height * Zoom * DisplayZoom));
		}
		canvas = new RedCanvas((int) (Width * Zoom), (int) (Height * Zoom), RedImage.TYPE_INT_ARGB);
		canvas.zoom = Zoom;
		setSize(size);
		setTitle(Title);
		setName(Title);

		x = (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - Width * Zoom * DisplayZoom) / 2;
		y = (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - Height * Zoom * DisplayZoom) / 2;
		width = Width;
		height = Height;
		displayZoom = DisplayZoom;
		setLocation((int) x, (int) y);

		proportion = width / height;

		addKeyListener(RedG.keys);
		addMouseListener(RedG.mouse);

		setBackground(new Color(WindowColor, true));
		
		createBufferStrategy(2);
		bufferStrategy = getBufferStrategy();
	}

	@Override
	public void repaint() {
		if (canvas != null && RedGame.state != null) {
			canvas.fillRect(0, 0, canvas.width, canvas.height, 0);
			RedGame.state.draw(canvas);
		}

		Graphics2D g2d = (Graphics2D) bufferStrategy.getDrawGraphics();
		g2d.clearRect(0, 0, getContentPane().getWidth(), getContentPane().getHeight());
		g2d.setComposite(AlphaComposite.Src);

		if (RedGame.scaleType == RedGame.NOSCALE) {
			g2d.drawImage(canvas, getInsets().top, getInsets().left, null);
		}
		if (RedGame.scaleType == RedGame.FILL) {
			g2d.drawImage(canvas,
					getInsets().top,
					getInsets().left,
					getWidth(),
					getHeight(),
					null);
		}
		if (RedGame.scaleType == RedGame.PROPOTION) {
			double propX = (getContentPane().getWidth()) / (double) canvas.width;
			double propY = (getContentPane().getHeight()) / (double) canvas.height;
			if (propX <= propY) {
				g2d.drawImage(
						canvas,
						getInsets().left,
						(getContentPane().getHeight()) / 2 - (int) (canvas.height * propX) / 2 + getInsets().top,
						getContentPane().getWidth(),
						(int) (canvas.height * propX),
						null);
			} else {
				g2d.drawImage(
						canvas,
						(getContentPane().getWidth()) / 2 - (int) (canvas.width * propY) / 2 + getInsets().top,
						getInsets().top,
						(int) (canvas.width * propY),
						getContentPane().getHeight(),
						null);
			}
		}
		g2d.dispose();
		
		bufferStrategy.show();
	}
}

/**
 * Do what you want 'cause a pirate is free, You are a pirate!
 * This work is licensed under the Creative Commons Attribution 4.0 International
 * License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative
 * Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 */

package Engine;

import Engine.system.RedSoundSystem;
import Engine.system.RedWindow;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Timer;

/**
 * Basic game class,handles update and etc.
 *
 * @author red__hara
 */
public class RedGame {

	public static RedSoundSystem sound;
	
    public static float timeMultyplyer = 1;
    /**
     * Time of start.
     */
    public long beginingTime;
    /**
     * Timer to call <code>Update()</code>.
     */
    public static Timer updateTimer;
    /**
     * ActionListener to call <code>Update()</code>.
     */
    public boolean isFocused;
    public static boolean canBePaused = true;
    public static boolean isPaused = false;
    public ActionListener updateTask = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            System.gc();
            RedG.elapsed = ((double) (System.currentTimeMillis() - lastTime) / 1000) * timeMultyplyer;
            lastTime = System.currentTimeMillis();
            isFocused = false;
            for (RedWindow window : _windowList) {
                if (window.isFocused()) {
                    isFocused = true;
                }
            }
            isPaused = !isFocused;
            if (!isPaused || !canBePaused) {

                state.update();
                RedG.keys.update();
                RedG.mouse.update();
            }
        }
    };
    /**
     * Timer to call <code>Draw()</code>.
     */
    public Timer drawTimer;
    /**
     * ActionListener to call <code>Draw()</code>.
     */
    public ActionListener drawTask = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {

            for (RedWindow window : _windowList) {
                if (scaleType == PROPOTION) {
                    if (window.resizableCanvas && window.getContentPane().getWidth() > 0 && window.getContentPane().getHeight() > 0) {
                        if (window.width != window.getContentPane().getWidth() || window.height != window.getContentPane().getHeight()) {
                            window.width = window.getContentPane().getWidth();
                            window.height = window.getContentPane().getHeight();
                            if (window.width / window.height > window.proportion) {
                                window.canvas = new RedCanvas((int) Math.round(window.height * window.proportion), (int) window.height, RedCanvas.TYPE_INT_ARGB);
                            } else {
                                window.canvas = new RedCanvas((int) window.width, (int) Math.round(window.width / window.proportion), RedCanvas.TYPE_INT_ARGB);
                            }
                            window.canvas.zoom = window.canvas.width/width;
                        }
                    }
                }

                window.canvas.fillRect(0, 0, window.canvas.width, window.canvas.height, 0);

                Graphics windowGraphics = window.canvas.getGraphics();
                windowGraphics.setColor(new Color(RedG.bgColor, true));
                windowGraphics.fillRect(0, 0, window.canvas.width, window.canvas.height);

                window.panel.repaint();

            }
        }

    };
    /**
     * Screen width in texels.
     */
    public static double width;
    /**
     * Screen height in texels.
     */
    public static double height;
    public static double angle;
    /**
     * State to be updated and drawn.
     */
    public static RedGroup state;
    /**
     * Main mainWindow of game.
     */
    public static final int NOSCALE = 0;
    public static final int FILL = 1;
    public static final int PROPOTION = 2;
    public static int scaleType = NOSCALE;
    public static boolean debug = false;
    private static RedWindow[] _windowList;
    /**
     * Time in the start of step.
     */
    public long lastTime;

    public RedGame(int Width, int Height, RedGroup InitialState, double Zoom, int UpdateRate, int DrawRate, String Title, boolean Decorated, int WindowColor, double DisplayZoom) {

        RedG.elapsed = 0;
        RedG.globalSeed = Math.random();

        width = Width;
        height = Height;

        state = InitialState;

        _windowList = new RedWindow[1];
        _windowList[0] = new RedWindow((int) (width), (int) (height), Zoom, Title, Decorated, WindowColor, DisplayZoom);
        if (_windowList[0].isDisplayable()) {
        }

        updateTimer = new Timer(UpdateRate, updateTask);
        drawTimer = new Timer(DrawRate, drawTask);
        setWindowIcon("/Engine/system/data/RedIcon.png");
		
		sound = new RedSoundSystem();
		sound.start();
    }

    public final void start() {
        state.create();
		
		try {
			RedG.fontPixie = Font.createFont(Font.PLAIN, RedText.class.getResourceAsStream("/Engine/system/data/pixie.ttf"));
		} catch (FontFormatException | IOException ex) {
			Logger.getLogger(RedGame.class.getName()).log(Level.SEVERE, null, ex);
		}

        _windowList[0].setVisible(true);
        lastTime = System.currentTimeMillis();
        updateTimer.start();
        drawTimer.start();
    }

    public final void setWindowIcon(String IconPath) {
        try {
            _windowList[0].setIconImage(ImageIO.read(getClass().getResource(IconPath)));
        } catch (IOException Exception) {
            System.err.println(Exception);
        }
    }

    public void setCursor(RedImage NewCursor) {
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(NewCursor, new Point(0, 0), "blank");
        _windowList[0].setCursor(cursor);
    }

    /*public void drawPause(Graphics g) {
     g.setColor(new Color(0x80000000, true));
     g.fillRect( 0, 0, (int) (width * zoom), (int) (height * zoom));
     g.setColor(new Color(0x80ffffff, true));
     g.fillRect((int) ((width - Math.min(width, height) / 3) * zoom / 2), (int) ((height - Math.min(width, height) / 3) * zoom / 2), (int) ((Math.min(width, height)) * zoom / 3 * 2 / 5), (int) ((Math.min(width, height)) * zoom / 3));
     g.fillRect((int) ((width - Math.min(width, height) / 3) * zoom / 2) + (int) ((Math.min(width, height)) * zoom / 3 * 3 / 5), (int) ((height - Math.min(width, height) / 3) * zoom / 2), (int) ((Math.min(width, height)) * zoom / 3 * 2 / 5), (int) ((Math.min(width, height)) * zoom / 3));
     }*/
    public static RedWindow getWindow() {
        return _windowList[0];
    }

    public static void setWindow(int Width, int Height, String Title, boolean Decorated, int WindowColor, double DisplayZoom) {
        _windowList[0] = new RedWindow(Width, Height, _windowList[0].canvas.zoom,  Title, Decorated, WindowColor, DisplayZoom);
    }

    public static void addWindow(RedWindow Window) {
        for (int i = 0; i < _windowList.length; i++) {
            if (_windowList[i] == null) {
                _windowList[i] = Window;
                return;
            }
        }
        RedWindow oldWindow[];
        oldWindow = _windowList;
        _windowList = new RedWindow[_windowList.length + 1];
        System.arraycopy(oldWindow, 0, _windowList, 0, oldWindow.length);
        _windowList[ _windowList.length - 1] = Window;
    }
}

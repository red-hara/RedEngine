	/**
 * Do what you want 'cause a pirate is free, You are a pirate!
 * This work is licensed under the Creative Commons Attribution 4.0 International
 * License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative
 * Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 */

package Engine.system;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author Red Hara (rtc6fg4.fejg2@gmail.com)
 */
public class RedKeyboard extends KeyAdapter {

    public final int BACKSPACE = 8;
    public final int SHIFT = 16;
    public final int ESC = 27;
    public final int UP = 38;
    public final int LEFT = 37;
    public final int DOWN = 40;
    public final int RIGHT = 39;
    private boolean currentPressed[] = new boolean[256];
    private boolean lastPressed[] = new boolean[256];
    private boolean typed[] = new boolean[256];

    @Override
    public void keyTyped(KeyEvent Event) {
        if ((int) Event.getKeyChar() < typed.length) {
            typed[(int) Event.getKeyChar()] = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent Event) {
        if (Event.getKeyCode() < currentPressed.length) {
            currentPressed[ Event.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent Event) {
        if (Event.getKeyCode() < currentPressed.length) {
            currentPressed[ Event.getKeyCode()] = false;
        }
    }

    public void update() {
        for (int i = 0; i < typed.length; i++) {
            typed[i] = false;
        }
        System.arraycopy(currentPressed, 0, lastPressed, 0, currentPressed.length);
    }

    public boolean isPressed(int KeyCode) {
        return (currentPressed[ KeyCode]);
    }

    public boolean justPressed(int KeyCode) {
        return (currentPressed[KeyCode] && !lastPressed[KeyCode]);
    }

    public boolean justReleased(int KeyCode) {
        return (!currentPressed[KeyCode] && lastPressed[KeyCode]);
    }

    public boolean isTyped(int CharCode) {
        return typed[CharCode];
    }

    public int getJustTyped() {
        for (int i = 0; i < typed.length; i++) {
            if (typed[i]) {
                return i;
            }
        }
        return -1;
    }

    public int getJustPressed() {
        for (int i = 0; i < currentPressed.length; i++) {
            if (currentPressed[i] && !lastPressed[i]) {
                return i;
            }
        }
        return -1;
    }
}

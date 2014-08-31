/**
 * Do what you want 'cause a pirate is free, You are a pirate!
 * This work is licensed under the Creative Commons Attribution 4.0 International
 * License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative
 * Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 */

package Engine;

/**
 *
 * @author hara
 */
public class RedInput extends RedBasic {

    public String text = "";
    public int maxLength = -1;
    public char[] forbiddenSymbols;

    @Override
    public void update() {
        int pressedKey = RedG.keys.getJustTyped();
        if (pressedKey != -1 && (maxLength == -1 || text.length() < maxLength) && (pressedKey >= ' ' || pressedKey == '\n')) {
            if (forbiddenSymbols != null) {
                boolean notForbidden = true;
                for (int i = 0; i < forbiddenSymbols.length; i++) {
                    if (pressedKey == forbiddenSymbols[i]) {
                        notForbidden = false;
                    }
                }
                if (notForbidden) {
                    text += (char) pressedKey;
                }
            } else {
                text += (char) pressedKey;
            }
        }
        if (pressedKey == 8 && text.length() > 0) {
            text = text.substring(0, text.length() - 1);
        }
    }
}
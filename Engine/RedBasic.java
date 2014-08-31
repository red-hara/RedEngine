/**
 * Do what you want 'cause a pirate is free, You are a pirate!
 * This work is licensed under the Creative Commons Attribution 4.0 International
 * License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative
 * Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 */

package Engine;

/**
 * Basic class, children are
 * <code>RedGrop</code>,
 * <code>RedObject</code>. Controls existence and visibility.
 *
 * @author red__hara
 */
public class RedBasic extends Object{

    /**
     * Controls whether
     * <code>update()</code> and
     * <code>draw()</code> are automatically called by RedState/RedGroup.
     */
    public boolean exist;
    /**
     * Controls whether
     * <code>draw()</code> is automatically called by RedState/RedGroup.
     */
    public boolean visible;

    /**
     * Initializes existence and visibility.
     */
    public RedBasic() {
        exist = true;
        visible = true;
    }
    
    /**
     * Create is called by <code>RedG.switshState()</code>
     * and should be overridden.
     */
    public void create() {
    }
    
    /**
     * Function is used by <code>RedObject</code> and his inheritors
     * for remembering <code>last>.
     */
    public void preUpdate() {
    }

    public void update() {
    }

    public void postUpdate() {
    }

    public void draw(RedCanvas Canvas) {
    }
    
    public void destroy() {
        
    }
}

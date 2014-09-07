/**
 * Do what you want 'cause a pirate is free, You are a pirate! This work is licensed under the
 * Creative Commons Attribution 4.0 International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative Commons, 171 Second
 * Street, Suite 300, San Francisco, California, 94105, USA.
 */
package Engine;

/**
 * Basic class, children are <code>RedGrop</code>, <code>RedObject</code>. Controls existence and
 * visibility.
 *
 * @author Red Hara (rtc6fg4.fejg2@gmail.com)
 */
public class RedBasic extends Object {

	/**
	 * Controls whether <code>update()</code> and <code>draw()</code> are automatically called by
	 * RedState/RedGroup.
	 */
	public boolean exist;
	/**
	 * Controls whether <code>draw()</code> is automatically called by RedState/RedGroup.
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
	 * Create is called by <code>RedG.switshState()</code> and should be overridden.
	 */
	public void create() {
	}

	/**
	 * First step of updating.
	 */
	public void preUpdate() {
	}

	/**
	 * Second step of updating.
	 */
	public void update() {
	}

	/**
	 * Third step of updating.
	 */
	public void postUpdate() {
	}

	/**
	 * Method for dawning on <code>RedCanvas</code>.
	 *
	 * @param Canvas Canvas to draw on.
	 */
	public void draw(RedCanvas Canvas) {
	}
	
	/** 
	 * Method is called in some special cases.
	 */
	public void destroy() {

	}
}

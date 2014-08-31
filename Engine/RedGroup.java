/**
 * Do what you want 'cause a pirate is free, You are a pirate!
 * This work is licensed under the Creative Commons Attribution 4.0 International
 * License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative
 * Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 */

package Engine;

import java.util.LinkedList;

/**
 * Class for working with group of <code>RedBasic</code>.
 *
 * @author red__hara
 */
public class RedGroup extends RedBasic {

    public LinkedList<RedBasic> members;

    public RedGroup() {
        members = new LinkedList<>();
    }

    public void add(RedBasic Member) {
		members.add(Member);
    }

    public void remove(int NumberOfMember) {
		members.remove(NumberOfMember);
    }

    public void remove(RedBasic Member) {
		members.remove(Member);
    }

    @Override
    public void update() {
        if (exist) {
            for (Engine.RedBasic member : members) {
                if (member != null) {
                    if (member.exist) {
                        member.preUpdate();
                    }
                }
                if (member != null) {
                    if (member.exist) {
                        member.update();
                    }
                }
                if (member != null) {
                    if (member.exist) {
                        member.postUpdate();
                    }
                }
            }
        }
    }

    @Override
    public void draw(RedCanvas Canvas) {
        if (visible && members != null) {
            for (RedBasic member : members) {
                if (member != null) {
                    member.draw(Canvas);
                }
            }
        }
    }

    @Override
    public void create() {

    }
    
    @Override
    public void destroy() {
        if( members != null ) {
            for( RedBasic member : members ) {
                if( member != null ) {
                    member.destroy();
                }
            }
        }
    }
}

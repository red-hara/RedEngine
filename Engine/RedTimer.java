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
public class RedTimer extends RedBasic {

    private boolean _timerRang = false;
    private int _loopCounter;
    private double _timeCounter;
    public int loop;
    public double time;
    public boolean paused = true;
    public boolean finished = true;
	public RedMethod method;

	public RedTimer() {
		
	}
	
    public RedTimer( RedMethod Method ) {
		method = Method;
    }

    public void start(double Time, int Loop) {
        time = Time;
        loop = Loop;
        _timeCounter = 0;
        paused = false;
        finished = false;
    }

    public void stop() {
        finished = true;
    }

    public int getPassedLoops() {
        return _loopCounter;
    }

    public void setPassedLoops(int PassedLoops) {
        _loopCounter = PassedLoops;
    }

    @Override
    public void update() {
        if (_timerRang == true) {
            _timerRang = false;
        }

        if (!paused && !finished) {
            _timeCounter += RedG.elapsed;
            if (_timeCounter >= time) {
                _timerRang = true;
                
				if(method != null) {
					method.execute();
				}
				
                if (loop == -1) {
                    start(time, loop);
                }
                if (loop > 0) {
                    start(time, loop - 1);
                }
                if (loop == 0) {
                    finished = true;
                }
            }
        }
    }

    public boolean timerRang() {
        return _timerRang;
    }

    public double getDelay() {
        return time;
    }
	
	public double getTimeLeft() {
		return time - _timeCounter;
	}
}

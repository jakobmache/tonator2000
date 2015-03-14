package modules;

import engine.Event;
import engine.Module;
import events.PlayEvent;

public abstract class Oscillator extends Module {
	
	protected float frequency;
	protected float phase;
	protected float pulseWidth;
	protected float duration;
	
	public Oscillator()
	{
	}

	public abstract void handleEvent(Event event);

	public abstract void startPlaying(PlayEvent event);

	public abstract void stopPlaying(Event event);

	public double getFrequency() {
		return frequency;
	}

	public double getPhase() {
		return phase;
	}

	public double getPulseWidth() {
		return pulseWidth;
	}
	
	public float getDuration() {
		return duration;
	}
	
	

}

package events;

import engine.Event;

public class PlayEvent extends Event{

	public PlayEvent(int type, float frequency, float duration, float pulseWidth, float phase)
	{
		super(type, frequency, duration, pulseWidth, phase);
	}

	public float getFrequency() {
		return getData()[0];
	}

	public float getDuration() {
		return getData()[1];
	}
	
	public float getPulseWidth() {
		return getData()[2];
	}

	public float getPhase() {
		return getData()[3];
	}
}

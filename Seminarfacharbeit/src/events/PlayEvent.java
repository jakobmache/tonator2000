/*
 * 
 */
package events;

import engine.Event;

// TODO: Auto-generated Javadoc
/**
 * The Class PlayEvent.
 */
public class PlayEvent extends Event{

	/**
	 * Instantiates a new play event.
	 *
	 * @param frequency the frequency
	 * @param duration the duration
	 * @param pulseWidth the pulse width
	 * @param phase the phase
	 */
	public PlayEvent(float frequency, float duration, float pulseWidth, float phase)
	{
		super(frequency, duration, pulseWidth, phase);
	}

	/**
	 * Gets the frequency.
	 *
	 * @return the frequency
	 */
	public float getFrequency() {
		return getData()[0];
	}

	/**
	 * Gets the duration.
	 *
	 * @return the duration
	 */
	public float getDuration() {
		return getData()[1];
	}
	
	/**
	 * Gets the pulse width.
	 *
	 * @return the pulse width
	 */
	public float getPulseWidth() {
		return getData()[2];
	}

	/**
	 * Gets the phase.
	 *
	 * @return the phase
	 */
	public float getPhase() {
		return getData()[3];
	}
}

/*
 * 
 */
package events;

import engine.Event;

// TODO: Auto-generated Javadoc
/**
 * The Class StopEvent.
 */
public class StopEvent extends Event {

	/**
	 * Instantiates a new stop event.
	 *
	 * @param frequency the frequency
	 */
	public StopEvent(float frequency) 
	{
		super(frequency);
	}
	
	/**
	 * Gets the frequency.
	 *
	 * @return the frequency
	 */
	public float getFrequency()
	{
		return data[0];
	}

}

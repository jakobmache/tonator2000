/*
 * 
 */
package modules;

import engine.Event;
import engine.Module;
import engine.ModuleContainer;
import events.PlayEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class Oscillator.
 */
public abstract class Oscillator extends Module {
	
	/** The frequency. */
	protected float frequency;
	
	/** The phase. */
	protected float phase;
	
	/** The pulse width. */
	protected float pulseWidth;
	
	/** The duration. */
	protected float duration;
	
	/** The amplitude. */
	protected float amplitude;
	
	/**
	 * Instantiates a new oscillator.
	 *
	 * @param parent the parent
	 */
	public Oscillator(ModuleContainer parent)
	{
		super(parent);
	}

	/* (non-Javadoc)
	 * @see engine.Module#handleSample(float)
	 */
	public abstract float handleSample(float sampleValue);

	/**
	 * Start playing.
	 *
	 * @param event the event
	 */
	public abstract void startPlaying(PlayEvent event);

	/**
	 * Stop playing.
	 *
	 * @param event the event
	 */
	public abstract void stopPlaying(Event event);
	
	/**
	 * Pause playing.
	 */
	public abstract void pausePlaying();

	/**
	 * Gets the frequency.
	 *
	 * @return the frequency
	 */
	public double getFrequency() {
		return frequency;
	}

	/**
	 * Gets the phase.
	 *
	 * @return the phase
	 */
	public double getPhase() {
		return phase;
	}

	/**
	 * Gets the pulse width.
	 *
	 * @return the pulse width
	 */
	public double getPulseWidth() {
		return pulseWidth;
	}
	
	/**
	 * Gets the duration.
	 *
	 * @return the duration
	 */
	public float getDuration() {
		return duration;
	}
	
	

}

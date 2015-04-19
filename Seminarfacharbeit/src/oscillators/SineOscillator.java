/*
 * 
 */
package oscillators;

import modules.Oscillator;
import engine.Event;
import engine.ModuleContainer;
import events.PlayEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class SineOscillator.
 */
public class SineOscillator extends Oscillator {

	/**
	 * Instantiates a new sine oscillator.
	 *
	 * @param parent the parent
	 */
	public SineOscillator(ModuleContainer parent) 
	{
		super(parent);
	}

	@Override
	public float handleSample(float sampleValue) 
	{
		System.out.println(getOutputWire().toString());
		return (float) 11.1;
	}

	/* (non-Javadoc)
	 * @see modules.Oscillator#startPlaying(events.PlayEvent)
	 */
	@Override
	public void startPlaying(PlayEvent event) 
	{
		frequency = event.getFrequency();
		duration = event.getDuration();
		pulseWidth = event.getPulseWidth();
		phase = event.getPhase();
		
		if (duration == -1)
		{
			//TODO: implement synthesizing
		}
		
		else
		{
			throw new UnsupportedOperationException("Not yet implemented!");
		}

		
	}

	/* (non-Javadoc)
	 * @see modules.Oscillator#stopPlaying(engine.Event)
	 */
	@Override
	public void stopPlaying(Event event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see modules.Oscillator#pausePlaying()
	 */
	@Override
	public void pausePlaying() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see engine.Module#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	

}

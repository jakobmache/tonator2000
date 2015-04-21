
package oscillators;

import modules.Oscillator;
import engine.Event;
import engine.ModuleContainer;
import events.PlayEvent;

public class SineOscillator extends Oscillator {

	public SineOscillator(ModuleContainer parent) 
	{
		super(parent);
	}

	@Override
	public float handleSample(float sampleValue) 
	{
		return (float) sampleValue;
	}

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

	@Override
	public void stopPlaying(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pausePlaying() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	

}

package oscillators;

import engine.Event;
import events.PlayEvent;
import modules.Oscillator;

public class SineOscillator extends Oscillator {

	public SineOscillator() 
	{

	}

	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof PlayEvent)
		{
			startPlaying((PlayEvent) event);
		}
		
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
	

}

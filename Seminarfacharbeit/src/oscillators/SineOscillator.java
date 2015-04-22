
package oscillators;

import modules.Oscillator;
import engine.Event;
import engine.ModuleContainer;
import events.PlayEvent;

public class SineOscillator extends Oscillator {
	
	private double cycleIncrease;
	private double cyclePosition;


	public SineOscillator(ModuleContainer parent) 
	{
		super(parent);
	}

	@Override
	public short handleSample(short sampleValue) 
	{
		cycleIncrease = frequency / getEngine().getSamplingRate();
		short value = (short) (amplitude * Math.sin(2 * Math.PI * cyclePosition));
		cyclePosition += cycleIncrease;
		if (cyclePosition > 1)
			cyclePosition -= 1;	
		System.out.println("Value: " + value);
		return value;
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
			cycleIncrease = frequency / getEngine().getSamplingRate();
			cyclePosition = 0;
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

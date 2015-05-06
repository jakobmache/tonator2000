package modules;

import engine.Module;
import engine.ModuleContainer;

//Grundlage für Sinus: http://www.wolinlabs.com/blog/java.sine.wave.html
public class Oscillator extends Module 
{

	public static final int TYPE_SINE = 0;
	public static final int TYPE_SQUARE = 1;

	private static int TYPE = Oscillator.TYPE_SINE;

	private double frequency;
	private double amplitude;

	private double cycleIncrease;
	private double cyclePosition;

	public Oscillator(ModuleContainer parent) 
	{
		super(parent, 0, 1);
	}

	public void setType(int newType)
	{
		TYPE = newType;
	}

	public void setFrequency(double frequency)
	{
		this.frequency = frequency;
		cycleIncrease = frequency / getEngine().getSamplingRate();
		cyclePosition = 0;
	}
	
	public void setAmplitude(double amplitude)
	{
		this.amplitude = amplitude;
	}

	public short requestNextSample() 
	{
		short value;
		switch(TYPE)
		{
		case(Oscillator.TYPE_SINE):
			cycleIncrease = frequency / getEngine().getSamplingRate();
			value = (short) (amplitude * Math.sin(2 * Math.PI * cyclePosition));
			cyclePosition += cycleIncrease;
			if (cyclePosition > 1)
				cyclePosition -= 1;	
			return value;
		
		case (Oscillator.TYPE_SQUARE):
			cycleIncrease = frequency / getEngine().getSamplingRate();
			short sineValue = (short) (amplitude * Math.sin(2 * Math.PI * cyclePosition));
			cyclePosition += cycleIncrease;
			if (sineValue >= 0)
				value = (short) amplitude;
			else 
				value = (short) (-1 * amplitude);
			return value;
			
		default:
			return 0;
		}
	}

}

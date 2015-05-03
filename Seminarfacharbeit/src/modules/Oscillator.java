package modules;

import engine.Module;
import engine.ModuleContainer;

public class Oscillator extends engine.Module {

	public static final int TYPE_SINE = 0;
	public static final int TYPE_SAW = 1;

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
		switch(TYPE)
		{
		case(Oscillator.TYPE_SINE):
			cycleIncrease = frequency / getEngine().getSamplingRate();
			short value = (short) (amplitude * Math.sin(2 * Math.PI * cyclePosition));
			cyclePosition += cycleIncrease;
			if (cyclePosition > 1)
				cyclePosition -= 1;	
			return value;
			
		default:
			return 0;
		}
	}

}

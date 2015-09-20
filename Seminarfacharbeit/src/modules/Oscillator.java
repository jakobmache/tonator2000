package modules;

import utils.Constants;
import engine.Module;
import engine.SynthesizerEngine;

//Grundlage für Sinus: http://www.wolinlabs.com/blog/java.sine.wave.html
public class Oscillator extends Module 
{

	public static final int TYPE_SINE = 0;
	public static final int TYPE_SQUARE = 1;
	public static final int TYPE_SAW = 2;

	private int type = Oscillator.TYPE_SINE;

	private double frequency;
	private double amplitude;

	private double cycleIncrease;
	private double cyclePosition;

	public Oscillator(SynthesizerEngine parent) 
	{
		super(parent, 0, 1);
	}

	public void setType(int newType)
	{
		type = newType;
	}

	public void setFrequency(double frequency)
	{
		this.frequency = frequency;
		cycleIncrease = frequency / parent.getSamplingRate();
		cyclePosition = 0;
	}

	public void setAmplitude(double amplitude)
	{
		this.amplitude = amplitude;
	}

	public short requestNextSample(int outputWireIndex) 
	{

		if (frequency == 0)
			return 0;

		short value;
		if (type == TYPE_SINE)
		{
			cycleIncrease = frequency / parent.getSamplingRate();
			value = (short) (amplitude * Math.sin(Constants.TWOPI * cyclePosition));
			cyclePosition += cycleIncrease;
			if (cyclePosition > 1)
				cyclePosition -= 1;	
			return value;
		}

		else if (type == TYPE_SQUARE)
		{
			cycleIncrease = frequency / parent.getSamplingRate();
			short sineValue = (short) (amplitude * Math.sin(2 * Math.PI * cyclePosition));
			cyclePosition += cycleIncrease;
			if (cyclePosition > 1)
				cyclePosition -= 1;	
			if (sineValue >= 0)
				value = (short) amplitude;
			else 
				value = (short) (-1 * amplitude);
			return value;
		}

		else if (type == TYPE_SAW)
		{
			cycleIncrease = frequency / parent.getSamplingRate();
			double doubleValue = cyclePosition - Math.floor(cyclePosition);
			cyclePosition += cycleIncrease;
			value = (short) (amplitude * doubleValue);
			if (cyclePosition > 1)
				cyclePosition -= 1;	
			return value;
		}
	
		else {
	
			return 0;
		}
	}

}

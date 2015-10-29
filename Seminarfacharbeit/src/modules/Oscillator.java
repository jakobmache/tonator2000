package modules;

import utils.Constants;
import engine.Module;
import engine.SynthesizerEngine;

//Grundlage für Sinus: http://www.wolinlabs.com/blog/java.sine.wave.html
public class Oscillator extends Module 
{
	public static final int FREQUENCY_INPUT = 0;
	public static final int AMPLITUDE_INPUT = 1;
	public static final int TYPE_INPUT = 2;
	
	public static final int SAMPLE_OUTPUT = 0;

	public static final int TYPE_SINE = 0;
	public static final int TYPE_SQUARE = 1;
	public static final int TYPE_SAW = 2;

	private int type = Oscillator.TYPE_SINE;

	private float frequency;
	private float amplitude;

	private double cycleIncrease;
	private double cyclePosition;

	public Oscillator(SynthesizerEngine parent, int id) 
	{
		super(parent, 3, 1, id);
	}

	public void setType(int newType)
	{
		type = newType;
		if (type == TYPE_SAW)
			cyclePosition = -1;
	}

	public void setFrequency(float frequency)
	{
		this.frequency = frequency;
		cycleIncrease = frequency / parent.getSamplingRate();
		cyclePosition = 0;
	}

	public void setAmplitude(float amplitude)
	{
		this.amplitude = amplitude;
	}

	public float requestNextSample(int index) 
	{
		if (frequency != inputWires[FREQUENCY_INPUT].getNextSample())
			setFrequency(inputWires[FREQUENCY_INPUT].getNextSample());
		
		if (type != inputWires[TYPE_INPUT].getNextSample())
			setType((int) inputWires[TYPE_INPUT].getNextSample());
		
		setAmplitude(inputWires[AMPLITUDE_INPUT].getNextSample());
		
		if (frequency == 0)
			return 0;

		float value;
		if (type == TYPE_SINE)
		{
			value = (float) (amplitude * Math.sin(Constants.TWOPI * cyclePosition));
			cyclePosition += cycleIncrease;
			if (cyclePosition > 1)
				cyclePosition -= 1;	
			return value;
		}

		else if (type == TYPE_SQUARE)
		{
			float sineValue = (float) (amplitude * Math.sin(2 * Math.PI * cyclePosition));
			cyclePosition += cycleIncrease;
			if (cyclePosition > 1)
				cyclePosition -= 1;	
			if (sineValue >= 0)
				value = amplitude;
			else 
				value = (-1F * amplitude);
			return value;
		}

		else if (type == TYPE_SAW)
		{
			cycleIncrease = 2 / (parent.getSamplingRate() / frequency);
			cyclePosition += cycleIncrease;
			if (cyclePosition > 1)
				cyclePosition = -1;
			return (float) (amplitude * cyclePosition);
		}
	
		else {
	
			return 0;
		}
	}
	
	public double getFrequency()
	{
		return frequency;
	}

	public double getAmplitude() {
		return amplitude;
	}

}

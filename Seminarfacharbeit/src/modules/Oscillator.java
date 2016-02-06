package modules;

import java.util.Random;

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
	public static final int TYPE_SQUARE = 2;
	public static final int TYPE_SAW = 1;
	public static final int TYPE_TRI = 3;
	public static final int TYPE_WHITE_NOISE = 4;

	private int waveform = Oscillator.TYPE_SINE;

	private float frequency;
	private float amplitude;

	private double cycleIncrease;
	private double cyclePosition;
	
	private Random random;

	/**
	 * Ein Oszillator erzeugt eine Grundwellenform.
	 * 
	 * @param parent Engine
	 * @param id ID
	 * @param name Name
	 */
	public Oscillator(SynthesizerEngine parent, int id, String name) 
	{
		super(parent, 3, 1, id, name);
		type = ModuleType.OSCILLATOR;
		random = new Random(System.currentTimeMillis());
	}

	public void setWaveform(int newType)
	{
		waveform = newType;
		if (waveform == TYPE_SAW)
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

	@Override
	public float calcNextDisabledSample(int index) 
	{
		return 0F;
	}
	
	public float calcNextSample(int index) 
	{
		if (frequency != inputWires[FREQUENCY_INPUT].getNextSample())
			setFrequency(inputWires[FREQUENCY_INPUT].getNextSample());
		
		if (waveform != inputWires[TYPE_INPUT].getNextSample())
			setWaveform((int) inputWires[TYPE_INPUT].getNextSample());
		
		setAmplitude(inputWires[AMPLITUDE_INPUT].getNextSample());
		
		//ZeroDivision verhindern!
		if (Math.abs(frequency) < 0.0001F)
		{
			return 0;
		}

		if (Math.abs(amplitude) < 0.0001F)
		{
			return 0;
		}

		float value;
		if (waveform == TYPE_SINE)
		{
			value = (float) (amplitude * Math.sin(Constants.TWOPI * cyclePosition));
			cyclePosition += cycleIncrease;
			if (cyclePosition > 1)
				cyclePosition -= 1;	
			return value;
		}

		else if (waveform == TYPE_SQUARE)
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

		else if (waveform == TYPE_SAW)
		{
			cycleIncrease = 2 / (parent.getSamplingRate() / frequency);
			cyclePosition += cycleIncrease;
			if (cyclePosition > 1)
				cyclePosition = -1;
			return (float) (amplitude * cyclePosition);
		}
		
		else if (waveform == TYPE_TRI)
		{
			cycleIncrease = 2 / (parent.getSamplingRate() / frequency);
			value = (float) (2 / Math.PI * Math.asin(Math.sin(Math.PI * cyclePosition)));
			cyclePosition += cycleIncrease;
			if (cyclePosition > 1)
				cyclePosition = -1;
			return (float) (amplitude * value);
		}
		
		else if (waveform == TYPE_WHITE_NOISE)
		{
			value = (2 * random.nextFloat() - 1) * amplitude;
			return value;
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

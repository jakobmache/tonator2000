package modules;

import engine.Module;
import engine.SynthesizerEngine;

public class HighpassFilter extends Module
{
	public static final int SAMPLE_INPUT = 0;
	public static final int CUTOFF_INPUT = 1;
	public static final int RESONANCE_INPUT = 2;

	public static final int SAMPLE_OUTPUT = 0;

	private float cutoffFrequency = 1000F;
	private float resonance = 0.2F;

	private double p = 0, q = 0, f = 0;
	private double t1 = 0, t2 = 0, b0 = 0, b1 = 0, b2 = 0, b3 = 0, b4 = 0;

	/**
	 * Der Hochpassfilter filtert alle Obertöne unter der Grenzfrequenz
	 * 
	 * @param parent Engine
	 * @param id ID
	 * @param name Name
	 */
	public HighpassFilter(SynthesizerEngine parent, int id, String name) 
	{
		super(parent, 3, 1, id, name);
	}

	public float calcNextDisabledSample(int index) 
	{
		return inputWires[SAMPLE_INPUT].getNextSample();
	}

	@Override
	public float calcNextSample(int index) 
	{
		if (cutoffFrequency != inputWires[CUTOFF_INPUT].getNextSample())
		{
			setCutoffFrequency(inputWires[CUTOFF_INPUT].getNextSample());
		}
		if (resonance != inputWires[RESONANCE_INPUT].getNextSample())
		{
			setResonance(inputWires[RESONANCE_INPUT].getNextSample());
		}

		float inputSample = inputWires[SAMPLE_INPUT].getNextSample() / Short.MAX_VALUE;
		
		inputSample -= q * (b4 - inputSample);                          //feedback

		t1 = b1;  
		b1 = (inputSample + b0) * p - b1 * f;
		t2 = b2;  
		b2 = (b1 + t1) * p - b2 * f;
		t1 = b3;  
		b3 = (b2 + t2) * p - b3 * f;
		b4 = (b3 + t1) * p - b4 * f;
		b4 = b4 - b4 * b4 * b4 * 0.166667f;    //clipping
		b0 = inputSample;

		//System.out.println(inputSample + "|" + b4);
		return (float)(b0 - b4) * Short.MAX_VALUE;
	}

	public void setCutoffFrequency(float newValue)
	{
		if (newValue > 1.0F)
			newValue = 1.0F;
		if (newValue < 0F)
			newValue = 0F;
		
		cutoffFrequency = newValue;
		precalc();
	}

	public void setResonance(float newValue)
	{
		if (newValue > 1.0F)
			newValue = 1.0F;
		if (newValue < 0F)
			newValue = 0F;
		
		this.resonance = newValue;
		precalc();
	}

	private void precalc()
	{
		q = 1.0f - cutoffFrequency;
		p = cutoffFrequency + 0.8f * cutoffFrequency * q;
		f = p + p - 1.0f;
		q = resonance * (1.0f + 0.5f * q * (1.0f - q + 5.6f * q * q));
	}

	public double getCutoffFrequency() {
		return cutoffFrequency;
	}

	public double getResonance() {
		return resonance;
	}
}

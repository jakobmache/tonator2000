package modules;

import engine.Module;
import engine.SynthesizerEngine;

public class Amplifier extends Module 
{
	public static final int SAMPLE_INPUT = 0;
	public static final int FACTOR_INPUT = 1;
	public static final int SAMPLE_OUTPUT = 0;

	public Amplifier(SynthesizerEngine parent, int id) 
	{
		super(parent, 2, 1, id);
	}

	@Override
	public float requestNextSample(int index) 
	{
		float inputSample = inputWires[SAMPLE_INPUT].getNextSample();
		float newValue = inputWires[FACTOR_INPUT].getNextSample() * inputSample;
		return newValue;
	}
}

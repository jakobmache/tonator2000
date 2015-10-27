package modules;

import engine.Module;
import engine.SynthesizerEngine;

public class Amplifier extends Module 
{
	private float factor;

	public Amplifier(SynthesizerEngine parent) 
	{
		super(parent, 1, 1, Ids.ID_AMPLIFIER);
	}

	@Override
	public float requestNextSample() 
	{
		float inputSample = inputWires[0].getNextSample();
		float newValue = inputSample * factor;
		return (float) newValue;
	}
	
	public void setFactor(float newFactor)
	{
		factor = newFactor;
	}

}

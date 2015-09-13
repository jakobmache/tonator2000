package modules;

import engine.Module;
import engine.SynthesizerEngine;

public class Amplifier extends Module 
{
	
	private double factor;

	public Amplifier(SynthesizerEngine parent) 
	{
		super(parent, 1, 1);
	}

	@Override
	public short requestNextSample(int outputWireIndex) 
	{
		short inputSample = inputWires[0].getNextSample();
		double newValue = inputSample * factor;
		return (short) newValue;
	}
	
	public void setFactor(double newFactor)
	{
		factor = newFactor;
	}

}

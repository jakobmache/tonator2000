package modules;

import engine.SynthesizerEngine;

public class Constant extends Oscillator
{

	private double value = 100;
	
	public Constant(SynthesizerEngine parent) 
	{
		super(parent);
	}
	
	public void setValue(short newValue)
	{
		value = newValue;
	}

	@Override
	public float requestNextSample() 
	{
		return (short) value;
	}
	
	@Override
	public void setAmplitude(double newValue)
	{
		value = (short) newValue;
	}

	

}

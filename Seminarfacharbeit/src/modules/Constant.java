package modules;

import engine.Module;
import engine.SynthesizerEngine;

public class Constant extends Module
{
	public static final int VALUE_OUTPUT = 0;
	
	private float value = 100;
	
	public Constant(SynthesizerEngine parent, int id) 
	{
		super(parent, 0, 1, id);
	}
	
	public void setValue(float sample)
	{
		value = sample;
	}

	@Override
	public float requestNextSample(int index)
	{
		if (index != VALUE_OUTPUT)
			return 0;
		return value;
	}


	

}

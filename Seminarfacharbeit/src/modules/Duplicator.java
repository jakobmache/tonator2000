package modules;

import engine.Module;
import engine.SynthesizerEngine;

public class Duplicator extends Module
{
	public static final int SAMPLE_INPUT = 0;
	public static final int MAIN_SAMPLE_OUTPUT = 0;
	
	private float sampleSave = 0;

	public Duplicator(SynthesizerEngine parent, int id, String moduleName) 
	{
		super(parent, 1, 2, id, moduleName);
		
		type = ModuleType.DUPLICATOR;
	}

	@Override
	public float calcNextSample(int index) 
	{		
		if (index == MAIN_SAMPLE_OUTPUT)
		{
			float sample = inputWires[SAMPLE_INPUT].getNextSample();
			sampleSave = sample;
			return sample;
		}
		else 
		{
			return sampleSave;
		}
	}

	@Override
	public float calcNextDisabledSample(int index)
	{
		return 0;
	}

}

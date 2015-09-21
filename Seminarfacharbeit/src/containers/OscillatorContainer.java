package containers;

import modules.Oscillator;
import engine.ModuleContainer;
import engine.SynthesizerEngine;

public class OscillatorContainer extends ModuleContainer
{
	
	private Oscillator oscillator;

	public OscillatorContainer(SynthesizerEngine parent) 
	{
		super(parent);
		initModules();
	}
	
	private void initModules()
	{
		oscillator = new Oscillator(parent);
	}
	
	public Oscillator getOscillator()
	{
		return oscillator;
	}

	@Override
	public short requestNextSample(int outputWireIndex) 
	{
		short value = oscillator.requestNextSample(0);
		return value;
	}

}

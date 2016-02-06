package containers;

import engine.ModuleContainer;
import engine.SynthesizerEngine;
import modules.Constant;
import modules.Ids;
import modules.Oscillator;

public class TestContainer extends PlayableModuleContainer
{

	
	public TestContainer(SynthesizerEngine parent) 
	{
		super(parent, 1, 1, Ids.getNextId(), "DududuContainer");
		
		Constant amplConstant = new Constant(parent, Ids.getNextId(), "Amplitude");
		Constant freqConstant = new Constant(parent, Ids.getNextId(), "Frequenz");
		Constant wfConstant = new Constant(parent, Ids.getNextId(), "Wellenform");
		addModule(amplConstant);
		addModule(freqConstant);
		addModule(wfConstant );
		setAmplitudeId(amplConstant.getId());
		setFrequencyId(freqConstant.getId());
		wfConstant.setValue(1.0F);
		
		Oscillator oscillator = new Oscillator(parent, Ids.getNextId(), "Oszillator 1");
		addModule(oscillator);
		
		addConnection(amplConstant, oscillator, Constant.VALUE_OUTPUT, Oscillator.AMPLITUDE_INPUT);
		addConnection(freqConstant, oscillator, Constant.VALUE_OUTPUT, Oscillator.FREQUENCY_INPUT);
		addConnection(wfConstant, oscillator, Constant.VALUE_OUTPUT, Oscillator.TYPE_INPUT);
		
		addConnection(oscillator, this, Oscillator.SAMPLE_OUTPUT, ModuleContainer.SAMPLE_INPUT);
		
		setFreqToZeroOnStop(true);
	}

	@Override
	public void startPlaying(float frequency, float amplitude) 
	{	
	}

	@Override
	public void stopPlaying() 
	{		
	}

}

package containers;

import engine.Module;
import engine.PlayableModuleContainer;
import engine.SynthesizerEngine;
import modules.Constant;
import modules.Envelope;
import modules.ModuleType;

public class SynthesizerModuleContainer extends PlayableModuleContainer
{
	public SynthesizerModuleContainer(SynthesizerEngine parent, int numInputWires, int numOutputWires, int id,
			String name, PlayableModuleContainer container) {
		super(parent, numInputWires, numOutputWires, id, name, container);
	}

	@Override
	public void startPlaying(float frequency, float amplitude) 
	{	
		((Constant) findModuleById(getFrequencyId())).setValue(frequency);
		((Constant) findModuleById(getAmplitudeId())).setValue(amplitude);
		
		System.out.println("Start playing!");
		for (Module module:modules)
		{
			if (module.getType() == ModuleType.ENVELOPE)
				((Envelope) module).start();
		}
	}

	@Override
	public void stopPlaying() 
	{	
		for (Module module:modules)
		{
			if (module.getType() == ModuleType.ENVELOPE)
				((Envelope) module).release();
		}
	}
	
	public float calcNextSample(int index)
	{
		return inputWires[SAMPLE_INPUT].getNextSample();
	}


}

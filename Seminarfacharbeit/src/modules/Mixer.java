package modules;

import engine.Module;
import engine.SynthesizerEngine;
import engine.Wire;

public class Mixer extends Module{

	private int numModules = 0;
	
	public Mixer(SynthesizerEngine parent, int numInputWires) 
	{
		super(parent, numInputWires, 1, Ids.ID_MIXER);
	}

	@Override
	public float requestNextSample() 
	{
		float value = calculateSum();
		return value;
	}
	
	private float calculateSum()
	{
		float sum = 0;
		
		for (Wire inputWire:inputWires)
		{
			float value = 0;
			
			if (inputWire != null)
				value = inputWire.getNextSample();
			else 
				continue;

			sum += value;
		}

		
		sum = sum / parent.getMaxPolyphony();
		
		return sum;
		
	}
	
	@Override
	public void connectInputWire(int index, Wire wire)
	{
		inputWires[index] = wire;
		numModules++;
	}
	
	public int getNumModules()
	{
		return numModules;
	}


}

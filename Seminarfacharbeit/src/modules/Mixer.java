package modules;

import engine.Module;
import engine.SynthesizerEngine;
import engine.Wire;

public class Mixer extends Module{

	public static final int SAMPLE_OUTPUT = 0;
	
	public static final int NEXT_FREE_INPUT = 0;
	
	private int numModules = 0;
	
	public Mixer(SynthesizerEngine parent, int numInputWires, int id) 
	{
		super(parent, numInputWires, 1, id);
	}
	
	@Override
	public float calcNextDisabledSample(int index) 
	{
		return 0;
	}

	@Override
	public float calcNextSample(int index) 
	{
		if (!enabled)
			return 0;
		
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
			{
				value = inputWire.getNextSample();
			}
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
		int targetIndex = inputWires.length - 1;
		//Wir wollen am nächsten freien Index "andocken"
		for (int i = 0; i < inputWires.length; i++)	
		{
			if (inputWires[i] == null)
			{
				targetIndex = i;
				break;
			}
		}
		
		inputWires[targetIndex] = wire;
	}
	
	public void disconnectInputWire(Wire wire)
	{
		for (int i = 0; i < inputWires.length; i++)
		{
			if (inputWires[i] == wire)
				inputWires[i] = null;
		}
	}
	
	public int getNumModules()
	{
		return numModules;
	}


}

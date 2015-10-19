package modules;

import engine.Module;
import engine.SynthesizerEngine;
import engine.Wire;

public class Mixer extends Module{

	private int numModules = 0;
	
	public Mixer(SynthesizerEngine parent, int numInputWires) 
	{
		super(parent, numInputWires, 1);
	}

	@Override
	public short requestNextSample(int outputWireIndex) 
	{
		short value = calculateSum();
		return value;
	}
	
	private short calculateSum()
	{
		int n = 0;
		long sum = 0;
		
		for (Wire inputWire:inputWires)
		{
			short value = 0;
			
			if (inputWire != null)
				value = inputWire.getNextSample();
			else 
				continue;

	
			
//			if (value != 0)
//				n++;
			sum += value;
		}
		
//		if (n != 0)
//			sum = sum / n;
		
		sum = sum / parent.getMaxPolyphony();
		
		return (short) sum;
		
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

package modules;

import java.util.Arrays;
import java.util.List;

import javafx.scene.chart.ValueAxis;
import engine.Module;
import engine.SynthesizerEngine;
import engine.Wire;

public class Mixer extends Module{

	private InputController inputModule;
	private int numModules = 0;
	
	public Mixer(SynthesizerEngine parent, int numInputWires) 
	{
		super(parent, numInputWires, 1);
		inputModule = parent.getInputController();
		
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
		int i = 0;
		int sum = 0;
		
		for (Wire inputWire:inputWires)
		{
			short value = 0;
			
			if (inputWire != null)
				value = inputWire.getNextSample();
			else 
				continue;

	
			
			if (value != 0)
				n++;
			sum += value;
		}
		
		if (n != 0)
			sum = sum / n;
		
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

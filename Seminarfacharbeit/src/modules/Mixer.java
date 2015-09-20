package modules;

import java.util.Arrays;
import java.util.List;

import javafx.scene.chart.ValueAxis;
import engine.Module;
import engine.SynthesizerEngine;
import engine.Wire;

public class Mixer extends Module{

	private InputController inputModule;
	
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
		int sum = 0;
		
		for (Wire inputWire:inputWires)
		{
			short value = inputWire.getNextSample();
			if (value != 0)
				n++;
			sum += value;
		}
		
		if (n != 0)
			sum = sum / n;
		
		return (short) sum;
		
	}
	


}

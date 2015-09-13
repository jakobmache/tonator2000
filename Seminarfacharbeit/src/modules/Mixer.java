package modules;

import java.util.Arrays;

import javafx.scene.chart.ValueAxis;
import engine.Module;
import engine.SynthesizerEngine;
import engine.Wire;

public class Mixer extends Module{

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
		int sum = 0;
		int x = 0;
		
		for (Wire inputWire:inputWires)
		{
			short value = inputWire.getNextSample();
			if (value != 0)
			{
				//System.out.println(x + ":" + value);
				n += 1;
			}
			sum += value;
			x++;
		}
		
		if (n != 0)
			sum = sum / n;

		return (short) sum;
		
	}
	


}

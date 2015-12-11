package modules;

import engine.Module;
import engine.SynthesizerEngine;
import engine.Wire;

public class Mixer extends Module{

	public static final int SAMPLE_OUTPUT = 0;

	public static final int NEXT_FREE_INPUT = 0;

	private int numModules = 0;

	public Mixer(SynthesizerEngine parent, int id, String name) 
	{
		super(parent, SynthesizerEngine.MAX_POLYPHONY, 1, id, name);
	}

	@Override
	public float calcNextDisabledSample(int index) 
	{
		return 0;
	}

	@Override
	public float calcNextSample(int index) 
	{
		float value = calculateSum();
		return value;
	}

	private float calculateSum()
	{
		float sum = 0;
		for (int i = 0; i < parent.getMaxPolyphony(); i++)
		{
			Wire inputWire = inputWires[i];
			if (inputWire != null)
			{
				float value = inputWire.getNextSample();
				sum += value;
			}
			else 
				continue;		
		}

		sum = sum /parent.getMaxPolyphony();

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

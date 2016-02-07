package modules;

import engine.Module;
import engine.SynthesizerEngine;
import engine.Wire;

public class Mixer extends Module{

	public static final int SAMPLE_OUTPUT = 0;

	public static final int NEXT_FREE_INPUT = Integer.MAX_VALUE;

	private int numInputs = 0;

	/**
	 * Ein Mixer addiert verschiedene Audiosignale und hält sie dabei in ihrem Wertebereich.
	 * 
	 * @param parent Engine, von der die maximale Polyphonie abgerufen wird.
	 * @param id ID
	 * @param name Name
	 */
	public Mixer(SynthesizerEngine parent, int numInputs, int id, String name) 
	{
		super(parent, numInputs, 1, id, name);
		type = ModuleType.MIXER;
		this.numInputs = numInputs;
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
		for (int i = 0; i < numInputs; i++)
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

		sum = sum / numInputs;

		return sum;
	}

	@Override
	public void connectInputWire(int index, Wire wire)
	{
		if (index == NEXT_FREE_INPUT)
		{
			//Muhahahaha fake
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
		else {
			inputWires[index] = wire;
		}
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
		return numInputs;
	}

	public int getNumInputs() 
	{
		return numInputs;
	}


}

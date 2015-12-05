package modules;

import engine.SynthesizerEngine;
import engine.Wire;

public class BalancedMixer extends Mixer
{
	public static final int SAMPLE_INPUT_1 = 0;
	public static final int SAMPLE_INPUT_2 = 1;
	public static final int BALANCE_INPUT = 2;
	
	//Balance --> 0 bedeutet, dass komplett Input 1 "durchkommt", bei 0.5 beide, bei 1 Input 2
	private float balance = 0.5F;

	public BalancedMixer(SynthesizerEngine parent, int id, String name) 
	{
		super(parent, 3, id, name);
	}
	
	@Override
	public float calcNextSample(int index)
	{
		balance = inputWires[BALANCE_INPUT].getNextSample();
		
		float input1 = inputWires[SAMPLE_INPUT_1].getNextSample();
		input1 *= 1.0F - balance;
		
		float input2 = inputWires[SAMPLE_INPUT_2].getNextSample();
		input2 *= balance;
		
		float value = (input1 + input2) / 2;

		return value;
	}
	
	public void connectInputWire(int index, Wire wire)
	{
		inputWires[index] = wire;
	}
	

}

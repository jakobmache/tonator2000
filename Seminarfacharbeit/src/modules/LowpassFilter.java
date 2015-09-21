package modules;

import utils.Constants;
import engine.Module;
import engine.SynthesizerEngine;

public class LowpassFilter extends Module{
	
	private double oldValue = 0;

	private double alpha;
	
	private double cutoffFrequency = 1000;

	public LowpassFilter(SynthesizerEngine parent, int numInputWires,
			int numOutputWires) 
	{
		super(parent, numInputWires, numOutputWires);
		
		double timeDelta = 1 / parent.getSamplingRate();
		alpha = (Constants.TWOPI * timeDelta * cutoffFrequency) / (Constants.TWOPI * timeDelta * cutoffFrequency + 1);
	}

	@Override
	public short requestNextSample(int outputWireIndex) 
	{
		short inputSample = inputWires[0].getNextSample();
		double value = alpha * inputSample + (1 - alpha) * oldValue;
		oldValue = value;
		return (short) value;
	}
	
	public void setCutoffFrequency(double newValue)
	{
		cutoffFrequency = newValue;
		double timeDelta = 1 / parent.getSamplingRate();
		alpha = (Constants.TWOPI * timeDelta * cutoffFrequency) / (Constants.TWOPI * timeDelta * cutoffFrequency + 1);
	}

}

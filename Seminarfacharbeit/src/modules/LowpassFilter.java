package modules;

import utils.Constants;
import engine.Module;
import engine.SynthesizerEngine;

public class LowpassFilter extends Module
{
	
	private double oldValue = 0;

	private double alpha;
	
	private double cutoffFrequency;
	private double resonance = 0.2;
	
	private double a1, a2, a3, b1, b2, c;
	private double input1, input2, output1, output2;

	//Input 1 - samples
	//Input 2 - cutoff frequency
	public LowpassFilter(SynthesizerEngine parent) 
	{
		super(parent, 2, 1);
		
		double timeDelta = 1 / parent.getSamplingRate();
		alpha = (Constants.TWOPI * timeDelta * cutoffFrequency) / (Constants.TWOPI * timeDelta * cutoffFrequency + 1);
	}

	@Override
	public short requestNextSample(int outputWireIndex) 
	{
//		short inputSample = inputWires[0].getNextSample();
//		inputSample -= resonance * oldValue;
//		double value = alpha * inputSample + (1 - alpha) * oldValue;
//		oldValue = value;
		setCutoffFrequency(inputWires[1].getNextSample());
		c = (1.0) / Math.tan(Math.PI * cutoffFrequency / parent.getSamplingRate());
		a1 = 1.0 / ( 1.0 + resonance * c + c * c);
		a2 = 2 * a1;
		a3 = a1;
		b1 = 2.0 * ( 1.0 - c*c) * a1;
		b2 = ( 1.0 - resonance * c + c * c) * a1;
		input1 = 0;
		output1 = 0;
		input2 = 0;
		output2 = 0;
		
		double input = inputWires[0].getNextSample();
		double value = a1 * input + a2 * input1 + a3* input2 - b1 * output1 - b2 * output2;
		
		input2 = input1;
		input1 = input;
		output2 = output1;
		output1 = value;
		
		return (short) value;
	}
	
	public void setCutoffFrequency(double newValue)
	{
		cutoffFrequency = newValue;
		double timeDelta = 1 / parent.getSamplingRate();
		alpha = (Constants.TWOPI * timeDelta * cutoffFrequency) / (Constants.TWOPI * timeDelta * cutoffFrequency + 1);
	}
	
	public void setResonance(double newValue)
	{
		this.resonance = newValue;
	}

}

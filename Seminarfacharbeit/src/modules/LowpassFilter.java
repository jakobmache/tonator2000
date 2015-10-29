package modules;

import utils.Constants;
import engine.Module;
import engine.SynthesizerEngine;

public class LowpassFilter extends Module
{
	
	public static final int SAMPLE_INPUT = 0;
	public static final int CUTOFF_INPUT = 1;
	public static final int RESONANCE_INPUT = 2;
	
	public static final int SAMPLE_OUTPUT = 0;
	
	private double oldValue = 0;

	private double alpha;
	
	private float cutoffFrequency = 1000F;
	private float resonance = 0.2F;
	
	private double a1, a2, a3, b1, b2, c;
	private double input1, input2, output1, output2;

	public LowpassFilter(SynthesizerEngine parent, int id) 
	{
		super(parent, 3, 1, id);
	}

	@Override
	public float requestNextSample(int index) 
	{
		if (cutoffFrequency != inputWires[CUTOFF_INPUT].getNextSample())
		{
			setCutoffFrequency(inputWires[CUTOFF_INPUT].getNextSample());
		}
		if (resonance != inputWires[RESONANCE_INPUT].getNextSample())
		{
			setCutoffFrequency(inputWires[RESONANCE_INPUT].getNextSample());
		}
		
		float inputSample = inputWires[SAMPLE_INPUT].getNextSample();

		inputSample -= resonance * oldValue;
		float value = (float) (alpha * inputSample + (1 - alpha) * oldValue);
		oldValue = value;
//		setCutoffFrequency(inputWires[1].getNextSample());
//		c = (1.0) / Math.tan(Math.PI * cutoffFrequency / parent.getSamplingRate());
//		a1 = 1.0 / ( 1.0 + resonance * c + c * c);
//		a2 = 2 * a1;
//		a3 = a1;
//		b1 = 2.0 * ( 1.0 - c*c) * a1;
//		b2 = ( 1.0 - resonance * c + c * c) * a1;
//		input1 = 0;
//		output1 = 0;
//		input2 = 0;
//		output2 = 0;
//		
//		double input = inputWires[0].getNextSample();
//		double value = a1 * input + a2 * input1 + a3* input2 - b1 * output1 - b2 * output2;
//		
//		input2 = input1;
//		input1 = input;
//		output2 = output1;
//		output1 = value;
		

		
		return value;
	}
	
	public void setCutoffFrequency(float newValue)
	{
		cutoffFrequency = newValue;
		double timeDelta = 1 / parent.getSamplingRate();
		alpha = (Constants.TWOPI * timeDelta * cutoffFrequency) / (Constants.TWOPI * timeDelta * cutoffFrequency + 1);
	}
	
	public void setResonance(float newValue)
	{
		this.resonance = newValue;
	}

	public double getCutoffFrequency() {
		return cutoffFrequency;
	}

	public double getResonance() {
		return resonance;
	}
	

}

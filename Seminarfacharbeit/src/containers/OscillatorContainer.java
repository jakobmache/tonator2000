package containers;

import modules.Envelope;
import modules.Oscillator;
import engine.ModuleContainer;
import engine.SynthesizerEngine;

public class OscillatorContainer extends ModuleContainer
{
	
	private Oscillator oscillator;
	private Envelope envelope;

	public OscillatorContainer(SynthesizerEngine parent) 
	{
		super(parent);
		initModules();
	}
	
	private void initModules()
	{
		oscillator = new Oscillator(parent);
		envelope = new Envelope(parent, oscillator);
	}
	
	public void startPlaying(float frequency, float amplitude)
	{
		oscillator.setFrequency(frequency);
		oscillator.setAmplitude(amplitude);
		envelope.start();
	}
	
	public void stopPlaying()
	{
		envelope.release();
	}
	
	public Oscillator getOscillator()
	{
		return oscillator;
	}
	
	public Envelope getEnvelope()
	{
		return envelope;
	}

	@Override
	public short requestNextSample(int outputWireIndex) 
	{
		short value = envelope.requestNextSample(0);
		return value;
	}

}

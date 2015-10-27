package containers;

import modules.Envelope;
import modules.Oscillator;
import engine.ModuleContainer;
import engine.SynthesizerEngine;
import engine.Wire;

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
		new Wire(envelope, oscillator, 0, 0);
	}
	
	public void startPlaying(float frequency, float amplitude)
	{
		oscillator.setFrequency(frequency);
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
	public float requestNextSample() 
	{
		float value = envelope.requestNextSample();
		return value;
	}

}

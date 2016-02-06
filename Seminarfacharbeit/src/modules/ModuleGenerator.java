package modules;

import engine.Module;
import engine.SynthesizerEngine;

public class ModuleGenerator 
{
	
	public static Module createModule(ModuleType type, SynthesizerEngine engine, String name, int id)
	{
		switch(type)
		{
		case OSCILLATOR:
			return new Oscillator(engine, id, name);
		case LOWPASS:
			return new LowpassFilter(engine, id, name);
		case HIGHPASS:
			return new HighpassFilter(engine, id, name);
		case ENVELOPE:
			return new Envelope(engine, id, name);
		case MIXER:
			return new OutputMixer(engine, id, name);
		case BALANCED_MIXER:
			return new BalancedMixer(engine,id, name);
		case AMPLIFIER:
			return new Amplifier(engine, id, name);
		case CONSTANT:
			return new Constant(engine, id, name);
		default:
			return null;
		}
	}
	
	public static Mixer createMixer(SynthesizerEngine engine, String name, int id, int numInputs)
	{
		return new Mixer(engine, numInputs, id, name);
	}

}

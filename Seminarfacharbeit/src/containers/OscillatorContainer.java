package containers;

import modules.BalancedMixer;
import modules.Constant;
import modules.Envelope;
import modules.Ids;
import modules.LowpassFilter;
import modules.Mixer;
import modules.Oscillator;
import modules.listener.EnvelopeFinishedListener;
import engine.ModuleContainer;
import engine.SynthesizerEngine;

public class OscillatorContainer extends ModuleContainer implements EnvelopeFinishedListener
{
	public OscillatorContainer(SynthesizerEngine parent) 
	{
		super(parent, 1, 1, Ids.ID_CONTAINER);
		initModules();
	}
	
	private void initModules()
	{
		addModule(new Constant(parent, Ids.ID_CONSTANT_FREQUENCY_1));
		addModule(new Constant(parent, Ids.ID_CONSTANT_AMPLITUDE_1));
		addModule(new Constant(parent, Ids.ID_CONSTANT_OSCITYPE_1));
		
		addModule(new Constant(parent, Ids.ID_CONSTANT_FREQUENCY_2));
		addModule(new Constant(parent, Ids.ID_CONSTANT_AMPLITUDE_2));
		addModule(new Constant(parent, Ids.ID_CONSTANT_OSCITYPE_2));
		
		addModule(new Constant(parent, Ids.ID_CONSTANT_OSCIBALANCE_1));
		
		addModule(new Constant(parent, Ids.ID_CONSTANT_ATTACK_1));
		addModule(new Constant(parent, Ids.ID_CONSTANT_DECAY_1));
		addModule(new Constant(parent, Ids.ID_CONSTANT_SUSTAIN_1));
		addModule(new Constant(parent, Ids.ID_CONSTANT_RELEASE_1));
		addModule(new Constant(parent, Ids.ID_CONSTANT_STEEPNESS_1));
		addModule(new Constant(parent, Ids.ID_CONSTANT_STARTLEVEL_1));
		addModule(new Constant(parent, Ids.ID_CONSTANT_PEAKLEVEL_1));

		addModule(new Constant(parent, Ids.ID_CONSTANT_CUTOFF_1));
		addModule(new Constant(parent, Ids.ID_CONSTANT_RESONANCE_1));

		addModule(new Constant(parent, Ids.ID_CONSTANT_ATTACK_2));
		addModule(new Constant(parent, Ids.ID_CONSTANT_DECAY_2));
		addModule(new Constant(parent, Ids.ID_CONSTANT_SUSTAIN_2));
		addModule(new Constant(parent, Ids.ID_CONSTANT_RELEASE_2));
		addModule(new Constant(parent, Ids.ID_CONSTANT_STEEPNESS_2));
		addModule(new Constant(parent, Ids.ID_CONSTANT_STARTLEVEL_2));
		addModule(new Constant(parent, Ids.ID_CONSTANT_PEAKLEVEL_2));
		
		
		addModule(new Oscillator(parent, Ids.ID_OSCILLATOR_1));
		addModule(new Oscillator(parent, Ids.ID_OSCILLATOR_2));
		addModule(new Envelope(parent, Ids.ID_ENVELOPE_1, this));
		addModule(new Envelope(parent, Ids.ID_ENVELOPE_2, this));
		addModule(new LowpassFilter(parent, Ids.ID_LOWPASS_1));
		addModule(new BalancedMixer(parent, Ids.ID_MIXER_2));
				
		addConnection(findModuleById(Ids.ID_CONSTANT_FREQUENCY_1), findModuleById(Ids.ID_OSCILLATOR_1), Constant.VALUE_OUTPUT, Oscillator.FREQUENCY_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_AMPLITUDE_1), findModuleById(Ids.ID_OSCILLATOR_1), Constant.VALUE_OUTPUT, Oscillator.AMPLITUDE_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_OSCITYPE_1), findModuleById(Ids.ID_OSCILLATOR_1), Constant.VALUE_OUTPUT, Oscillator.TYPE_INPUT);
		
		addConnection(findModuleById(Ids.ID_CONSTANT_FREQUENCY_2), findModuleById(Ids.ID_OSCILLATOR_2), Constant.VALUE_OUTPUT, Oscillator.FREQUENCY_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_AMPLITUDE_2), findModuleById(Ids.ID_OSCILLATOR_2), Constant.VALUE_OUTPUT, Oscillator.AMPLITUDE_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_OSCITYPE_2), findModuleById(Ids.ID_OSCILLATOR_2), Constant.VALUE_OUTPUT, Oscillator.TYPE_INPUT);
		
		addConnection(findModuleById(Ids.ID_CONSTANT_ATTACK_1), findModuleById(Ids.ID_ENVELOPE_1), Constant.VALUE_OUTPUT, Envelope.ATTACK_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_DECAY_1), findModuleById(Ids.ID_ENVELOPE_1), Constant.VALUE_OUTPUT, Envelope.DECAY_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_SUSTAIN_1), findModuleById(Ids.ID_ENVELOPE_1), Constant.VALUE_OUTPUT, Envelope.SUSTAIN_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_RELEASE_1), findModuleById(Ids.ID_ENVELOPE_1), Constant.VALUE_OUTPUT, Envelope.RELEASE_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_STEEPNESS_1), findModuleById(Ids.ID_ENVELOPE_1), Constant.VALUE_OUTPUT, Envelope.STEEPNESS_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_STARTLEVEL_1), findModuleById(Ids.ID_ENVELOPE_1), Constant.VALUE_OUTPUT, Envelope.STARTLEVEL_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_PEAKLEVEL_1), findModuleById(Ids.ID_ENVELOPE_1), Constant.VALUE_OUTPUT, Envelope.PEAKLEVEL_INPUT);
		
		addConnection(findModuleById(Ids.ID_CONSTANT_CUTOFF_1), findModuleById(Ids.ID_ENVELOPE_2), Constant.VALUE_OUTPUT, Envelope.SAMPLE_INPUT);
		addConnection(findModuleById(Ids.ID_ENVELOPE_2), findModuleById(Ids.ID_LOWPASS_1), Envelope.SAMPLE_OUTPUT, LowpassFilter.CUTOFF_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_RESONANCE_1), findModuleById(Ids.ID_LOWPASS_1), Constant.VALUE_OUTPUT, LowpassFilter.RESONANCE_INPUT);
		
		addConnection(findModuleById(Ids.ID_CONSTANT_ATTACK_2), findModuleById(Ids.ID_ENVELOPE_2), Constant.VALUE_OUTPUT, Envelope.ATTACK_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_DECAY_2), findModuleById(Ids.ID_ENVELOPE_2), Constant.VALUE_OUTPUT, Envelope.DECAY_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_SUSTAIN_2), findModuleById(Ids.ID_ENVELOPE_2), Constant.VALUE_OUTPUT, Envelope.SUSTAIN_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_RELEASE_2), findModuleById(Ids.ID_ENVELOPE_2), Constant.VALUE_OUTPUT, Envelope.RELEASE_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_STEEPNESS_2), findModuleById(Ids.ID_ENVELOPE_2), Constant.VALUE_OUTPUT, Envelope.STEEPNESS_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_STARTLEVEL_2), findModuleById(Ids.ID_ENVELOPE_2), Constant.VALUE_OUTPUT, Envelope.STARTLEVEL_INPUT);
		addConnection(findModuleById(Ids.ID_CONSTANT_PEAKLEVEL_2), findModuleById(Ids.ID_ENVELOPE_2), Constant.VALUE_OUTPUT, Envelope.PEAKLEVEL_INPUT);
		
		addConnection(findModuleById(Ids.ID_CONSTANT_OSCIBALANCE_1), findModuleById(Ids.ID_MIXER_2), Constant.VALUE_OUTPUT, BalancedMixer.BALANCE_INPUT);
		
		addConnection(findModuleById(Ids.ID_OSCILLATOR_1), findModuleById(Ids.ID_MIXER_2), Oscillator.SAMPLE_OUTPUT, BalancedMixer.SAMPLE_INPUT_1);
		addConnection(findModuleById(Ids.ID_OSCILLATOR_2), findModuleById(Ids.ID_MIXER_2), Oscillator.SAMPLE_OUTPUT, BalancedMixer.SAMPLE_INPUT_2);
		addConnection(findModuleById(Ids.ID_MIXER_2), findModuleById(Ids.ID_LOWPASS_1), Mixer.SAMPLE_OUTPUT, LowpassFilter.SAMPLE_INPUT);
		addConnection(findModuleById(Ids.ID_LOWPASS_1), findModuleById(Ids.ID_ENVELOPE_1), LowpassFilter.SAMPLE_OUTPUT, Envelope.SAMPLE_INPUT);
		addConnection(findModuleById(Ids.ID_ENVELOPE_1), this, Envelope.SAMPLE_OUTPUT, ModuleContainer.SAMPLE_INPUT);
	}
	
	public void startPlaying(float frequency, float amplitude)
	{
		((Constant)findModuleById(Ids.ID_CONSTANT_FREQUENCY_1)).setValue(frequency);
		((Constant)findModuleById(Ids.ID_CONSTANT_AMPLITUDE_1)).setValue(amplitude);
		((Constant)findModuleById(Ids.ID_CONSTANT_FREQUENCY_2)).setValue(frequency);
		((Constant)findModuleById(Ids.ID_CONSTANT_AMPLITUDE_2)).setValue(amplitude);
		
		((Constant)findModuleById(Ids.ID_CONSTANT_STARTLEVEL_1)).setValue(0.0F);
		((Constant)findModuleById(Ids.ID_CONSTANT_PEAKLEVEL_1)).setValue(1.0F);
		((Envelope)findModuleById(Ids.ID_ENVELOPE_1)).start();
		
		((Constant)findModuleById(Ids.ID_CONSTANT_STARTLEVEL_2)).setValue(1.0F);
		((Constant)findModuleById(Ids.ID_CONSTANT_PEAKLEVEL_2)).setValue(1.0F / findModuleById(Ids.ID_CONSTANT_CUTOFF_1).requestNextSample(Constant.VALUE_OUTPUT));
		((Envelope)findModuleById(Ids.ID_ENVELOPE_2)).start();
	}
	
	public void stopPlaying()
	{
		((Envelope)findModuleById(Ids.ID_ENVELOPE_1)).release();
		((Envelope)findModuleById(Ids.ID_ENVELOPE_2)).release();
	}

	@Override
	public void onEnvelopeFinished(Envelope envelope) 
	{
		if (envelope.getId() == Ids.ID_ENVELOPE_1)
			onFinished();
	}

	@Override
	public float calcNextSample(int index) 
	{
		float sample = inputWires[SAMPLE_INPUT].getNextSample();
		return sample;
	}

	@Override
	public float calcNextDisabledSample(int index) {
		float sample = inputWires[SAMPLE_INPUT].getNextSample();
		return sample;
	}
}

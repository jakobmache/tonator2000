package presets;

import modules.Ids;
import modules.Oscillator;
import containers.ContainerPreset;

public class OscillatorContainerPreset extends ContainerPreset
{
	
	public OscillatorContainerPreset()
	{
		setParam(Ids.ID_CONSTANT_OSCITYPE_1, Oscillator.TYPE_SINE);
		setParam(Ids.ID_CONSTANT_STEEPNESS_1, -3.0F);
		setParam(Ids.ID_CONSTANT_ATTACK_1, 100F);
		setParam(Ids.ID_CONSTANT_DECAY_1, 100F);
		setParam(Ids.ID_CONSTANT_SUSTAIN_1, 0.8F);
		setParam(Ids.ID_CONSTANT_RELEASE_1, 100F);
		setParam(Ids.ID_CONSTANT_CUTOFF_1, 1.0F);
		setParam(Ids.ID_CONSTANT_RESONANCE_1, 0.0F);
		
		setParam(Ids.ID_CONSTANT_STEEPNESS_2, -3.0F);
		setParam(Ids.ID_CONSTANT_ATTACK_2, 1000);
		setParam(Ids.ID_CONSTANT_DECAY_2, 1000);
		setParam(Ids.ID_CONSTANT_SUSTAIN_2, 1.5F);
		setParam(Ids.ID_CONSTANT_RELEASE_2, 400F);
		
		setParam(Ids.ID_CONSTANT_STARTLEVEL_1, 0F);
		setParam(Ids.ID_CONSTANT_PEAKLEVEL_1, 1.0F);
		
		setParam(Ids.ID_CONSTANT_STARTLEVEL_2, 0.5F);
		setParam(Ids.ID_CONSTANT_PEAKLEVEL_2, 1.0F);
	}

}

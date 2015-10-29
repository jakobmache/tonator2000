package presets;

import containers.ContainerPreset;
import modules.Ids;
import modules.Oscillator;

public class OscillatorContainerPreset extends ContainerPreset
{
	
	public OscillatorContainerPreset()
	{
		setParam(Ids.ID_CONSTANT_OSCITYPE_1, Oscillator.TYPE_SAW);
		setParam(Ids.ID_CONSTANT_STEEPNESS_1, -3.0F);
		setParam(Ids.ID_CONSTANT_ATTACK_1, 100F);
		setParam(Ids.ID_CONSTANT_DECAY_1, 100F);
		setParam(Ids.ID_CONSTANT_SUSTAIN_1, 0.8F);
		setParam(Ids.ID_CONSTANT_RELEASE_1, 100F);
		setParam(Ids.ID_CONSTANT_CUTOFF_1, 1000F);
		setParam(Ids.ID_CONSTANT_RESONANCE_1, 0.0F);
	}

}

package modules;

import java.util.concurrent.atomic.AtomicInteger;

public class Ids 
{
	//Enthält die Ids für den Standardaufbau
	public static final int ID_AMPLIFIER_1 = 0;
	public static final int ID_ENVELOPE_1 = 1;
	public static final int ID_LOWPASS_1 = 2;
	public static final int ID_MIXER_1 = 3;
	public static final int ID_OSCILLATOR_1 = 4;
	public static final int ID_OSCILLATOR_2 = 5;
	public static final int ID_OUTPUT_1 = 6;
	public static final int ID_SAMPLE_FILTER_1 = 7;
	public static final int ID_ENVELOPE_2 = 8;
	public static final int ID_MIXER_2 = 9;
	
	public static final int ID_CONSTANT_CUTOFF_1 = 100;
	public static final int ID_CONSTANT_RESONANCE_1 = 101;
	public static final int ID_CONSTANT_FREQUENCY_1 = 102;
	public static final int ID_CONSTANT_AMPLITUDE_1 = 103;
	public static final int ID_CONSTANT_ATTACK_1 = 104;
	public static final int ID_CONSTANT_DECAY_1 = 105;
	public static final int ID_CONSTANT_SUSTAIN_1 = 106;
	public static final int ID_CONSTANT_RELEASE_1 = 107;
	public static final int ID_CONSTANT_STEEPNESS_1 = 108;
	public static final int ID_CONSTANT_OSCITYPE_1 = 109;
	public static final int ID_CONSTANT_ATTACK_2 = 110;
	public static final int ID_CONSTANT_DECAY_2 = 111;
	public static final int ID_CONSTANT_SUSTAIN_2 = 112;
	public static final int ID_CONSTANT_RELEASE_2 = 113;
	public static final int ID_CONSTANT_STEEPNESS_2 = 114;
	public static final int ID_CONSTANT_FREQUENCY_2 = 115;
	public static final int ID_CONSTANT_AMPLITUDE_2 = 116;
	public static final int ID_CONSTANT_OSCITYPE_2 = 117;
	
	public static final int ID_CONSTANT_STARTLEVEL_1 = 118;
	public static final int ID_CONSTANT_STARTLEVEL_2 = 119;
	public static final int ID_CONSTANT_PEAKLEVEL_1 = 120;
	public static final int ID_CONSTANT_PEAKLEVEL_2 = 121;
	
	public static final int ID_CONSTANT_RESONANCE_2 = 123;
	public static final int ID_CONSTANT_CUTOFF_2 = 124;
	
	public static final int ID_VOLUME = 122;
	
	public static final int ID_HIGHPASS_1 = 125;
	
	public static final int ID_CONSTANT_OSCIBALANCE_1 = 1000;
	
	public static final int ID_CONTAINER = 200;
	
	private static AtomicInteger counter = new AtomicInteger(0);
	
	public static synchronized int getNextId()
	{
		return counter.getAndIncrement();
	}
	
}

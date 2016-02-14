package modules;

import engine.Module;
import engine.SynthesizerEngine;
import resources.Strings;

public class Envelope extends Module
{
	public static final int SAMPLE_INPUT = 0;
	public static final int ATTACK_INPUT = 1;
	public static final int DECAY_INPUT = 2;
	public static final int SUSTAIN_INPUT = 3;
	public static final int RELEASE_INPUT = 4;
	public static final int STEEPNESS_INPUT = 5;
	public static final int STARTLEVEL_INPUT = 6;
	public static final int PEAKLEVEL_INPUT = 7;
	public static final int SAMPLE_OUTPUT = 0;

	private float attackTime = 100;
	private float decayTime = 100;
	private float sustainLevel = 0.8F;
	private float peakLevel = 1F;
	private float releaseTime = 100;

	private float startLevel = 0;

	private float[] increases = new float[3];	
	private float[] startAmplitudes = new float[3];
	private float[] endAmplitudes = new float[3];
	private float[] numSamples = new float[3];
	private int[] sampleCounter = new int[3];

	private float xStart = 0;
	private float xEnd = 1;
	
	private float currFactor = 0;

	private float steepness = -2.0F;
	private float precalc = 0;

	private int phase = ATTACK;
	private final static int ATTACK = 0;
	private final static int DECAY = 1;
	public final static int RELEASE = 2;
	private final static int SUSTAIN = 3;

	/**
	 * Eine ADSR-Hüllkurve verändert den Amplitudenverlauf eines Parameters.
	 * 
	 * @param parent Engine
	 * @param id ID
	 * @param name Name
	 */
	public Envelope(SynthesizerEngine parent, int id, String name)  
	{
		super(parent, 8, 1, id, name);
		
		type = ModuleType.ENVELOPE;
		
		inputNames[SAMPLE_INPUT] = Strings.PARAM_NAMES_MAIN[ENVELOPE][0];
	}


	@Override
	public float calcNextSample(int index)
	{	
		float currValue = inputWires[SAMPLE_INPUT].getNextSample();
		
		if (attackTime != inputWires[ATTACK_INPUT].getNextSample() ||
				decayTime != inputWires[DECAY_INPUT].getNextSample() ||
				sustainLevel != inputWires[SUSTAIN_INPUT].getNextSample() ||
				releaseTime != inputWires[RELEASE_INPUT].getNextSample() ||
				steepness != inputWires[STEEPNESS_INPUT].getNextSample() ||
				peakLevel != inputWires[PEAKLEVEL_INPUT].getNextSample() ||
				startLevel != inputWires[STARTLEVEL_INPUT].getNextSample())
		{
			updateValues();
		}
		
		if (phase == SUSTAIN)
		{
			currFactor = sustainLevel;
		}
		else 
		{
			currFactor = (float) (Math.pow(Math.E, steepness * sampleCounter[phase] * increases[phase]) - Math.pow(Math.E, steepness * xStart));
			currFactor = precalc * currFactor;
			currFactor += startAmplitudes[phase];

			sampleCounter[phase]++;
			
			if (phase == ATTACK && sampleCounter[ATTACK] > numSamples[ATTACK])
			{
				setPhase(DECAY);
			}
			else if (phase == DECAY && sampleCounter[DECAY] > numSamples[DECAY])
				setPhase(SUSTAIN);
			else if (phase == RELEASE && sampleCounter[RELEASE] >= numSamples[RELEASE])
			{
				currFactor = endAmplitudes[RELEASE];
			}
		}
		
		currValue *= currFactor;
		
		return currValue;

	}

	public void setPhase(int newPhase)
	{
		//Phase und Werte aktualisieren 
		for (int i = 0; i < sampleCounter.length; i++)
		{
			sampleCounter[i] = 0;
		}
		phase = newPhase;
		updateValues();
		if (phase == ATTACK && numSamples[ATTACK] == 0)
			setPhase(DECAY);
		if (phase == DECAY && numSamples[DECAY] == 0)
			setPhase(SUSTAIN);
	}

	private void updateValues()
	{
		//Das wollen wir nur berechnen, wenn sich etwas geändert hat
		attackTime = inputWires[ATTACK_INPUT].getNextSample();
		decayTime = inputWires[DECAY_INPUT].getNextSample();
		sustainLevel = inputWires[SUSTAIN_INPUT].getNextSample();
		releaseTime = inputWires[RELEASE_INPUT].getNextSample();
		steepness = inputWires[STEEPNESS_INPUT].getNextSample();
		startLevel = inputWires[STARTLEVEL_INPUT].getNextSample();
		peakLevel = inputWires[PEAKLEVEL_INPUT].getNextSample();

		if (steepness > 0)
			steepness = -1 * steepness;

		startAmplitudes[ATTACK] = startLevel;
		endAmplitudes[ATTACK] = peakLevel;
		startAmplitudes[DECAY] = peakLevel;
		endAmplitudes[DECAY] = sustainLevel;
		startAmplitudes[RELEASE] = currFactor;
		endAmplitudes[RELEASE] = startLevel;
		increases[ATTACK] = 1 / ((attackTime / 1000) * parent.getSamplingRate());
		increases[DECAY] = 1 / ((decayTime / 1000) * parent.getSamplingRate());
		increases[RELEASE] = 1 / ((releaseTime / 1000) * parent.getSamplingRate());
		numSamples[ATTACK] = Math.round((attackTime / 1000) * parent.getSamplingRate());
		numSamples[DECAY] =  Math.round((decayTime / 1000) * parent.getSamplingRate());
		numSamples[RELEASE] = Math.round((releaseTime / 1000) * parent.getSamplingRate());	
		
		if (phase != SUSTAIN)
		{
			float diffUp = (endAmplitudes[phase] - startAmplitudes[phase]);
			float diffDown = (float) (Math.pow(Math.E, steepness * xEnd) - Math.pow(Math.E, steepness * xStart));
			precalc = diffUp / diffDown;
		}
	}
	
	@Override
	public float calcNextDisabledSample(int index) 
	{
		return inputWires[SAMPLE_INPUT].getNextSample();
	}

	public void start()
	{
		setPhase(ATTACK);
	}

	public void release()
	{
		setPhase(RELEASE);
	}

	public float getAttackTime() {
		return attackTime;
	}

	public float getDecayTime() {
		return decayTime;
	}

	public float getSustainLevel() {
		return sustainLevel;
	}

	public float getReleaseTime() {
		return releaseTime;
	}
}

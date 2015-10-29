package modules;

import modules.listener.EnvelopeFinishedListener;
import engine.Module;
import engine.SynthesizerEngine;

public class Envelope extends Module
{
	private EnvelopeFinishedListener listener;
	
	public static final int SAMPLE_INPUT = 0;
	public static final int ATTACK_INPUT = 1;
	public static final int DECAY_INPUT = 2;
	public static final int SUSTAIN_INPUT = 3;
	public static final int RELEASE_INPUT = 4;
	public static final int STEEPNESS_INPUT = 5;
	public static final int SAMPLE_OUTPUT = 0;

	private float attackTime = 100;
	private float decayTime = 100;
	private float sustainLevel = 0.8F;
	private float peakLevel = 1F;
	private float releaseTime = 100;

	private float maxValue = Short.MAX_VALUE;

	private float[] increases = new float[3];	
	private float[] startAmplitudes = new float[3];
	private float[] endAmplitudes = new float[3];
	private float[] numSamples = new float[3];
	private int[] sampleCounter = new int[3];

	private float xStart = 0;
	private float xEnd = 1;

	private float factor = -2.0F;
	private float precalc = 0;

	private Constant constant;
	
	private boolean finished = false;

	private int phase = ATTACK;
	private final static int ATTACK = 0;
	private final static int DECAY = 1;
	public final static int RELEASE = 2;
	private final static int SUSTAIN = 3;

	public Envelope(SynthesizerEngine parent, Constant value, int id, EnvelopeFinishedListener listener) 
	{
		super(parent, 6, 1, id);
		constant = value;
		this.listener = listener;
	}

	@Override
	public float requestNextSample(int index)
	{
		if (finished)
		{
			return inputWires[SAMPLE_INPUT].getNextSample();
		}
		
		if (attackTime != inputWires[ATTACK_INPUT].getNextSample() ||
				decayTime != inputWires[DECAY_INPUT].getNextSample() ||
				sustainLevel != inputWires[SUSTAIN_INPUT].getNextSample() ||
				releaseTime != inputWires[RELEASE_INPUT].getNextSample() ||
				factor != inputWires[STEEPNESS_INPUT].getNextSample())
		{
			updateValues();
		}

		if (phase == SUSTAIN)
		{
			constant.setValue(sustainLevel * maxValue);
		}
		else 
		{
			float sample = (float) (Math.pow(Math.E, factor * sampleCounter[phase] * increases[phase]) - Math.pow(Math.E, factor * xStart));
			sample = precalc * sample;
			sample += startAmplitudes[phase];

			sampleCounter[phase]++;

			if (phase == ATTACK && sampleCounter[phase] >= numSamples[ATTACK])
			{
				if (numSamples[DECAY] == 0)
					setPhase(SUSTAIN);
				else 
					setPhase(DECAY);
			}
			else if (phase == DECAY && sampleCounter[phase] >= numSamples[DECAY])
				setPhase(SUSTAIN);
			else if (phase == RELEASE && sampleCounter[phase] >= numSamples[RELEASE])
			{
				sample = 0;
				listener.onEnvelopeFinished(this);
			}

			constant.setValue(sample * maxValue);
		}

		return inputWires[SAMPLE_INPUT].getNextSample();

	}

	public void setPhase(int newPhase)
	{
		for (int i = 0; i < sampleCounter.length; i++)
		{
			sampleCounter[i] = 0;
		}
		phase = newPhase;
		updateValues();
	}

	private void updateValues()
	{
		attackTime = inputWires[ATTACK_INPUT].getNextSample();
		decayTime = inputWires[DECAY_INPUT].getNextSample();
		sustainLevel = inputWires[SUSTAIN_INPUT].getNextSample();
		releaseTime = inputWires[RELEASE_INPUT].getNextSample();
		factor = inputWires[STEEPNESS_INPUT].getNextSample();

		if (factor > 0)
			factor = -1 * factor;

		startAmplitudes[ATTACK] = 0;
		endAmplitudes[ATTACK] = peakLevel;
		startAmplitudes[DECAY] = peakLevel;
		endAmplitudes[DECAY] = sustainLevel;
		startAmplitudes[RELEASE] = constant.requestNextSample(0) / maxValue;
		endAmplitudes[RELEASE] = 0;


		increases[ATTACK] = 1 / ((attackTime / 1000) * parent.getSamplingRate());
		increases[DECAY] = 1 / ((decayTime / 1000) * parent.getSamplingRate());
		increases[RELEASE] = 1 / ((releaseTime / 1000) * parent.getSamplingRate());
		numSamples[ATTACK] = Math.round((attackTime / 1000) * parent.getSamplingRate());
		numSamples[DECAY] =  Math.round((decayTime / 1000) * parent.getSamplingRate());
		numSamples[RELEASE] = Math.round((releaseTime / 1000) * parent.getSamplingRate());	

		if (phase != SUSTAIN)
		{
			float diffUp = (endAmplitudes[phase] - startAmplitudes[phase]);
			float diffDown = (float) (Math.pow(Math.E, factor * xEnd) - Math.pow(Math.E, factor * xStart));
			precalc = diffUp / diffDown;
		}
	}

	public void start()
	{
		finished = false;
		setPhase(ATTACK);
	}

	public void release()
	{
		setPhase(RELEASE);
	}

	public float getAttackTime() {
		return attackTime;
	}

	public void setAttackTime(float attackTime) {
		this.attackTime = attackTime;
		updateValues();
	}

	public float getDecayTime() {
		return decayTime;
	}

	public void setDecayTime(float decayTime) {
		this.decayTime = decayTime;
		updateValues();
	}

	public float getSustainLevel() {
		return sustainLevel;
	}

	public void setSustainLevel(float sustainLevel) {
		this.sustainLevel = sustainLevel;
		updateValues();
	}

	public float getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(float releaseTime) {
		this.releaseTime = releaseTime;
		updateValues();
	}
	
	public boolean isFinished()
	{
		return isFinished();
	}

}

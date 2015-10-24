package modules;

import engine.Module;
import engine.SynthesizerEngine;

public class Envelope extends Module
{

	private float attackTime = 100;
	private float decayTime = 100;
	private float sustainLevel = 1F;
	private float releaseTime = 100;

	private short maxValue = Short.MAX_VALUE;

	private float attackIncrease;
	private float decayIncrease;
	private float releaseIncrease;
	
	private float amplitudeValue = 0;

	private int attackSamples;
	private int decaySamples;
	private int releaseSamples;

	private int sampleCounter = 0;

	private Oscillator oscillator;
	
	private int phase = PHASE_ATTACK;
	private final static int PHASE_ATTACK = 0;
	private final static int PHASE_DECAY = 1;
	private final static int PHASE_SUSTAIN = 2;
	public final static int PHASE_RELEASE = 3;

	public Envelope(SynthesizerEngine parent, Oscillator osci) 
	{
		super(parent, 1, 1);
		oscillator = osci;
		updateValues();
	}

	@Override
	public short requestNextSample(int outputWireIndex)
	{
		switch (phase)
		{

		case PHASE_ATTACK:
			if (sampleCounter < attackSamples)
			{
				oscillator.setAmplitude(amplitudeValue + attackIncrease);
				amplitudeValue += attackIncrease;
				sampleCounter++;
			}
			else 
			{
				setPhase(PHASE_DECAY);
			}
			return oscillator.requestNextSample(0);
					
				
		case PHASE_DECAY:
			if (sampleCounter < decaySamples)
			{
				oscillator.setAmplitude(amplitudeValue + decayIncrease);
				amplitudeValue += decayIncrease;
				sampleCounter++;
			}
			else 
			{
				setPhase(PHASE_SUSTAIN);
				oscillator.setAmplitude(sustainLevel * maxValue);
			}
			return oscillator.requestNextSample(0);
			
		case PHASE_SUSTAIN:
			return oscillator.requestNextSample(0);
			
		case PHASE_RELEASE:
			if (sampleCounter < releaseSamples)
			{
				oscillator.setAmplitude(amplitudeValue + releaseIncrease);
				amplitudeValue += releaseIncrease;
				sampleCounter++;
			}
			else if (sampleCounter == releaseSamples)
			{
				oscillator.setAmplitude(0);
			}
			else 
			{
				sampleCounter++;
				return 0;
			}
			return oscillator.requestNextSample(0);
			
		default:
			return 0;
		}

	}

	public void setPhase(int newPhase)
	{
		phase = newPhase;
		
		sampleCounter = 0;
		
		if (newPhase == PHASE_ATTACK)
			amplitudeValue = 0;

		
		if (newPhase == PHASE_RELEASE)
		{
			releaseSamples = (int) ((0.001 * releaseTime) * parent.getSamplingRate());
			releaseIncrease = (float) (0 - (amplitudeValue)) / releaseSamples;
		}
		
		
	}

	private void updateValues()
	{
		attackSamples = (int) ((0.001 * attackTime) * parent.getSamplingRate());
		attackIncrease = maxValue / ((float) attackSamples);

		decaySamples = (int)((0.001 * decayTime) * parent.getSamplingRate());
		decayIncrease = ((sustainLevel * maxValue) - maxValue) / decaySamples;
	}

	public void start()
	{
		setPhase(PHASE_ATTACK);
	}

	public void release()
	{
		setPhase(PHASE_RELEASE);
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
	
	public void setMaxValue(Short newValue)
	{
		maxValue = newValue;
	}

}

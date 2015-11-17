package test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.sound.sampled.LineUnavailableException;

import modules.Envelope;

public class TestClass
{

	private float attackTime = 100;
	private float decayTime = 100;
	private float sustainLevel = 0.8F;
	private float peakLevel = 1F;
	private float releaseTime = 1000;
	
	private float maxValue = Short.MAX_VALUE;
	private float value = 0;

	private float[] increases = new float[3];	
	private float[] startAmplitudes = new float[3];
	private float[] endAmplitudes = new float[3];
	private float[] numSamples = new float[3];
	private float[] sampleCounter = new float[3];

	private float xStart = 0;
	private float xEnd = 1;

	private float steepness = -3F;
	private float precalc = 0;

	private int phase = ATTACK;
	private final static int ATTACK = 0;
	private final static int DECAY = 1;
	public final static int RELEASE = 2;
	private final static int SUSTAIN = 3;

	public float requestNextSample(int index)
	{
		updateValues();

		if (index != Envelope.SAMPLE_OUTPUT)
			return 0;

		float factor;
		
		if (phase == SUSTAIN)
		{
			factor = sustainLevel;
		}
		else 
		{
			factor = (float) (Math.pow(Math.E, steepness * sampleCounter[phase] * increases[phase]) - Math.pow(Math.E, steepness * xStart));
			factor = precalc * factor;
			factor += startAmplitudes[phase];

			sampleCounter[phase]++;
			
			if (phase == ATTACK && sampleCounter[ATTACK] > numSamples[ATTACK])
			{
				setPhase(DECAY);
			}
			else if (phase == DECAY && sampleCounter[DECAY] > numSamples[DECAY])
				setPhase(SUSTAIN);
			else if (phase == RELEASE && sampleCounter[RELEASE] > numSamples[RELEASE])
			{
				factor = endAmplitudes[RELEASE];
			}
		}
//		
//		if (factor < 0)
//			factor *= -1;

		return factor;
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
		if (steepness > 0)
			steepness = -1 * steepness;

		startAmplitudes[ATTACK] = 0;
		endAmplitudes[ATTACK] = peakLevel;
		startAmplitudes[DECAY] = peakLevel;
		endAmplitudes[DECAY] = sustainLevel;
		startAmplitudes[RELEASE] = sustainLevel;
		endAmplitudes[RELEASE] = 0;

		increases[ATTACK] = 1 / ((attackTime / 1000) * 44100);
		increases[DECAY] = 1 / ((decayTime / 1000) * 44100);
		increases[RELEASE] = 1 / ((releaseTime / 1000) * 44100);
		numSamples[ATTACK] = Math.round((attackTime / 1000) * 44100);
		numSamples[DECAY] =  Math.round((decayTime / 1000) * 44100);
		numSamples[RELEASE] = Math.round((releaseTime / 1000) * 44100);	
		

		if (phase != SUSTAIN)
		{
			float diffUp = (endAmplitudes[phase] - startAmplitudes[phase]);
			float diffDown = (float) (Math.pow(Math.E, steepness * xEnd) - Math.pow(Math.E, steepness * xStart));
			precalc = diffUp / diffDown;
		}
	}

	public void start()
	{
		setPhase(ATTACK);
	}

	public void release()
	{
		setPhase(RELEASE);
	}

	public static void main(String[] args) throws LineUnavailableException, FileNotFoundException 
	{

		PrintWriter writer = new PrintWriter("ausgabeEnvelope.txt");
		TestClass test = new TestClass();
		test.start();
		for (int i = 0; i < 100000; i++)
		{

			float sample = test.requestNextSample(0);
			writer.println(sample);
			//System.out.println(sample);
		}
		test.release();
		for (int i = 0; i < 100000; i++)
		{
			writer.println(test.requestNextSample(0));
		}
		writer.close();
	}
}
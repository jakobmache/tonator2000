package engine;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;

import midi.MidiUtils;
import threads.PlayThread;
import containers.StandardModuleContainer;

public class SynthesizerEngine implements Receiver
{

	private AudioFormat audioFormat;
	private float samplingRate = 44100;
	private int numChannels = 1;
	private boolean signed = true;
	private boolean bigEndian = true;

	private int sampleSizeInBits = 16;
	private int sampleSizeInBytes = 2;

	private double bufferTime = 0.01;
	
	private Map <Integer, ModuleContainer> currentNotes = new HashMap<Integer, ModuleContainer>();

	public SynthesizerEngine()
	{
		updateAudioFormat();

	}

	private void updateAudioFormat()
	{
		audioFormat = new AudioFormat(samplingRate, sampleSizeInBits, numChannels, signed, bigEndian);
	}

	public AudioFormat getAudioFormat() {
		return audioFormat;
	}

	public float getSamplingRate() {
		return samplingRate;
	}

	public void setSamplingRate(float samplingRate) {
		this.samplingRate = samplingRate;
	}

	public int getNumChannels() {
		return numChannels;
	}

	public void setNumChannels(int numChannels) {
		this.numChannels = numChannels;
	}

	public boolean isSigned() {
		return signed;
	}

	public void setSigned(boolean signed) {
		this.signed = signed;
	}

	public boolean isBigEndian() {
		return bigEndian;
	}

	public void setBigEndian(boolean bigEndian) {
		this.bigEndian = bigEndian;
	}

	public int getSampleSizeInBits() {
		return sampleSizeInBits;
	}

	public void setSampleSizeInBits(int sampleSizeInBits) {
		this.sampleSizeInBits = sampleSizeInBits;
	}

	public int getSampleSizeInBytes() {
		return sampleSizeInBytes;
	}

	public void setSampleSizeInBytes(int sampleSizeInBytes) {
		this.sampleSizeInBytes = sampleSizeInBytes;
	}

	public double getBufferTime() {
		return bufferTime;
	}

	public void setBufferTime(double bufferTime) {
		this.bufferTime = bufferTime;
	}

	@Override
	public void send(MidiMessage message, long timeStamp) 
	{
		if (message instanceof ShortMessage)
		{
			handleMessage((ShortMessage) message); 
		}

	}

	private void handleMessage(ShortMessage message)
	{

		if (message.getCommand() == ShortMessage.NOTE_ON)
		{
			System.out.println("Note on! - " + message.getData1());
			int key = message.getData1();
			float frequency = MidiUtils.midiNoteNumberToFrequency(key);
			
			StandardModuleContainer container = null;

			try 
			{
				container = new StandardModuleContainer(this);
			} 
			catch (LineUnavailableException e) 
			{
				e.printStackTrace();
			}
			
			container.getOscillator().setFrequency((double) frequency);
			container.getOscillator().setAmplitude(Short.MAX_VALUE);
			
			currentNotes.put(key, container);

			PlayThread thread = new PlayThread(container.getOutputModule());
			thread.start();
		}

		else if (message.getCommand() == ShortMessage.NOTE_OFF)
		{
			System.out.println("Note off! - " + message.getData1());
			StandardModuleContainer container = (StandardModuleContainer) currentNotes.get(message.getData1());
			container.getOutputModule().stopPlaying();
			currentNotes.remove(message.getData1());
		}
	}
	
	public void close()
	{
		for (ModuleContainer container:currentNotes.values())
		{
			container.getOutputModule().stopPlaying();
		}
	}

}

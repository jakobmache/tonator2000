package engine;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;

import midi.MidiUtils;
import modules.Oscillator;
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
	
	private Map<Integer, ModuleContainer> currentNotes = new HashMap<Integer, ModuleContainer>();
	private Map<Integer, ModuleContainer> existingContainers = new HashMap<Integer, ModuleContainer>();

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
		updateAudioFormat();
	}

	public int getNumChannels() {
		return numChannels;
	}

	public void setNumChannels(int numChannels) {
		this.numChannels = numChannels;
		updateAudioFormat();
	}

	public boolean isSigned() {
		return signed;
	}

	public void setSigned(boolean signed) {
		this.signed = signed;
		updateAudioFormat();
	}

	public boolean isBigEndian() {
		return bigEndian;
	}

	public void setBigEndian(boolean bigEndian) {
		this.bigEndian = bigEndian;
		updateAudioFormat();
	}

	public int getSampleSizeInBits() {
		return sampleSizeInBits;
	}

	public void setSampleSizeInBits(int sampleSizeInBits) {
		this.sampleSizeInBits = sampleSizeInBits;
		updateAudioFormat();
	}

	public int getSampleSizeInBytes() {
		return sampleSizeInBytes;
	}

	public void setSampleSizeInBytes(int sampleSizeInBytes) {
		this.sampleSizeInBytes = sampleSizeInBytes;
		updateAudioFormat();
	}

	public double getBufferTime() {
		return bufferTime;
	}

	public void setBufferTime(double bufferTime) {
		this.bufferTime = bufferTime;updateAudioFormat();
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
			int key = message.getData1();
			int velocity = message.getData2();
			
			System.out.println("Note on! - " + key + " - " + velocity);
			float frequency = MidiUtils.midiNoteNumberToFrequency(key);
			
			if (currentNotes.keySet().contains(key))
			{
				System.out.println("Note wird schon abgespielt - Fehler!");
				return;
			}
			
			StandardModuleContainer container = null;

			try 
			{
				if (!existingContainers.containsKey(key))
				{
					container = new StandardModuleContainer(this);
					existingContainers.put(key, container);
				}
				else 
				{
					container = (StandardModuleContainer) existingContainers.get(key);
				}
				
				container.getOscillator().setFrequency((double) frequency);
				container.getOscillator().setAmplitude((velocity / 127.0) * Short.MAX_VALUE);
				container.getOscillator().setType(Oscillator.TYPE_SQUARE);
				
				currentNotes.put(key, container);

				PlayThread thread = new PlayThread(container.getOutputModule());
				thread.start();
			} 
			catch (LineUnavailableException e) 
			{
				e.printStackTrace();
			}
			

		}

		else if (message.getCommand() == ShortMessage.NOTE_OFF)
		{
			int key = message.getData1();
			System.out.println("Note off! - " + key);
			
			if (!currentNotes.containsKey(key))
			{
				System.out.println("Note wird nicht abgespielt - Fehler!");
				return;
			}
			StandardModuleContainer container = (StandardModuleContainer) currentNotes.get(key);
			container.getOutputModule().stopPlaying();
			currentNotes.remove(message.getData1());
		}
	}
	
	public void close()
	{
		for (ModuleContainer container:existingContainers.values())
		{
			container.getOutputModule().stopPlaying();
			container.getOutputModule().close();
		}
	}

}

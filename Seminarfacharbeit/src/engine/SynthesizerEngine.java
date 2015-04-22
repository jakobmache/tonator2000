
package engine;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.sampled.AudioFormat;

import events.PlayEvent;
import midi.MidiData;

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
	
	private List<ModuleContainer> containers = new ArrayList<ModuleContainer>();
	private List<Integer> currentNotes = new ArrayList<Integer>();
	private boolean isPlaying = false;
	
	public SynthesizerEngine()
	{
		updateAudioFormat();
	}
	
	@Override
	public void send(MidiMessage message, long timeStamp) 
	{	
		if (message instanceof ShortMessage)
		{
			printMessage((ShortMessage) message);
			createEvent((ShortMessage) message);

		}

		else if (message instanceof SysexMessage)
		{
			System.out.printf("SysexMessage\n");
		}

		else if (message instanceof MetaMessage)
		{
			System.out.printf("MetaMessage\n");
		}

		else
		{
			System.out.printf("Unknows message type: %s", message.getClass());
		}


	}

	@Override
	public void close() 
	{
	}
	
	private void createEvent(ShortMessage message)
	{
		int key = message.getData1();
		float frequency = MidiData.getFrequency(key);
		ModuleContainer container = containers.get(0);
		switch (message.getCommand())
		{
		case ShortMessage.NOTE_ON:
			if (!currentNotes.contains(frequency))
			{
				//Event event = new PlayEvent(frequency, 0, container.getToneModule() , container.getToneModule());
			}
		}
		
	}
	
	private void printMessage(ShortMessage message)
	{	
		String stringMessage = null;
		
		switch (message.getCommand())
		{
		case ShortMessage.NOTE_ON:
			stringMessage = "Note on: " + message.getData1() + " - " + message.getData2(); break;
		
		case ShortMessage.NOTE_OFF:
			stringMessage = "Note off: " + message.getData1() + " - " + message.getData2(); break;
		}
		
		System.out.println(stringMessage);
	}

	private void updateAudioFormat()
	{
		audioFormat = new AudioFormat(samplingRate, sampleSizeInBits, numChannels, signed, bigEndian);
	}

	public void addContainer(ModuleContainer container)
	{
		containers.add(container);
	}
	
	public List<ModuleContainer> getModuleContainers()
	{
		return containers;
	}

	public AudioFormat getAudioFormat()
	{
		return audioFormat;
	}

	public double getBufferTime() {
		return bufferTime;
	}

	public int getNumChannels() {
		return numChannels;
	}

	public int getSampleSizeInBits() {
		return sampleSizeInBits;
	}
	
	public int getSampleSizeInBytes() {
		return sampleSizeInBytes;
	}

	public float getSamplingRate() {
		return samplingRate;
	}

	public boolean isBigEndian() {
		return bigEndian;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public boolean isSigned() {
		return signed;
	}

	public void setBigEndian(boolean bigEndian) {
		this.bigEndian = bigEndian;
		updateAudioFormat();
	}

	public void setBufferTime(double bufferTime) {
		this.bufferTime = bufferTime;
	}

	public void setNumChannels(int numChannels) {
		this.numChannels = numChannels;
		updateAudioFormat();
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	
	public void setSampleSizeInBits(int sampleSizeInBits) {
		this.sampleSizeInBits = sampleSizeInBits;
		updateAudioFormat();
	}

	public void setSampleSizeInBytes(int sampleSizeInBytes) {
		this.sampleSizeInBytes = sampleSizeInBytes;
	}
	
	public void setSamplingRate(float samplingRate) {
		this.samplingRate = samplingRate;
		updateAudioFormat();
	}

	public void setSigned(boolean signed) {
		this.signed = signed;
		updateAudioFormat();
	}


}


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

public class SynthesizerEngine implements Receiver{

	
	//=====================Audioformat=====================
	
	private final int BUFFER_SIZE = 1024;

	private AudioFormat audioFormat;
	private float samplingRate = 44100;
	private int sampleSizeInBits = 16;
	private int numChannels = 1;
	private boolean signed = true;
	private boolean bigEndian = true;
	private List<ModuleContainer> containers = new ArrayList<ModuleContainer>();
	private List<Integer> currentNotes = new ArrayList<Integer>();
	private boolean isPlaying = false;
	private int bufferTime = 1;

	public int getBufferTime() {
		return bufferTime;
	}

	public void setBufferTime(int bufferTime) {
		this.bufferTime = bufferTime;
	}

	public SynthesizerEngine()
	{
		updateAudioFormat();
	}
	
	private void updateAudioFormat()
	{
		audioFormat = new AudioFormat(samplingRate, sampleSizeInBits, numChannels, signed, bigEndian);
	}

	public void run()
	{
	}

	@Override
	public void close() 
	{
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

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
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

	public float getSamplingRate() {
		return samplingRate;
	}

	public void setSamplingRate(float samplingRate) {
		this.samplingRate = samplingRate;
		updateAudioFormat();
	}

	public int getSampleSizeInBits() {
		return sampleSizeInBits;
	}

	public void setSampleSizeInBits(int sampleSizeInBits) {
		this.sampleSizeInBits = sampleSizeInBits;
		updateAudioFormat();
	}

	public int getNumChannels() {
		return numChannels;
	}

	public void setNumChannels(int numChannels) {
		this.numChannels = numChannels;
		updateAudioFormat();
	}

	public int getBufferSize() {
		return BUFFER_SIZE;
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
	
	public AudioFormat getAudioFormat()
	{
		return audioFormat;
	}

	public List<ModuleContainer> getModuleContainers()
	{
		return containers;
	}
	
	public void addContainer(ModuleContainer container)
	{
		containers.add(container);
	}

}

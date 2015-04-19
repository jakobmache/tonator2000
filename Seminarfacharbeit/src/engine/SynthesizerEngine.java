/*
 * 
 */
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

// TODO: Auto-generated Javadoc
/**
 * The Class SynthesizerEngine.
 */
public class SynthesizerEngine implements Receiver{

	
	//=====================Audioformat=====================
	
	/** The audio format. */
	private AudioFormat audioFormat;
	
	/** The sampling rate. */
	private float samplingRate = 44100;
	
	/** The sample size in bits. */
	private int sampleSizeInBits = 16;
	
	/** The num channels. */
	private int numChannels = 1;
	
	/** The signed. */
	private boolean signed = true;
	
	/** The big endian. */
	private boolean bigEndian = true;
	
	
	/** The containers. */
	private List<ModuleContainer> containers = new ArrayList<ModuleContainer>();
	
	/** The current notes. */
	private List<Integer> currentNotes = new ArrayList<Integer>();
	
	/** The is playing. */
	private boolean isPlaying = false;
	
	/** The buffer time. */
	private int bufferTime = 1;
	
	//======================Methoden========================
	
	/**
	 * Gets the buffer time.
	 *
	 * @return the buffer time
	 */
	public int getBufferTime() {
		return bufferTime;
	}

	/**
	 * Sets the buffer time.
	 *
	 * @param bufferTime the new buffer time
	 */
	public void setBufferTime(int bufferTime) {
		this.bufferTime = bufferTime;
	}

	/**
	 * Instantiates a new synthesizer engine.
	 */
	public SynthesizerEngine()
	{
		updateAudioFormat();
	}
	
	/**
	 * Update audio format.
	 */
	private void updateAudioFormat()
	{
		audioFormat = new AudioFormat(samplingRate, sampleSizeInBits, numChannels, signed, bigEndian);
	}

	/**
	 * Run.
	 */
	public void run()
	{
	}

	/* (non-Javadoc)
	 * @see javax.sound.midi.Receiver#close()
	 */
	@Override
	public void close() 
	{
	}

	/* (non-Javadoc)
	 * @see javax.sound.midi.Receiver#send(javax.sound.midi.MidiMessage, long)
	 */
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

	/**
	 * Checks if is playing.
	 *
	 * @return true, if is playing
	 */
	public boolean isPlaying() {
		return isPlaying;
	}

	/**
	 * Sets the playing.
	 *
	 * @param isPlaying the new playing
	 */
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	
	/**
	 * Creates the event.
	 *
	 * @param message the message
	 */
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

	/**
	 * Prints the message.
	 *
	 * @param message the message
	 */
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

	/**
	 * Gets the sampling rate.
	 *
	 * @return the sampling rate
	 */
	public float getSamplingRate() {
		return samplingRate;
	}

	/**
	 * Sets the sampling rate.
	 *
	 * @param samplingRate the new sampling rate
	 */
	public void setSamplingRate(float samplingRate) {
		this.samplingRate = samplingRate;
		updateAudioFormat();
	}

	/**
	 * Gets the sample size in bits.
	 *
	 * @return the sample size in bits
	 */
	public int getSampleSizeInBits() {
		return sampleSizeInBits;
	}

	/**
	 * Sets the sample size in bits.
	 *
	 * @param sampleSizeInBits the new sample size in bits
	 */
	public void setSampleSizeInBits(int sampleSizeInBits) {
		this.sampleSizeInBits = sampleSizeInBits;
		updateAudioFormat();
	}

	/**
	 * Gets the num channels.
	 *
	 * @return the num channels
	 */
	public int getNumChannels() {
		return numChannels;
	}

	/**
	 * Sets the num channels.
	 *
	 * @param numChannels the new num channels
	 */
	public void setNumChannels(int numChannels) {
		this.numChannels = numChannels;
		updateAudioFormat();
	}

	/**
	 * Checks if is signed.
	 *
	 * @return true, if is signed
	 */
	public boolean isSigned() {
		return signed;
	}

	/**
	 * Sets the signed.
	 *
	 * @param signed the new signed
	 */
	public void setSigned(boolean signed) {
		this.signed = signed;
		updateAudioFormat();
	}

	/**
	 * Checks if is big endian.
	 *
	 * @return true, if is big endian
	 */
	public boolean isBigEndian() {
		return bigEndian;
	}

	/**
	 * Sets the big endian.
	 *
	 * @param bigEndian the new big endian
	 */
	public void setBigEndian(boolean bigEndian) {
		this.bigEndian = bigEndian;
		updateAudioFormat();
	}
	
	/**
	 * Gets the audio format.
	 *
	 * @return the audio format
	 */
	public AudioFormat getAudioFormat()
	{
		return audioFormat;
	}

	/**
	 * Gets the module containers.
	 *
	 * @return the module containers
	 */
	public List<ModuleContainer> getModuleContainers()
	{
		return containers;
	}
	
	/**
	 * Adds the container.
	 *
	 * @param container the container
	 */
	public void addContainer(ModuleContainer container)
	{
		containers.add(container);
	}

}

package engine;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.sampled.AudioFormat;

public class SynthesizerEngine implements Receiver{

	
	//=====================Audioformat=====================
	
	private AudioFormat audioFormat;
	
	private float samplingRate = 44100;
	private int sampleSizeInBits = 2;
	private int numChannels = 1;
	private boolean signed = true;
	private boolean bigEndian = true;
	
	
	private List<ModuleContainer> containers = new ArrayList<ModuleContainer>();
	
	//======================Methoden========================
	
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
		//TODO: Handle all types of messages
		
		if (message instanceof ShortMessage)
		{
				printMessage((ShortMessage) message);
				createEvent((ShortMessage) message);
		}

		else if (message instanceof SysexMessage)
		{

		}

		else if (message instanceof MetaMessage)
		{

		}

		else
		{

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
	private Event createEvent(ShortMessage message) 
	{
		//TODO: create Event
		return new Event(1, 1, 1, 1);
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

	public List<ModuleContainer> getModuleContainers()
	{
		return containers;
	}

}

package engine;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;

import modules.InputController;
import modules.Mixer;
import modules.OutputModule;
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
	
	private int startKeyNumber = 0;
	private int endKeyNumber = 100;
	
	private OutputModule outputModule;

	private InputController inputModule;
	private Map<Integer, ModuleContainer> containers;

	public SynthesizerEngine()
	{
		updateAudioFormat();
		initContainers();
		initModules();
	}

	private void initModules()
	{
		inputModule = new InputController(this);
		try 
		{
			outputModule = new OutputModule(this);
		} 
		catch (LineUnavailableException e) 
		{
			e.printStackTrace();
		}
		
		Mixer outputMixer = new Mixer(this, endKeyNumber - startKeyNumber);
		int i = 0;
		for (int key = startKeyNumber; key < endKeyNumber; key++)
		{
			Wire wire = new Wire(outputMixer, containers.get(key), 0, i);
			i++;
		}
		
		Wire mixerOutput = new Wire(outputModule, outputMixer, 0, 0);
		
	}

	private void initContainers()
	{
		containers = new HashMap<Integer, ModuleContainer>();
		for (int key = startKeyNumber; key <= endKeyNumber; key++)
		{
			ModuleContainer container = new StandardModuleContainer(this);
			containers.put(key, container);
		}
		
	}

	private void updateAudioFormat()
	{
		audioFormat = new AudioFormat(samplingRate, sampleSizeInBits, numChannels, signed, bigEndian);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) 
	{
		if (message instanceof ShortMessage)
		{
			inputModule.handleMessage((ShortMessage) message);
		}

	}
	
	public void close()
	{
		outputModule.stopPlaying();
	}
	
	public void run()
	{
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() 
			{
				try 
				{
					outputModule.startPlaying();
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	
	public Map<Integer, ModuleContainer> getModuleContainers()
	{
		return containers;
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


}

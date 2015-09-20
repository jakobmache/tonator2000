package engine;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;

import containers.StandardModuleContainer;
import modules.InputController;
import modules.Mixer;
import modules.Oscillator;
import modules.OutputModule;

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
	
	private boolean isRunning = false;
	
	private int oscillatorType = Oscillator.TYPE_SINE;
	
	private OutputModule outputModule;

	private InputController inputModule;
	private Map<Integer, Oscillator> oscillators;
	private ModuleContainer container;
	
	private MidiDevice connectedMidiDevice;

	public SynthesizerEngine() throws LineUnavailableException
	{
		updateAudioFormat();
		initContainers();
		initModules();
	}
	
	public void connectMidiDevice(MidiDevice device) throws MidiUnavailableException
	{
		if (connectedMidiDevice != null)
			connectedMidiDevice.close();
		
		Transmitter transmitter = device.getTransmitter();
		transmitter.setReceiver(this);
		
		connectedMidiDevice = device;
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
			oscillators.get(key).setType(oscillatorType);
			Wire wire = new Wire(outputMixer, oscillators.get(key), 0, i);
			i++;
		}
		
		container = new StandardModuleContainer(this, outputMixer);
		new Wire(outputModule, container, 0, 0);
		
	}
	
	private void initContainers()
	{
		oscillators = new HashMap<Integer, Oscillator>();
		for (int key = startKeyNumber; key <= endKeyNumber; key++)
		{
			Oscillator oscillator = new Oscillator(this);
			oscillators.put(key, oscillator);
		}
		
	}

	private void updateAudioFormat() throws LineUnavailableException
	{
		stop();
		audioFormat = new AudioFormat(samplingRate, sampleSizeInBits, numChannels, signed, bigEndian);
		if (outputModule != null)
			outputModule.updateFormat();
	}

	@Override
	public void send(MidiMessage message, long timeStamp) 
	{
		if (message instanceof ShortMessage)
		{
			inputModule.handleMessage((ShortMessage) message);
		}

	}
	
	public void stop()
	{
		if (outputModule != null)
			outputModule.stopPlaying();
		isRunning = false;
	}
	
	public void close()
	{
		stop();
		disconnectMidiDevice();
	}
	
	public void disconnectMidiDevice()
	{
		if (connectedMidiDevice != null)
			connectedMidiDevice.close();
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
		isRunning = true;
	}
	
	public void setOscillatorType(int type)
	{
		for (Oscillator osci:oscillators.values())
		{
			osci.setType(type);
		}
		
		oscillatorType = type;
	}
	
	public int getOscillatorType()
	{
		return oscillatorType;
	}
	
	public ModuleContainer getContainer() 
	{
		return container;
	}

	public Map<Integer, Oscillator> getOscillators()
	{
		return oscillators;
	}
	
	public AudioFormat getAudioFormat() {
		return audioFormat;
	}

	public float getSamplingRate() {
		return samplingRate;
	}

	public void setSamplingRate(float samplingRate) throws LineUnavailableException {
		this.samplingRate = samplingRate;
		updateAudioFormat();
	}

	public int getNumChannels() {
		return numChannels;
	}

	public void setNumChannels(int numChannels) throws LineUnavailableException {
		this.numChannels = numChannels;
		updateAudioFormat();
	}

	public boolean isSigned() {
		return signed;
	}

	public void setSigned(boolean signed) throws LineUnavailableException {
		this.signed = signed;
		updateAudioFormat();
	}

	public boolean isBigEndian() {
		return bigEndian;
	}

	public void setBigEndian(boolean bigEndian) throws LineUnavailableException {
		this.bigEndian = bigEndian;
		updateAudioFormat();
	}

	public int getSampleSizeInBits() {
		return sampleSizeInBits;
	}

	public void setSampleSizeInBits(int sampleSizeInBits) throws LineUnavailableException {
		this.sampleSizeInBits = sampleSizeInBits;
		updateAudioFormat();
	}

	public int getSampleSizeInBytes() {
		return sampleSizeInBytes;
	}

	public void setSampleSizeInBytes(int sampleSizeInBytes) throws LineUnavailableException {
		this.sampleSizeInBytes = sampleSizeInBytes;
		updateAudioFormat();
	}

	public double getBufferTime() {
		return bufferTime;
	}

	public void setBufferTime(double bufferTime) throws LineUnavailableException {
		this.bufferTime = bufferTime;
		updateAudioFormat();
	}
	
	public InputController getInputController()
	{
		return inputModule;
	}
	
	public MidiDevice getConnectedMidiDevice()
	{
		return connectedMidiDevice;
	}
	
	public boolean isRunning()
	{
		return isRunning;
	}


}

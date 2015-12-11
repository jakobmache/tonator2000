package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;

import resources.Strings;
import midi.MidiPlayer;
import modules.Ids;
import modules.Mixer;
import modules.OutputModule;
import modules.listener.EngineListener;
import containers.StandardModuleContainer;

public class SynthesizerEngine implements Receiver
{

	private AudioFormat audioFormat;
	private float samplingRate = 22050;
	private int numChannels = 1;
	private boolean signed = true;
	private boolean bigEndian = true;

	public static final int MAX_POLYPHONY = 1000;

	private int sampleSizeInBits = 16;
	private int sampleSizeInBytes = 2;

	private double bufferTime = 0.02;

	private boolean isRunning = false;

	private int maxPolyphony = 45;

	private MidiPlayer midiPlayer;

	private OutputModule outputModule;

	private InputController inputModule;
	private Mixer outputMixer;
	private ModuleContainer allContainer;
	private ModuleContainer osciContainer;
	private ProgramManager programManager;

	private MidiDevice connectedMidiDevice;

	private List<EngineListener> listeners = new ArrayList<EngineListener>();

	public SynthesizerEngine() throws LineUnavailableException, IOException
	{
		updateAudioFormat();
		initModules();
		midiPlayer = new MidiPlayer(this);
	}

	public void connectMidiDevice(MidiDevice device) throws MidiUnavailableException
	{
		if (connectedMidiDevice != null)
			connectedMidiDevice.close();

		Transmitter transmitter = device.getTransmitter();
		transmitter.setReceiver(this);

		if (transmitter.getReceiver() == this)
			connectedMidiDevice = device;

		notifyListeners();
	}

	private void initModules() throws IOException
	{
		programManager = new ProgramManager();
		outputMixer = new Mixer(this, Ids.ID_MIXER_1, Strings.getStandardModuleName(Ids.ID_MIXER_1));
		inputModule = new InputController(this);


		try 
		{
			outputModule = new OutputModule(this, Ids.ID_OUTPUT_1, Strings.getStandardModuleName(Ids.ID_OUTPUT_1));
		} 
		catch (LineUnavailableException e) 
		{
			e.printStackTrace();
		}

		allContainer = new StandardModuleContainer(this, 1, 1, Ids.ID_CONTAINER, Strings.getStandardModuleName(Ids.ID_CONTAINER));
		new Wire(allContainer, outputMixer, Mixer.SAMPLE_OUTPUT, ModuleContainer.SAMPLE_INPUT);
		new Wire(outputModule, allContainer, ModuleContainer.SAMPLE_OUTPUT, OutputModule.SAMPLE_INPUT);
	}

	private void updateAudioFormat() throws LineUnavailableException
	{
		stop();
		audioFormat = new AudioFormat(samplingRate, sampleSizeInBits, numChannels, signed, bigEndian);
		if (outputModule != null)
			outputModule.updateFormat();

		notifyListeners();
	}

	private void notifyListeners()
	{
		for (EngineListener listener:listeners)
		{
			listener.onValueChanged();
		}
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
		reset();
		notifyListeners();
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
		notifyListeners();
	}

	public void reset()
	{
		try 
		{
			if (inputModule != null)
				inputModule.resetMidi();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

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
		notifyListeners();
	}

	public ModuleContainer getAllContainer() 
	{
		return allContainer;
	}

	public ModuleContainer getOsciContainer()
	{
		return osciContainer;
	}

	public Mixer getOutputMixer()
	{
		return outputMixer;
	}

	public OutputModule getOutputModule()
	{
		return outputModule;
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

	public void addListener(EngineListener listener)
	{
		listeners.add(listener);
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

	public int getMaxPolyphony()
	{
		return maxPolyphony;
	}

	public MidiPlayer getMidiPlayer()
	{
		return midiPlayer;
	}

	public ProgramManager getProgramManager() {
		return programManager;
	}

	public void setMaxPolyphony(int newValue)
	{
		if (newValue <= MAX_POLYPHONY && newValue > 0)
		{
			maxPolyphony = newValue;
			notifyListeners();
		}
	}
}

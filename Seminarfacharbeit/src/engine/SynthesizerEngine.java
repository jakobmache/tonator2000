package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;

import resources.Strings;
import listener.EngineListener;
import midi.MidiLogger;
import midi.MidiPlayer;
import modules.Ids;
import modules.Mixer;
import modules.OutputModule;
import containers.OscillatorContainer;
import containers.StandardModuleContainer;

public class SynthesizerEngine implements Receiver
{
	private AudioFormat audioFormat;
	private float samplingRate = 22050;
	private int numChannels = 1;
	private boolean signed = true;
	private boolean bigEndian = true;

	//Maximalwerte für maximale Polyphonie bzw. Pufferzeit
	public static final int MAX_POLYPHONY = 1000;
	public static final double MAX_BUFFERTIME = 0.5;

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
	private PlayableModuleContainer synthesizerContainer;
	private ProgramManager programManager;
	private MidiLogger logger;
	
	private long startTimestamp;

	private MidiDevice connectedMidiDevice;

	private List<EngineListener> listeners = new ArrayList<EngineListener>();

	/**
	 * Instanziert eine neue SynthesizerEngine. Diese ist die Containerklasse für alle Parameter und Module des Synthesizers.
	 * 
	 * @throws LineUnavailableException wenn die Audioausgabe nicht erzeugt werden kann
	 * @throws IOException wenn der ProgramManager die Namen der Instrumente nicht auslesen kann
	 */
	public SynthesizerEngine() throws LineUnavailableException, IOException
	{
		updateAudioFormat();
		initModules();
		midiPlayer = new MidiPlayer(this);
		logger = new MidiLogger();
		
		setSynthesizerContainer(new OscillatorContainer(this, "DefaultOscillatorContainer"));
	}

	/**
	 * Verbindet ein MIDI-Gerät mit der Engine.
	 * 
	 * @param device das MIDI-Gerät, welches verbunden werden soll
	 * @throws MidiUnavailableException wenn das Verbinden scheitert
	 */
	public void connectMidiDevice(MidiDevice device) throws MidiUnavailableException
	{
		if (connectedMidiDevice != null)
		{
			connectedMidiDevice.close();
			connectedMidiDevice = null;
			notifyListeners();
		}

		Transmitter transmitter = device.getTransmitter();
		transmitter.setReceiver(this);

		device.open();
		
		if (transmitter.getReceiver() == this)
			connectedMidiDevice = device;
		
		notifyListeners();
	}

	/**
	 * Initialisiert alle Module.
	 * 
	 * @throws LineUnavailableException wenn die Audioausgabe nicht erzeugt werden kann
	 * @throws IOException wenn der ProgramManager die Namen der Instrumente nicht auslesen kann
	 */
	private void initModules() throws LineUnavailableException, IOException
	{
		programManager = new ProgramManager();
		outputMixer = new Mixer(this, Ids.ID_MIXER_1, Strings.getStandardModuleName(Ids.ID_MIXER_1));
		inputModule = new InputController(this);

		outputModule = new OutputModule(this, Ids.ID_OUTPUT_1, Strings.getStandardModuleName(Ids.ID_OUTPUT_1));
	
		allContainer = new StandardModuleContainer(this, 1, 1, Ids.ID_CONTAINER, Strings.getStandardModuleName(Ids.ID_CONTAINER));
		new Wire(allContainer, outputMixer, Mixer.SAMPLE_OUTPUT, ModuleContainer.SAMPLE_INPUT);
		new Wire(outputModule, allContainer, ModuleContainer.SAMPLE_OUTPUT, OutputModule.SAMPLE_INPUT);
	}

	/**
	 * Aktualisiert das AudioFormat und damit die Parameter der Audioausgabe, wenn man zum Beispiel die Samplingrate ändert.
	 * 
	 * @throws LineUnavailableException wenn die Audioausgabe nicht mit den gewünschten Parametern erzeugt werden kann
	 */
	private void updateAudioFormat() throws LineUnavailableException
	{
		stopAudio();
		
		if (bufferTime > MAX_BUFFERTIME)
			throw new LineUnavailableException();
		
		audioFormat = new AudioFormat(samplingRate, sampleSizeInBits, numChannels, signed, bigEndian);
		
		if (outputModule != null)
			outputModule.updateFormat();

		notifyListeners();
	}

	/**
	 * Benachrichtigt alle verbundenen EngineListener, dass sich ein Parameter der Engine verändert hat.
	 */
	private void notifyListeners()
	{
		for (EngineListener listener:listeners)
		{
			listener.onValueChanged();
		}
	}

	/**
	 * Wird aufgerufen, wenn ein MIDI-Event eingeht.
	 * 
	 * @param message das eingehende MIDI-Event
	 * @param timeStamp Zeitstempel des Events
	 */
	@Override
	public void send(MidiMessage message, long timeStamp) 
	{
		//Und gleich weiter zum InputManager und Logger
		if (message instanceof ShortMessage)
		{
			inputModule.handleMessage((ShortMessage) message);
		}
		
		logger.receiveEvent(message, timeStamp);
	}

	/**
	 * Stoppt die Audiowiedergabe der Engine. MIDI-Events werden weiter empfangen!
	 */
	public void stopAudio()
	{
		if (outputModule != null)
			outputModule.stopPlaying();
		isRunning = false;
		reset();
		notifyListeners();
	}

	/**
	 * Schließt die Engine, stoppt also die Audiowiedergabe und trennt das MIDI-Gerät.
	 */
	public void close()
	{
		stopAudio();
		disconnectMidiDevice();
	}

	/**
	 * Trennt das aktuell verbundene MIDI-Gerät, wenn eines verbunden ist.
	 */
	public void disconnectMidiDevice()
	{
		if (connectedMidiDevice != null)
			connectedMidiDevice.close();
		notifyListeners();
	}

	/**
	 * Setzt das InputModule zurürck. Dadurch wird das Abspielen aller Noten unterbrochen.
	 */
	public void reset()
	{
		try 
		{
			if (inputModule != null)
				inputModule.resetMidi();
		} 
		catch (InvalidMidiDataException e) 
		{
			//Wir erzeugen nur korrekte MIDI-Off-Events!!!
			e.printStackTrace();
		}

	}

	/**
	 * Startet die Audiowiedergabe der Engine in einem neuen Thread.
	 */
	public void run()
	{
		updateStartTimestamp();
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

	/**
	 *  Gibt den Container zurück, der die Standardmodule enthält, die in jeder Engine sind. 
	 *  
	 * @return den in jeder Engine einmal enthaltenen Container
	 */
	public ModuleContainer getAllContainer() 
	{
		return allContainer;
	}

	/**
	 * Gibt den Container zurück, mit dem die Töne erzeugt werden.
	 * 
	 * @return der Container, mit dem die Töne erzeugt werden
	 */
	public PlayableModuleContainer getSynthesizerContainer()
	{
		return synthesizerContainer;
	}
	
	public void setSynthesizerContainer(PlayableModuleContainer container)
	{
		this.synthesizerContainer = container;
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

	public void setSamplingRate(float samplingRate) throws LineUnavailableException 
	{
		//Bei jedem Parameter: Erst versuchen, Parameter zu aktualisieren, wenn das nicht klappt, Werte zurücksetzen.
		float oldSamplingrate = this.samplingRate;
		try
		{
			this.samplingRate = samplingRate;
			updateAudioFormat();
		}
		catch(LineUnavailableException e)
		{
			samplingRate = oldSamplingrate;
			updateAudioFormat();
			throw new LineUnavailableException();
		}
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
		double oldBufferTime = this.bufferTime;
		try
		{
			this.bufferTime = bufferTime;
			updateAudioFormat();
		}
		catch(LineUnavailableException e)
		{
			bufferTime = oldBufferTime;
			updateAudioFormat();
			throw new LineUnavailableException();
		}
	}

	/**
	 * Fügt einen EngineListener hinzu, der immer benachrichtigt wird, wenn sich ein Parameter der Audioausgabe verändert.
	 * 
	 * @param listener der EngineListener, der hinzugefügt werden soll
	 */
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

	/**
	 * Gibt an, ob die Engine gerade Audio wiedergibt.
	 * 
	 * @return ob die Engine zur Zeit Audio wiedergibt
	 */
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

	/**
	 * Aktualisiert die maximale Polyphonie. Diese gibt an, wie viele Töne gleichzeitig abgespielt werden können. 
	 * Wenn mehr Container gleichzeitig spielen, wird jeder neue einfach ignoriert. 
	 * Scheitert, wenn der Wert zu hoch ist.
	 * 
	 * @param newValue die maximale Anzahl an Tönen, die gleichzeitig wiedergegeben werden könen sollen
	 */
	public void setMaxPolyphony(int newValue)
	{
		if (newValue <= MAX_POLYPHONY && newValue > 0)
		{
			maxPolyphony = newValue;
			notifyListeners();
		}
	}
	
	public MidiLogger getMidiLogger()
	{
		return logger;
	}
	
	public long getTimestamp()
	{
		return System.nanoTime() - startTimestamp;
	}
	
	public void updateStartTimestamp()
	{
		startTimestamp = System.nanoTime();
	}
	
}

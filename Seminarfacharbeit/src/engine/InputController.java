package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import containers.OscillatorContainer;
import containers.SynthesizerModuleContainer;
import listener.ModuleContainerListener;
import listener.ProgramListener;
import midi.MidiUtils;
import modules.Ids;
import modules.Mixer;

public class InputController implements ModuleContainerListener, ProgramListener{

	//Liste mit allen gerade aktiven, spielenden Containern
	private List<ModuleContainer> allContainers;
	//Hier ist jedem MIDI-Kanal eine Map zugeordnet, in welcher der entsprechenden Notennummer die Container zugeordnet sind, die sie gerade abspielen
	private Map<Integer, Map<Integer, ModuleContainer>> channelNotes;
	private SynthesizerEngine parent;
	
	//Speichert die Programme und Lautstärken für jeden MIDI-Kanal
	private List<Integer> channelPrograms = new ArrayList<Integer>();
	private List<Float> channelVolumes = new ArrayList<Float>();

	private final int NOTE_ON_START = 144;
	private final int NOTE_OFF_START = 128;
	public final static int NUM_CHANNELS = 16;
	
	public static final float INITIAL_CHANNEL_VOLUME = 100F;
	public static final int CHANNEL_VOLUME = 7;
	
	public static final int DEFAULT_PROGRAM = 0;

	/**
	 * Instanziert einen neuen InputController. Dieses verarbeitet eingehende MIDI-Events für eine Engine.
	 * 
	 * @param parent die Engine zum Verarbeiten.
	 */
	public InputController(SynthesizerEngine parent)
	{
		this.parent = parent;
		channelNotes = new HashMap<Integer, Map<Integer, ModuleContainer>>();
		allContainers = new ArrayList<ModuleContainer>();
		
		//Standardwerte initialisieren
		for (int i = 0; i < NUM_CHANNELS; i++)
		{
			channelPrograms.add(0);
			channelNotes.put(i, new HashMap<Integer, ModuleContainer>());
			channelVolumes.add(INITIAL_CHANNEL_VOLUME / 127F);
		}
		
		parent.getProgramManager().addListener(this);
	}

	/**
	 * Verarbeitet MIDI-Messages.
	 * 
	 * @param message die eingehende MIDI-Nachricht.
	 */
	public void handleMessage(ShortMessage message)
	{
		
		//Note-On Event
		if ((NOTE_ON_START <= message.getCommand()) && (message.getCommand() < NOTE_ON_START + NUM_CHANNELS))
		{
			//Velocity = 0 --> Anschlagsstärke = 0 --> es ist ein Note-off Event
			if (message.getData2() == 0)
				playNoteOff(message);
			else 
				playNoteOn(message);
		}

		//Note-Off Event
		else if ((NOTE_OFF_START <= message.getCommand()) && (message.getCommand() < NOTE_OFF_START + NUM_CHANNELS))
		{
			playNoteOff(message);
		}
		
		//Programm für den Kanal wechselt
		else if (message.getCommand() == ShortMessage.PROGRAM_CHANGE)
		{
			setProgram(message);
		}
		
		//Lautstärke für den Kanal wechselt
		else if (message.getCommand() == ShortMessage.CONTROL_CHANGE && message.getData1() == CHANNEL_VOLUME)
		{
			setChannelVolume(message);
		}
	}

	/**
	 * Beginnt das Abspielen eines Tons.
	 * 
	 * @param message eingehendes MIDI-Event
	 */
	private void playNoteOn(ShortMessage message)
	{
		//Daten aus der MIDI-Message auslesen
		int key = message.getData1();
		int velocity = message.getData2();
		int channel = message.getChannel();
		float frequency = MidiUtils.midiNoteNumberToFrequency(key);

		
		//Wird die Note auf diesem Kanal schon abgespielt?
		if (channelNotes.get(channel).containsKey(key))
		{
			//System.out.println("Note wird schon abgespielt - Fehler!");
			return;
		}
		
		//Sie wird nicht abgespielt --> Wir erzeugen einen neuen Container, der sie spielt
//		ProgramManager manager = parent.getProgramManager();
		
		PlayableModuleContainer container;
		if (parent.getSynthesizerContainer().getClass() == OscillatorContainer.class)
		{
			System.out.println("Default!");
			container = new OscillatorContainer(parent, "OscillatorContainer");
		}
		else {
			container = new SynthesizerModuleContainer(parent, 1, 1, Ids.getNextId(), "SynthesizerContainer", parent.getSynthesizerContainer());
		}

//		container.applyContainerPreset(manager.getInstrumentPreset(channelPrograms.get(channel)));
//		container.addListener(this);
		
		new Wire(parent.getOutputMixer(), container, ModuleContainer.SAMPLE_OUTPUT, Mixer.NEXT_FREE_INPUT);
		
		//Wir müssen uns den Container merken
		allContainers.add(container);
		Map<Integer, ModuleContainer> noteMap = channelNotes.get(channel);
		noteMap.put(key, container);
		container.startPlaying(frequency, channelVolumes.get(channel) * (velocity / 127.0F) * Short.MAX_VALUE);
	}

	/**
	 * Stoppt das Abspielen einer Note.
	 * 
	 * @param message das eingehende MIDI-Event
	 */
	private void playNoteOff(ShortMessage message)
	{
		int key = message.getData1();
		int channel = message.getChannel();
		//System.out.println("Note off! - " + key);
		
		try {
			
			//Sie wird abgespielt --> Wir stoppen das Abspielen (bzw bei ASDR Release)
			Map<Integer, ModuleContainer> noteMap = channelNotes.get(channel);
			OscillatorContainer container = (OscillatorContainer) noteMap.get(key);
			container.stopPlaying();
			
			//Die entsprechende Note wird nicht mehr abgespielt
			noteMap.remove(key);
		} 
		catch (Exception e) 
		{
			//Wenn der Ton nicht abgespielt wird, können wir es ignorieren
			//System.out.println("Note wird nicht abgespielt - Fehler!");
			return;

		}

	}
	
	/**
	 * Verändert das Programm auf dem entsprechenden Kanal.
	 * 
	 * @param message das eingehende MIDI-Event
	 */
	private void setProgram(ShortMessage message)
	{
		int channel = message.getChannel();
		int program = message.getData1();
		
		channelPrograms.set(channel, program);
	}
	
	/**
	 * Verändert die Lautstärke auf dem entsprechenden Kanal.
	 * 
	 * @param message das eingehende MIDI-Event
	 */
	private void setChannelVolume(ShortMessage message)
	{
		int channel = message.getChannel();
		int volume = message.getData2();
		
		channelVolumes.set(channel, volume / 127F);
	}

	/**
	 * Setzt alle MIDI-Daten zurück. Das bedeutet, sämtliche Container hören auf zu spielen.
	 * 
	 * @throws InvalidMidiDataException wenn ein falsches Note-Off-Event generiert wurde - sollte nicht passieren!
	 */
	public void resetMidi() throws InvalidMidiDataException
	{
		for (ModuleContainer container:allContainers)
		{
			((PlayableModuleContainer) container).stopPlaying();
		}
	}

	/**
	 * Gibt alle Container zurück, die zur Zeit spielen.
	 * 
	 * @return alle Container, die aktuell einen Ton spielen
	 */
	public List<ModuleContainer> getAllContainers()
	{
		return allContainers;
	}

	/**
	 * Ein InputController ist auch ein ModuleContainerListener. Wenn ein Container fertig mit Abspielen ist, muss er gelöscht werden.
	 */
	@Override
	public void onContainerFinished(ModuleContainer container) 
	{
		parent.getOutputMixer().disconnectInputWire(container.getOutputWire());
		allContainers.remove(container);
	}
	
	public int getNumMidiChannels()
	{
		return NUM_CHANNELS;
	}

	/**
	 * Ein InputController ist auch ein ProgramListener. Wenn sich bei einem Programm / Instrument ein Parameter ändert,
	 * muss bei allen Containern, die mit diesem Instrument spielen, der entsprechende Parameter auch geändert werden.
	 * 
	 * @param program das Programm, welches sich verändert hat
	 * @param id ID des veränderten Parameters
	 * @param newValue neuer Wert des Parameters
	 */
	@Override
	public void programValueChanged(int program, int id, float newValue) 
	{
		for (int channel:channelNotes.keySet())
		{
			if (channelPrograms.get(channel) == program)
			{
				for (ModuleContainer container:channelNotes.get(channel).values())
				{
					container.updatePreset(id, newValue);
				}		
			}
		}
	}
	
	/**
	 * Weist einem MIDI-Kanal ein neues Programm zu.
	 * 
	 * @param channel der MIDI-Kanal
	 * @param newProgram das neue Programm
	 */
	public void setChannelProgram(int channel, int newProgram)
	{
		channelPrograms.set(channel, newProgram);
	}
	
	public int getChannelProgram(int channel)
	{
		return channelPrograms.get(channel);
	}
	
	public PlayableModuleContainer getSynthesizerContainer()
	{
		return parent.getSynthesizerContainer();
	}

}

package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import resources.Strings;
import midi.MidiUtils;
import modules.Ids;
import modules.Mixer;
import modules.listener.ProgramListener;
import containers.ModuleContainerListener;
import containers.OscillatorContainer;

public class InputController implements ModuleContainerListener, ProgramListener{

	private List<ModuleContainer> allContainers;
	private Map<Integer, Map<Integer, ModuleContainer>> channelNotes;
	private SynthesizerEngine parent;
	
	private List<Integer> channelPrograms = new ArrayList<Integer>();

	private final int NOTE_ON_START = 144;
	private final int NOTE_OFF_START = 128;
	private final int NUM_CHANNELS = 16;

	public InputController(SynthesizerEngine parent)
	{
		this.parent = parent;
		channelNotes = new HashMap<Integer, Map<Integer, ModuleContainer>>();
		allContainers = new ArrayList<ModuleContainer>();
		
		for (int i = 0; i < NUM_CHANNELS; i++)
		{
			channelPrograms.add(0);
			channelNotes.put(i, new HashMap<Integer, ModuleContainer>());
		}
		
		parent.getProgramManager().addListener(this);
	}
	
	public ModuleContainer getReferenceContainer()
	{
		return new OscillatorContainer(parent, Strings.getStandardModuleName(Ids.ID_CONTAINER));
	}

	public void handleMessage(ShortMessage message)
	{
		if (message.getChannel() == 9)
			return;
		
		if ((NOTE_ON_START <= message.getCommand()) && (message.getCommand() < NOTE_ON_START + NUM_CHANNELS))
		{
			if (message.getData2() == 0)
				playNoteOff(message);
			else 
				playNoteOn(message);
		}


		else if ((NOTE_OFF_START <= message.getCommand()) && (message.getCommand() < NOTE_OFF_START + NUM_CHANNELS))
		{
			playNoteOff(message);
		}
		
		else if (message.getCommand() == ShortMessage.PROGRAM_CHANGE)
		{
			setProgram(message);
		}
	}

	private void playNoteOn(ShortMessage message)
	{
		//Daten aus der MIDI-Message auslesen
		int key = message.getData1();
		int velocity = message.getData2();
		int channel = message.getChannel();
		float frequency = MidiUtils.midiNoteNumberToFrequency(key);
		
		System.out.println("Note on! - " + key + " - " + velocity + " - " + frequency + "Hz | Programm " + channelPrograms.get(channel));
		
		//Wird die Note auf diesem Kanal schon abgespielt?
		if (channelNotes.get(channel).containsKey(key))
		{
			//System.out.println("Note wird schon abgespielt - Fehler!");
			return;
		}
		
		//Sie wird nicht abgespielt --> Wir erzeugen einen neuen Container, der sie spielt
		ProgramManager manager = parent.getProgramManager();
	
		OscillatorContainer container;
		
		container = new OscillatorContainer(parent, Strings.getStandardModuleName(Ids.ID_CONTAINER));
		container.applyContainerPreset(manager.getInstrumentPreset(channelPrograms.get(channel)));
		container.addListener(this);
		
		new Wire(parent.getOutputMixer(), container, ModuleContainer.SAMPLE_OUTPUT, Mixer.NEXT_FREE_INPUT);

		//Wir müssen uns den Container merken
		allContainers.add(container);
		Map<Integer, ModuleContainer> noteMap = channelNotes.get(channel);
		noteMap.put(key, container);

		container.startPlaying(frequency,(velocity / 127.0F) * Short.MAX_VALUE);
	}

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
	
	private void setProgram(ShortMessage message)
	{
		int channel = message.getChannel();
		int program = message.getData1();
		
		System.out.printf("New program on channel %d:%d\n", channel, program);
		
		channelPrograms.set(channel, program);
	}

	public void resetMidi() throws InvalidMidiDataException
	{
		for (ModuleContainer container:allContainers)
		{
			((OscillatorContainer) container).stopPlaying();
		}
	}

	public List<ModuleContainer> getAllContainers()
	{
		return allContainers;
	}

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

}

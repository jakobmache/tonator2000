package modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import midi.MidiUtils;
import presets.OscillatorContainerPreset;
import containers.ContainerPreset;
import containers.ModuleContainerListener;
import containers.OscillatorContainer;
import engine.ModuleContainer;
import engine.SynthesizerEngine;
import engine.Wire;

public class InputController implements ModuleContainerListener{

	private List<ModuleContainer> allContainers;
	private Map<Integer, Map<Integer, ModuleContainer>> channelNotes;
	private SynthesizerEngine parent;
	
	private HashMap<Integer, ContainerPreset> channelPresets = new HashMap<Integer, ContainerPreset>();

	private final int NOTE_ON_START = 144;
	private final int NOTE_OFF_START = 128;
	private final int NUM_CHANNELS = 16;

	public InputController(SynthesizerEngine parent)
	{
		this.parent = parent;
		channelNotes = new HashMap<Integer, Map<Integer, ModuleContainer>>();
		allContainers = new ArrayList<ModuleContainer>();
		initChannelPresets();
	}
	
	private void initChannelPresets()
	{
		for (int i = 0; i < NUM_CHANNELS; i++)
		{
			channelPresets.put(i, new OscillatorContainerPreset());
			channelNotes.put(i, new HashMap<Integer, ModuleContainer>());
		}
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
	}

	private void playNoteOn(ShortMessage message)
	{
		//Daten aus der MIDI-Message auslesen
		int key = message.getData1();
		int velocity = message.getData2();
		int channel = message.getChannel();
		float frequency = MidiUtils.midiNoteNumberToFrequency(key);
		
		System.out.println("Note on! - " + key + " - " + velocity + " - " + frequency + "Hz | Channel " + message.getChannel());

		//Wird die Note auf diesem Kanal schon abgespielt?
		if (channelNotes.get(channel).containsKey(key))
		{
			System.out.println("Note wird schon abgespielt - Fehler!");
			return;
		}

		System.out.println("Container playing!");
		//Sie wird nicht abgespielt --> Wir erzeugen einen neuen Container, der sie spielt
		OscillatorContainer container;
		container = new OscillatorContainer(parent);
		container.applyContainerPreset(channelPresets.get(channel));
		container.addListener(this);
	
		new Wire(parent.getOutputMixer(), container, ModuleContainer.SAMPLE_OUTPUT, Mixer.NEXT_FREE_INPUT);

		//Wir müssen uns den Container merken
		allContainers.add(container);
		Map<Integer, ModuleContainer> noteMap = channelNotes.get(channel);
		noteMap.put(key, container);

		updatePresetValue(channel, Ids.ID_CONSTANT_AMPLITUDE_1, (velocity / 127.0F) * Short.MAX_VALUE);
		Envelope envelope = (Envelope) channelNotes.get(channel).get(key).findModuleById(Ids.ID_ENVELOPE_1);
		envelope.setMaxValue((velocity / 127.0F) * Short.MAX_VALUE);
		container.startPlaying(frequency,(velocity / 127.0F) * Short.MAX_VALUE);
	}

	private void playNoteOff(ShortMessage message)
	{
		int key = message.getData1();
		int channel = message.getChannel();
		System.out.println("Note off! - " + key);

		//Wenn der Ton nicht abgespielt wird, können wir es ignorieren
		if (!channelNotes.get(channel).containsKey(key))
		{
			System.out.println("Note wird nicht abgespielt - Fehler!");
			return;
		}
		
		//Sie wird abgespielt --> Wir stoppen das Abspielen (bzw bei ASDR Release)
		Map<Integer, ModuleContainer> noteMap = channelNotes.get(channel);
		OscillatorContainer container = (OscillatorContainer) noteMap.get(key);
		container.stopPlaying();
		
		//Die entsprechende Note wird nicht mehr abgespielt
		noteMap.remove(key);
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

	public ContainerPreset getPreset(int channel) {
		return channelPresets.get(channel);
	}

	public void setPreset(ContainerPreset preset, int channel) 
	{
		channelPresets.put(channel, preset);
		for (ModuleContainer container:channelNotes.get(channel).values())
			container.applyContainerPreset(preset);
	}

	public void updatePresetValue(int channel, int id, float value)
	{
		for (ModuleContainer container:channelNotes.get(channel).values())
		{
			container.updatePreset(id, value);
		}
		channelPresets.get(channel).setParam(id, value);
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

}

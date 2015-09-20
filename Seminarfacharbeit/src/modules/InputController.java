package modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.ShortMessage;
import javax.sound.sampled.LineUnavailableException;

import midi.MidiUtils;
import containers.StandardModuleContainer;
import engine.Module;
import engine.ModuleContainer;
import engine.SynthesizerEngine;

public class InputController{

	private List<Integer> currentNotes;
	private SynthesizerEngine parent;

	public InputController(SynthesizerEngine parent)
	{
		this.parent = parent;
		currentNotes = new ArrayList<Integer>();
	}

	//TODO: Fix playing
	public void handleMessage(ShortMessage message)
	{

		if (message.getCommand() == ShortMessage.NOTE_ON)
		{
			int key = message.getData1();
			int velocity = message.getData2();

			System.out.println("Note on! - " + key + " - " + velocity);
			float frequency = MidiUtils.midiNoteNumberToFrequency(key);

			if (currentNotes.contains(key))
			{
				System.out.println("Note wird schon abgespielt - Fehler!");
				return;
			}

			Oscillator oscillator = parent.getOscillators().get(key);

			oscillator.setFrequency((double) frequency);
			//container.getOscillator().setAmplitude((velocity / 127.0) * Short.MAX_VALUE);
			oscillator.setAmplitude(Short.MAX_VALUE);
			currentNotes.add(key);
		}

		else if (message.getCommand() == ShortMessage.NOTE_OFF)
		{
			int key = message.getData1();
			System.out.println("Note off! - " + key);

			if (!currentNotes.contains(key))
			{
				System.out.println("Note wird nicht abgespielt - Fehler!");
				return;
			}

			Oscillator oscillator = parent.getOscillators().get(key);
			oscillator.setFrequency(0);
			oscillator.setAmplitude(0);
			currentNotes.remove((Integer) key);
		}
	}
	
	public List<Integer> getCurrentNotes()
	{
		return currentNotes;
	}

}

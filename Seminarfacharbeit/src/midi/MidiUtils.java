package midi;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

public class MidiUtils {

	public static int NOTE_ON_START = 144;
	public static int NOTE_ON_END = 259;

	public static int NOTE_OFF_START = 128;
	public static int NOTE_OFF_END = 143;
	
	public static List<String > channelNames = new ArrayList<String>()
	{
		private static final long serialVersionUID = 1L;
		
		{
			add("Klavier");
			add("Perkussion 1");
			add("Orgel");
			add("Gitarre");
			add("Bass");
			add("Streicher");
			add("Ensemble");
			add("Blechbläser");
			add("Holzbläser");
			add("Flöten");
			add("Synth Lead");
			add("Synth Pad");
			add("Synth Effekte");
			add("Ethnic");
			add("Perkussion 2");
			add("Soundeffekte");
		}
		
	};

	public static List<Integer> NOTE_ON_COMMANDS = new ArrayList<Integer>()
	{
		private static final long serialVersionUID = 1L;

	{
		for (int i = NOTE_ON_START; i <= NOTE_ON_END; i++)
		{
			add(i);
		}
	}};

	public static List<Integer> NOTE_OFF_COMMANDS = new ArrayList<Integer>()
	{
		private static final long serialVersionUID = 1L;

	{
		for (int i = NOTE_OFF_START; i <= NOTE_OFF_END; i++)
		{
			add(i);
		}
	}};

	private static final int REFERENCE_NOTE_NUMBER = 69;
	private static final int REFERENCE_NOTE_FREQ = 440;
	private static final int NOTES_PER_OCTAVE = 12;

	public static List<Info> getAvailableInputDevices()
	{
		Info[] infos = MidiSystem.getMidiDeviceInfo();

		List<Info> inputDeviceInfo = new ArrayList<Info>();

		for (Info info:infos)
		{
			try
			{
				MidiDevice device = MidiSystem.getMidiDevice(info);

				// Wenn das Gerï¿½t einen Transmitter hat, muss es hinzugefï¿½gt werden
				device.getTransmitter();
				inputDeviceInfo.add(info);
			}

			catch (MidiUnavailableException e)
			{
				// Das Gerï¿½t hat keinen Transmitter, muss also nicht hinzugefï¿½gt werden
			}
		}

		return inputDeviceInfo;
	}

	public static float midiNoteNumberToFrequency(int mnn) {

		float soundOffset = (mnn - REFERENCE_NOTE_NUMBER) / (float) NOTES_PER_OCTAVE;
		return (float) (REFERENCE_NOTE_FREQ * Math.pow(2.0, soundOffset));
	}

}

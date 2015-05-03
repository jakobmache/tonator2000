
package midi;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;


public class MidiUtils {
	
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
				
				// Wenn das Gerät einen Transmitter hat, muss es hinzugefügt werden
				device.getTransmitter();
				inputDeviceInfo.add(info);
			}

			catch (MidiUnavailableException e)
			{
				// Das Gerät hat keinen Transmitter, muss also nicht hinzugefügt werden
			}
		}

		return inputDeviceInfo;
	}

	public static void connectTransmitterToReceiver(MidiDevice device, Receiver receiver) throws MidiUnavailableException
	{
		Transmitter transmitter = device.getTransmitter();
		transmitter.setReceiver(receiver);
	}

	public static float midiNoteNumberToFrequency(int mnn) {

		float soundOffset = (mnn - REFERENCE_NOTE_NUMBER) / (float) NOTES_PER_OCTAVE;
		return (float) (REFERENCE_NOTE_FREQ * Math.pow(2.0, soundOffset));
	}

}

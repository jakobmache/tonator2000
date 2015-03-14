package midi;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

public class MidiManager {

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

}

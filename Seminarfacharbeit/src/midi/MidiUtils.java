
package midi;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;


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
				
				// Wenn das Ger�t einen Transmitter hat, muss es hinzugef�gt werden
				device.getTransmitter();
				inputDeviceInfo.add(info);
			}

			catch (MidiUnavailableException e)
			{
				// Das Ger�t hat keinen Transmitter, muss also nicht hinzugef�gt werden
			}
		}

		return inputDeviceInfo;
	}

	public static void connectTransmitterToReceiver(MidiDevice device, Receiver receiver) throws MidiUnavailableException
	{
		Transmitter transmitter = device.getTransmitter();
		transmitter.setReceiver(receiver);
	}

}

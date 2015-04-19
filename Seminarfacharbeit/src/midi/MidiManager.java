/*
 * 
 */
package midi;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

// TODO: Auto-generated Javadoc
/**
 * The Class MidiManager.
 */
public class MidiManager {

	/**
	 * Gets the available input devices.
	 *
	 * @return the available input devices
	 */
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

	/**
	 * Connect transmitter to receiver.
	 *
	 * @param device the device
	 * @param receiver the receiver
	 * @throws MidiUnavailableException the midi unavailable exception
	 */
	public static void connectTransmitterToReceiver(MidiDevice device, Receiver receiver) throws MidiUnavailableException
	{
		Transmitter transmitter = device.getTransmitter();
		transmitter.setReceiver(receiver);
	}

}

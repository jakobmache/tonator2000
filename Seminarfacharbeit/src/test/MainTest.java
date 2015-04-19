/*
 * 
 */
package test;

import java.util.List;
import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import midi.MidiManager;
import modules.Oscillator;
import engine.ModuleContainer;
import engine.SynthesizerEngine;
// TODO: Auto-generated Javadoc

/**
 * The Class MainTest.
 */
public class MainTest {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws InvalidMidiDataException the invalid midi data exception
	 */
	public static void main(String[] args) throws InvalidMidiDataException {

		List<Info> infoList = MidiManager.getAvailableInputDevices();

		int i = 0;

		for (Info info:infoList)
		{
			System.out.printf("%d: %s - %s\n", i, info.getName(), info.getDescription());
			i++;
		}

		Scanner scanner = new Scanner(System.in);
		System.out.println("Select Midi device: ");
		int choice = scanner.nextInt();

		try 
		{
			Receiver receiver = new SynthesizerEngine();
			MidiDevice device = MidiSystem.getMidiDevice(infoList.get(choice));
			
			MidiManager.connectTransmitterToReceiver(device, receiver);
			
			device.open();

			System.out.println("Press <Enter> to stop listening!");

			scanner.nextLine();
			scanner.nextLine();
			
			device.close();
		} 
		

		catch (MidiUnavailableException e) 
		{
			e.printStackTrace();
		}


	}
	

}

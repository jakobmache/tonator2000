package test;

import java.util.List;
import java.util.Scanner;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;

import midi.MidiUtils;
import engine.SynthesizerEngine;

public class MainTest {

	public static void main(String[] args) throws LineUnavailableException, InterruptedException {
		
		List<Info> infoList = MidiUtils.getAvailableInputDevices();

		int i = 0;

		for (Info info:infoList)
		{
			System.out.printf("%d: %s - %s\n", i, info.getName(), info.getDescription());
			i++;
		}

		Scanner scanner = new Scanner(System.in);
		System.out.println("Midi-Gerät auswählen: ");
		int choice = scanner.nextInt();
		
		SynthesizerEngine engine = new SynthesizerEngine();

		try 
		{
			
			MidiDevice device = MidiSystem.getMidiDevice(infoList.get(choice));
			
			MidiUtils.connectTransmitterToReceiver(device, engine);
			
			device.open();

			System.out.println("<Enter> zum Beenden drücken!");
			engine.run();
			scanner.nextLine();
			scanner.nextLine();
			
			device.close();
		} 
		

		catch (MidiUnavailableException e) 
		{
			e.printStackTrace();
		}
		
		finally
		{
			scanner.close();
			engine.close();
		}
	}

}

package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import containers.ContainerPreset;
import listener.ProgramListener;

public class ProgramManager 
{
	
	private ContainerPreset[] programPresets;
	private String[] instrumentNames;
	
	private List<ProgramListener> listeners = new ArrayList<ProgramListener>();
	
	public static final int NUM_PROGRAMS = 128;
	
	/**
	 * Erzeugt einen neuen ProgramManager. Dieser verwaltet die jedem Programm / Instrument zugeordneten Presets
	 * einer SynthesizerEngine.
	 * 
	 * @throws IOException wenn das Auslesen der Programmnamen scheitert
	 */
	public ProgramManager() throws IOException
	{
		initPresets();
	}
	
	/**
	 * Initialisiert die Standard-Presets und die Instrumente / Programme.
	 * 
	 * @throws IOException wenn das Auslesen der Programmnamen scheitert
	 */
	private void initPresets() throws IOException
	{
		programPresets = new ContainerPreset[NUM_PROGRAMS];
		instrumentNames = new String[NUM_PROGRAMS];
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("resources/generalmidi.txt")));
		
		//Set null for each program
		for (int i = 0; i < programPresets.length; i++)
		{
			programPresets[i] = null;
			instrumentNames[i] = reader.readLine();
		}
		
		reader.close();
	}
	
	public String getInstrumentName(int program)
	{
		return instrumentNames[program];
	}
	
	/**
	 * Ordnet einem Instrument ein neues Preset zu.
	 * 
	 * @param program das Instrument, dem das neue Preset zugeordnet wird
	 * @param preset das neue Preset
	 */
	public void setInstrumentPreset(int program, ContainerPreset preset)
	{
		programPresets[program] = preset;
	}
	
	/**
	 * Aktualisiert einen Parameter in einem Preset, welches einem Instrument zugeordnet sein muss.
	 * 
	 * @param program das Programm, zu welchem das Preset verändert werden soll
	 * @param id ID des zu verändernden Parameters
	 * @param value neuer Wert des Parameters
	 */
	public void updateInstrumentPresetValue(int program, int id, float value)
	{
		programPresets[program].setParam(id, value);
		notifyListeners(program, id, value);
	}
	
	public ContainerPreset getInstrumentPreset(int program)
	{
		return programPresets[program];
	}
	
	/**
	 * Fügt einen ProgramListener hinzu. Dieser wird benachrichtigt, wenn sich 
	 * etwas in einem Preset für ein Programm verändert hat.
	 * 
	 * @param listener der Listener, der hinzugefügt werden soll
	 */
	public void addListener(ProgramListener listener)
	{
		listeners.add(listener);
	}
	
	/**
	 * Benachrichtigt alle ProgramListenert.
	 * 
	 * @param program Programm, das verändert wurde
	 * @param id ID des veränderten Parameters
	 * @param value neuer Wert des Parameters
	 */
	private void notifyListeners(int program, int id, float value)
	{
		for (ProgramListener listener:listeners)
			listener.programValueChanged(program, id, value);
	}
	
	public void reset()
	{
		for (int program = 0; program < NUM_PROGRAMS; program++)
		{
			programPresets[program] = null;
		}
	}
	
	public void setForAll(ContainerPreset preset)
	{
		for (int program = 0; program < NUM_PROGRAMS; program++)
		{
			programPresets[program] = new ContainerPreset(preset);
		}
	}
}

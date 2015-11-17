package engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import modules.listener.ProgramListener;
import presets.OscillatorContainerPreset;
import containers.ContainerPreset;

public class ProgramManager 
{
	
	private ContainerPreset[] programPresets;
	private String[] instrumentNames;
	
	private List<ProgramListener> listeners = new ArrayList<ProgramListener>();
	
	public static final int NUM_PROGRAMS = 128;
	
	public ProgramManager() throws IOException
	{
		initPresets();
	}
	
	private void initPresets() throws IOException
	{
		programPresets = new ContainerPreset[NUM_PROGRAMS];
		instrumentNames = new String[NUM_PROGRAMS];
		
		BufferedReader reader = new BufferedReader(new FileReader(this.getClass().getClassLoader().getResource("resources/generalmidi.txt").getPath()));
		
		for (int i = 0; i < programPresets.length; i++)
		{
			programPresets[i] = new OscillatorContainerPreset();
			instrumentNames[i] = reader.readLine();
		}
		
		reader.close();
	}
	
	public String getInstrumentName(int program)
	{
		return instrumentNames[program];
	}
	
	public void setInstrumentPreset(int program, ContainerPreset preset)
	{
		programPresets[program] = preset;
	}
	
	public void updateInstrumentPresetValue(int program, int id, float value)
	{
		programPresets[program].setParam(id, value);
		notifyListeners(program, id, value);
	}
	
	public ContainerPreset getInstrumentPreset(int program)
	{
		return programPresets[program];
	}
	
	public void addListener(ProgramListener listener)
	{
		listeners.add(listener);
	}
	
	private void notifyListeners(int program, int id, float value)
	{
		for (ProgramListener listener:listeners)
			listener.programValueChanged(program, id, value);
	}
}

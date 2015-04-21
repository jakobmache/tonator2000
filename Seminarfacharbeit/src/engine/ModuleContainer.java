
package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator.OfDouble;

import javax.print.attribute.standard.MediaSize.Other;
import javax.sound.sampled.AudioFormat;

import modules.Oscillator;
import modules.OutputModule;
import events.PlayEvent;
import events.StopEvent;

public class ModuleContainer {
	
	private SynthesizerEngine parent;
	protected Oscillator mainToneModule; 
	protected OutputModule outputModule;
	private List<Module> modules = new ArrayList<Module>();
	
	public ModuleContainer(SynthesizerEngine parent)
	{
		this.parent = parent;
	}
	
	public Oscillator getToneModule() {
		return mainToneModule;
	}

	public void setToneModule(Oscillator toneModule) {
		this.mainToneModule = toneModule;
		modules.add(0, mainToneModule);
	}

	public OutputModule getOutputModule() {
		return outputModule;
	}

	public void setOutputModule(OutputModule outputModule) {
		this.outputModule = outputModule;
		modules.add(outputModule);
	}

	public List<Module> getModules()
	{
		return modules;
	}
	
	public int getBufferSize()
	{
		return parent.getBufferSize();
	}

	public void setModules(List<Module> modules)
	{
		this.modules = modules;
	}

	public void addModule(Module module) throws Exception
	{
		if (mainToneModule == null || outputModule == null)
			throw new Exception("Oscillator and OutputModule not connected!");
		
		if (modules.size() == 2)
		{
			modules.add(1, module);
			return;
		}
		
		modules.add(modules.size() - 2, module);
	}

	private void handleEvent(Event event)
	{
		if (event instanceof PlayEvent)
		{
			//Start playing
		}
		
		else if (event instanceof StopEvent)
		{
			//Stop playing
		}
	}

	public AudioFormat getAudioFormat()
	{
		return parent.getAudioFormat();
	}
	
	public void connectAll()
	{		
		if (modules.size() <= 1)
			return;
		
		System.out.println(modules.size());
		
		for (int i = 0; i < modules.size() - 1; i++)
		{
			System.out.println(i);
			System.out.println("Connect " + modules.get(i));
			System.out.println("Connect " + modules.get(i + 1));
			Module inputModule = modules.get(i + 1);
			Module outputModule = modules.get(i);
			
			new Wire(outputModule, inputModule);
		}
		
	}

}

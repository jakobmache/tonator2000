/*
 * 
 */
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

// TODO: Auto-generated Javadoc
/**
 * The Class ModuleContainer.
 */
public class ModuleContainer {
	
	/** The parent. */
	private SynthesizerEngine parent;
	
	/** The main tone module. */
	protected Oscillator mainToneModule; 
	
	/** The output module. */
	protected OutputModule outputModule;
	
	/** The modules. */
	private List<Module> modules = new ArrayList<Module>();
	
	
	/**
	 * Instantiates a new module container.
	 *
	 * @param parent the parent
	 */
	public ModuleContainer(SynthesizerEngine parent)
	{
		this.parent = parent;
	}
	
	/**
	 * Gets the tone module.
	 *
	 * @return the tone module
	 */
	public Oscillator getToneModule() {
		return mainToneModule;
	}

	/**
	 * Sets the tone module.
	 *
	 * @param toneModule the new tone module
	 */
	public void setToneModule(Oscillator toneModule) {
		this.mainToneModule = toneModule;
		modules.add(0, mainToneModule);
	}

	/**
	 * Gets the output module.
	 *
	 * @return the output module
	 */
	public OutputModule getOutputModule() {
		return outputModule;
	}

	/**
	 * Sets the output module.
	 *
	 * @param outputModule the new output module
	 */
	public void setOutputModule(OutputModule outputModule) {
		this.outputModule = outputModule;
		modules.add(outputModule);
	}
	
	/**
	 * Gets the modules.
	 *
	 * @return the modules
	 */
	public List<Module> getModules()
	{
		return modules;
	}
	
	/**
	 * Sets the modules.
	 *
	 * @param modules the new modules
	 */
	public void setModules(List<Module> modules)
	{
		this.modules = modules;
	}
	
	/**
	 * Adds the module.
	 *
	 * @param module the module
	 * @throws Exception the exception
	 */
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
	
	/**
	 * Handle event.
	 *
	 * @param event the event
	 */
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
	
	/**
	 * Gets the audio format.
	 *
	 * @return the audio format
	 */
	public AudioFormat getAudioFormat()
	{
		return parent.getAudioFormat();
	}
	
/*	public void connectAll()
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
		
	}*/

}

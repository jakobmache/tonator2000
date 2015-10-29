package ui;

import engine.SynthesizerEngine;

public abstract class ModuleController 
{

	protected SynthesizerEngine parent;
	
	public ModuleController(SynthesizerEngine parent)
	{
		this.parent = parent;
	}
	
	protected abstract void update();
	
	
}

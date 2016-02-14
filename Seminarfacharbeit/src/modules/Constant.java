package modules;

import engine.Module;
import engine.SynthesizerEngine;

public class Constant extends Module
{
	public static final int VALUE_OUTPUT = 0;
	
	private float value = 100;
	private boolean toZeroOnStop = false;
	
	/**
	 * Eine Konstante hält einen konstanten Wert.
	 * 
	 * @param parent Engine
	 * @param id ID
	 * @param name Name
	 */
	public Constant(SynthesizerEngine parent, int id, String name) 
	{
		super(parent, 0, 1, id, name);
		type = ModuleType.CONSTANT;
	}
	
	public void setValue(float newValue)
	{	
		value = newValue;
	}
	
	@Override
	public float calcNextDisabledSample(int index) 
	{
		return 0;
	}

	@Override
	public float calcNextSample(int index)
	{
		return value;
	}

	public boolean isToZeroOnStop() {
		return toZeroOnStop;
	}

	public void setToZeroOnStop(boolean toZeroOnStop) {
		this.toZeroOnStop = toZeroOnStop;
	}


	

}

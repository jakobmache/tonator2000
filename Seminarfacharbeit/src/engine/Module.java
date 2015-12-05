package engine;

//TODO:Modulname

public abstract class Module 
{
	protected SynthesizerEngine parent;
	
	protected Wire[] inputWires;
	protected Wire[] outputWires;
	
	protected int moduleId;
	
	protected boolean enabled = true;
	
	protected String name = "Modul";
	
	public Module(SynthesizerEngine parent, int numInputWires, int numOutputWires, int id, String moduleName)
	{
		this.parent = parent;
		this.inputWires = new Wire[numInputWires];
		this.outputWires = new Wire[numOutputWires];
		this.moduleId = id;
		this.name = moduleName;
	}
	
	public float requestNextSample(int index)
	{
		if (enabled)
		{
			float value =  calcNextSample(index);
			if (value > Short.MAX_VALUE || value < Short.MIN_VALUE)
				System.out.println("Value too high at " + moduleId);
			return value;
		}
		else 
			return calcNextDisabledSample(index);
	}
	
	public abstract float calcNextSample(int index);
	public abstract float calcNextDisabledSample(int index);
	
	public void connectInputWire(int index, Wire wire)
	{
		inputWires[index] = wire;
	}
	
	public void connectOutputWire(int index, Wire wire)
	{
		outputWires[index] = wire;
	}
	
	public int getId()
	{
		return moduleId;
	}
	
	public void setEnabled(boolean value)
	{
		System.out.println("Module "+ moduleId + " set to " + value);
		enabled = value;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public String getName()
	{
		return name;
	}
}

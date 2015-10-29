package engine;

public abstract class Module 
{
	protected SynthesizerEngine parent;
	
	protected Wire[] inputWires;
	protected Wire[] outputWires;
	
	protected int moduleId;
	
	public Module(SynthesizerEngine parent, int numInputWires, int numOutputWires, int id)
	{
		this.parent = parent;
		this.inputWires = new Wire[numInputWires];
		this.outputWires = new Wire[numOutputWires];
		this.moduleId = id;
	}
	
	public abstract float requestNextSample(int index);
	
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
}

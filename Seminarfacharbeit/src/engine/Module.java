package engine;

public abstract class Module 
{
	protected SynthesizerEngine parent;
	
	protected Wire[] inputWires;
	protected Wire[] outputWires;
	
	public Module(SynthesizerEngine parent, int numInputWires, int numOutputWires)
	{
		this.parent = parent;
		this.inputWires = new Wire[numInputWires];
		this.outputWires = new Wire[numOutputWires];
	}
	
	public abstract short requestNextSample(int outputWireIndex);
	
	public void connectInputWire(int index, Wire wire)
	{
		inputWires[index] = wire;
	}
	
	public void connectOutputWire(int index, Wire wire)
	{
		outputWires[index] = wire;
	}
}

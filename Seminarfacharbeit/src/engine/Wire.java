package engine;

public class Wire 
{
	private Module outputModule;
	private Module inputModule;
	
	public Wire (Module outputModule, Module inputModule, int inputModuleIndex, int outputModuleIndex)
	{
		this.outputModule = outputModule;
		this.inputModule = inputModule; 
		
		outputModule.connectInputWire(outputModuleIndex, this);
		inputModule.connectOutputWire(inputModuleIndex, this);
	}
	
	public short getNextSample()
	{
		return inputModule.requestNextSample(0);
	}
	
	public String toString()
	{
		return "Kabel von " + inputModule + " zu " + outputModule + ", id: " + this.hashCode();
	}

}

package engine;

public class Wire 
{
	private Module inputModule;
	private Module outputModule;
	
	public Wire (Module inputModule, Module outputModule)
	{
		this.inputModule = inputModule;
		this.outputModule = outputModule; 
		
		outputModule.connectOutputWire(0, this);
		inputModule.connectInputWire(0, this);
	}
	
	public short getNextSample()
	{
		return outputModule.requestNextSample();
	}

}

package engine;

public abstract class Module {

	private short inputSample;
	private short outputSample;

	public void setInputSample(short inputSample)
	{
		this.inputSample = inputSample;
	}
	
	public abstract short handleSample();
	
}

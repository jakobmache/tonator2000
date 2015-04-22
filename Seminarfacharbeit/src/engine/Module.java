
package engine;


public abstract class Module {
	
	private Wire inputWire;
	private Wire outputWire;
	
	protected ModuleContainer parent;
	private boolean isActive;
	
	public Module(ModuleContainer parent)
	{
		this.parent = parent;
	}
	
	public void processSample(short sampleValue) throws InterruptedException
	{
		short newSample = handleSample(sampleValue);
		if (outputWire != null)
			outputWire.sendSample(newSample);
	}

	public abstract short handleSample(short sampleValue) throws InterruptedException;
	
	public abstract void reset();
	
	public abstract void close();
	
	public boolean isActive() {
		return isActive;
	}

	public Wire getInputWire() {
		return inputWire;
	}

	public void connectInputWire(Wire inputWire) {
		System.out.printf("\tModule %s: InputWire %s was connected\n", toString(), inputWire.toString());
		this.inputWire = inputWire;
	}

	public Wire getOutputWire() {
		return outputWire;
	}

	public void connectOutputWire(Wire outputWire) {
		System.out.printf("\tModule %s: OutputWire %s was connected\n", toString(), outputWire.toString());
		this.outputWire = outputWire;
	}

	public ModuleContainer getParent() {
		return parent;
	}
	
	public SynthesizerEngine getEngine()
	{
		return parent.getEngine();
	}
	
}

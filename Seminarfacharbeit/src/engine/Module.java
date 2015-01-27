package engine;

public abstract class Module {

	private short inputSample;
	private short outputSample;
	
	private Wire inputWire;
	private Wire outputWire;
	
	public abstract void run();

	public Wire getInputWire() {
		return inputWire;
	}

	public void setInputWire(Wire inputWire) {
		this.inputWire = inputWire;
	}

	public Wire getOutputWire() {
		return outputWire;
	}

	public void setOutputWire(Wire outputWire) {
		this.outputWire = outputWire;
	}
	
}

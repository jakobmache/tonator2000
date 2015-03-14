package engine;

public abstract class Module {

	private short inputSample;
	private short outputSample;
	
	private Wire inputWire;
	private Wire outputWire;
	
	private boolean isActive;

	public abstract void handleEvent(Event event);
	
	public boolean isActive() {
		return isActive;
	}

	public Wire getInputWire() {
		return inputWire;
	}

	public void connectInputWire(Wire inputWire) {
		this.inputWire = inputWire;
	}

	public Wire getOutputWire() {
		return outputWire;
	}

	public void connectOutputWire(Wire outputWire) {
		this.outputWire = outputWire;
	}
	
}

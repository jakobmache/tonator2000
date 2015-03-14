package engine;

public class Wire {
	
	private Module inputModule;
	private Module outputModule;
	
	private short value;
	
	public Wire(Module inputModule, Module outputModule)
	{
		this.inputModule = inputModule;
		this.outputModule = outputModule;
	}

	public Module getInputModule() {
		return inputModule;
	}

	public void connectInputModule(Module inputModule) {
		this.inputModule = inputModule;
	}

	public Module getOutputModule() {
		return outputModule;
	}

	public void connectOutputModule(Module outputModule) {
		this.outputModule = outputModule;
	}

	public void sendEvent(Event event)
	{
		outputModule.handleEvent(event);
	}


}

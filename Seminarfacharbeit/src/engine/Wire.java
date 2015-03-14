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

	public void setInputModule(Module inputModule) {
		this.inputModule = inputModule;
	}

	public Module getOutputModule() {
		return outputModule;
	}

	public void setOutputModule(Module outputModule) {
		this.outputModule = outputModule;
	}

	public short getValue() {
		return value;
	}

	public void setValue(short value) {
		this.value = value;
	}


}

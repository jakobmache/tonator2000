package engine;

import modules.ModuleGenerator;

public abstract class PlayableModuleContainer extends ModuleContainer
{
	
	private int frequencyId;
	private int amplitudeId;
	
	public PlayableModuleContainer(SynthesizerEngine parent, int numInputWires, int numOutputWires, int id, String name, PlayableModuleContainer container)
	{
		super(parent, numInputWires, numOutputWires, id, name);
		
		for (Module module:container.getModules())
			modules.add(ModuleGenerator.createModule(module.getType(), parent, module.getName(), module.getId()));
		
		for (Wire wire:container.getWires())
		{
			Module moduleDataIsGrabbedFrom = wire.getModuleDataIsGrabbedFrom();
			Module moduleDataIsSentTo = wire.getModuleDataIsSentTo();
			
			if (moduleDataIsSentTo == container)
			{
				moduleDataIsSentTo = this;
			}
			
			addConnection(moduleDataIsGrabbedFrom, moduleDataIsSentTo, wire.getIndexDataIsGrabbedFrom(), wire.getIndexDataIsSentTo());
		}
		
		this.frequencyId = container.getFrequencyId();
		this.amplitudeId = container.getAmplitudeId();
	}
	
	public PlayableModuleContainer(SynthesizerEngine parent, int numInputWires, int numOutputWires, int id, String name)
	{
		super(parent, numInputWires, numOutputWires, id, name);
	}
	
	public abstract void startPlaying(float frequency, float amplitude);
	public abstract void stopPlaying();

	public int getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(int frequencyId) {
		this.frequencyId = frequencyId;
	}

	public int getAmplitudeId() {
		return amplitudeId;
	}

	public void setAmplitudeId(int amplitudeId) {
		this.amplitudeId = amplitudeId;
	}

}

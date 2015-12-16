package ui;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import engine.SynthesizerEngine;

public abstract class ModuleController 
{

	protected SynthesizerEngine parent;
	protected int id;
	protected int currProgram = 0;
	protected boolean enabled = true;

	public ModuleController(SynthesizerEngine parent, int id)
	{
		this.parent = parent;
		this.id = id;
		setModuleEnabled(true);
	}

	public void onMouseClicked(MouseEvent event)
	{

		if(event.getButton() == MouseButton.SECONDARY)
		{
			//Wir müssen das Modul deaktivieren
			if (enabled)
			{
				setModuleEnabled(false);
				setChildNodesEnabled(false);
				enabled = false;
			}
			else 
			{
				setModuleEnabled(true);
				setChildNodesEnabled(true);
				enabled = true;
			}
		}

	}

	public void setCurrProgram(int program)
	{
		currProgram = program;
		loadData();
	}

	protected abstract void update();
	
	public abstract void init();

	public abstract void loadData();

	public abstract void setModuleEnabled(boolean value);

	public abstract void setChildNodesEnabled(boolean value);



}

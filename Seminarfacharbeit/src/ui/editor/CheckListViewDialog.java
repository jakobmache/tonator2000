package ui.editor;

import java.util.HashMap;
import java.util.Map;

import org.controlsfx.control.CheckListView;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import javafx.util.Callback;
import modules.ModuleType;

public class CheckListViewDialog extends Dialog<Map<ModuleGuiBackend, Boolean>>
{
	
	private Map<ModuleGuiBackend, Boolean> modules;
	private Map<Integer, ModuleGuiBackend> inputs;
	
	private CheckListView<String> checkListView;
	
	public CheckListViewDialog(Stage owner, String title, String header, Map<ModuleGuiBackend, Boolean> oldValues)
	{
		this.modules = oldValues;
		System.out.println("Old values: " + oldValues);
		inputs = new HashMap<>();
		
		setTitle(title);
		setHeaderText(header);
		initInputs();
		
		initOwner(owner);
		
		getDialogPane().getButtonTypes().add(ButtonType.OK);
		
		setResultConverter(new Callback<ButtonType, Map<ModuleGuiBackend, Boolean>>() {
			
			@Override
			public Map<ModuleGuiBackend, Boolean> call(ButtonType param) 
			{
				if (param == ButtonType.OK)
				{
					for (int i:inputs.keySet())
					{
						Boolean res = checkListView.getCheckModel().isChecked(i);
						oldValues.put(inputs.get(i), res);
					}
					return oldValues;
				}
				else 
					return null;

			}
		});
	}
	
	private void initInputs()
	{
		checkListView = new CheckListView<>();
		int index = 0;
		for (ModuleGuiBackend module:modules.keySet())
		{
			if (module.getModuleType() == ModuleType.CONSTANT)
			{
				checkListView.getItems().add(module.getName());
				inputs.put(index, module);
				index++;
			}
		}
		
		for (Integer input:inputs.keySet())
		{
			ModuleGuiBackend module = inputs.get(input);
			if (modules.get(module))
			{
				checkListView.getCheckModel().check(input);
			}
		}

		getDialogPane().setContent(checkListView);
	}
	

}

package ui.utils;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class MultipleInputDialog extends Dialog<Float[]>
{
	
	private Label[] labels;
	private NumberInputField[] numberFields;
	
	public MultipleInputDialog(String title, String header, String[] labels)
	{
		this.labels = new Label[labels.length];
		for (int i = 0; i < labels.length; i++)
		{
			this.labels[i] = new Label(labels[i]);
		}
		
		numberFields = new NumberInputField[labels.length];
		setTitle(title);
		setHeaderText(header);
		initInputs();
		
		getDialogPane().getButtonTypes().add(ButtonType.OK);
		
		setResultConverter(new Callback<ButtonType, Float[]>() {
			
			@Override
			public Float[] call(ButtonType param) 
			{
				Float[] result = new Float[labels.length];
				if (param == ButtonType.OK)
				{
					for (int i = 0; i < labels.length; i++)
					{
						float res = Float.valueOf(numberFields[i].getText());
						result[i] = res;
					}
				}
				return result;
			}
		});
	}
	
	private void initInputs()
	{
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		for (int i = 0; i < labels.length; i++)
		{
			NumberInputField field = new NumberInputField(Float.MAX_VALUE, Float.MIN_VALUE, false);
			numberFields[i] = field;
			grid.add(labels[i], 0, i);
			grid.add(field, 1, i);
		}
		
		getDialogPane().setContent(grid);
	}
	
	

}

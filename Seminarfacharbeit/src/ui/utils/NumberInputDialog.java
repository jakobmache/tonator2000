package ui.utils;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Window;


public class NumberInputDialog extends TextInputDialog
{

	private int min;
	private int max;
	
	public NumberInputDialog(Window owner, String title, String header, String text, int min, int max)
	{
		initOwner(owner);
		setTitle(title);
		setHeaderText(header);
		setContentText(text);
		
		this.min = min;
		this.max = max;
		
		Button btOk = (Button) getDialogPane().lookupButton(ButtonType.OK);
		btOk.addEventFilter(ActionEvent.ACTION, event -> 
		{
			if (!isValid())
				event.consume();
		});
		
		getEditor().setStyle("-fx-focus-color: transparent;");

		getEditor().textProperty().addListener((observable, oldValue, newValue) ->
		{
			try
			{
				if (Integer.valueOf(newValue) < min || Integer.valueOf(newValue) > max)
				{
					DropShadow borderGlow = new DropShadow();
					borderGlow.setColor(Color.RED);
					borderGlow.setOffsetX(0f);
					borderGlow.setOffsetY(0f);
					getEditor().setEffect(borderGlow);
					return;
				}
				
				getEditor().setEffect(null);
			}
			catch(NumberFormatException e)
			{
				DropShadow borderGlow = new DropShadow();
				borderGlow.setColor(Color.RED);
				borderGlow.setOffsetX(0f);
				borderGlow.setOffsetY(0f);
				getEditor().setEffect(borderGlow);
			}
		});
	}
	
	public boolean isValid()
	{
		try
		{
			if (Integer.valueOf(getEditor().getText()) < min || Integer.valueOf(getEditor().getText()) > max)
				return false;
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

}

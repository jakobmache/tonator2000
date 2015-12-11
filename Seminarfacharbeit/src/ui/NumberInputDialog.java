package ui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Window;


public class NumberInputDialog extends TextInputDialog
{

	public NumberInputDialog(Window owner, String title, String header, String text)
	{
		initOwner(owner);
		setTitle(title);
		setHeaderText(header);
		setContentText(text);
		
		Button btOk = (Button) getDialogPane().lookupButton(ButtonType.OK);
		btOk.addEventFilter(ActionEvent.ACTION, event -> 
		{
			if (!isValid(getEditor().getText()))
				event.consume();
		});
		
		getEditor().setStyle("-fx-focus-color: transparent;");

		getEditor().textProperty().addListener((observable, oldValue, newValue) ->
		{
			try
			{
				if (Integer.valueOf(newValue) <= 0)
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
	
	public static boolean isValid(String text)
	{
		try
		{
			if (Integer.valueOf(text) > 0)
				return true;
			return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}

}

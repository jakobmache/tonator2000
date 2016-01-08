package ui.editor;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import resources.Strings;
import ui.MainApplication;
import engine.Module;
import engine.ModuleContainer;
import engine.SynthesizerEngine;

public class SynthesizerEditor extends Stage
{

	private MainApplication owner;
	private SynthesizerEngine engine;

	private ContextMenu menu;

	private Pane editorPane;

	private Node selectedNode;

	private BoundLine currentLine;

	private List<Node> nodes = new ArrayList<Node>();

	public SynthesizerEditor(MainApplication owner, SynthesizerEngine engine)
	{
		super();
		this.owner = owner;
		this.engine = engine;

		initEditor();
		initMenu();

		Scene scene = new Scene(editorPane, 1000, 1000);
		setScene(scene);
		
		show();

		initModules();
	}

	private void addModuleByType(int type, double layoutX, double layoutY)
	{
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(Strings.MODULE_NAME_INPUT_DIALOG_TITLE);
		dialog.setContentText(Strings.MODULE_NAME_INPUT_DIALOG_TEXT);
		dialog.setHeaderText(null);
		dialog.initOwner(this);
		String name = dialog.showAndWait().get();

		addModuleByType(type, name, layoutX, layoutY);
	}

	private void addModuleByType(int type, String name, double layoutX, double layoutY)
	{
		ModuleGui moduleGui = null;
		if (type == Module.CONSTANT)
		{	
			MultipleInputDialog floatDialog = new MultipleInputDialog("Parameter" , null, new String[]{"Maximaler Wert: ", "Minimaler Wert: ", "Standardwert: "});
			floatDialog.initOwner(this);
			Float[] result = floatDialog.showAndWait().get();
			moduleGui = createConstant(this, type, name, result[0], result[1], result[2]);
		}
		else
		{
			moduleGui = new ModuleGui(this, type, name);
		}

		addModuleByGui(moduleGui, layoutX, layoutY);
	}

	public void addModuleByGui(ModuleGui moduleGui, double layoutX, double layoutY)
	{
		Pane node = moduleGui.getGui();
		node.setLayoutX(layoutX);
		node.setLayoutY(layoutY);
		editorPane.getChildren().add(node);
		nodes.add(node);
	}

	private ConstantGui createConstant(SynthesizerEditor editor, int type, String name, float minValue, float maxValue, float defaultValue)
	{
		ConstantGui moduleGui = new ConstantGui(editor, type, name, maxValue, minValue, defaultValue);	
		return moduleGui;
	}

	private void initEditor()
	{
		editorPane = new Pane();
		editorPane.setOnMouseClicked((event) ->
		{
			//Rechtsklick bei gezogener Linie
			if (event.getClickCount() == 1 && event.getButton() == MouseButton.SECONDARY && currentLine != null)
			{
				currentLine.getStartCircle().deleteConnectedLine();
				editorPane.getChildren().remove(currentLine);
				currentLine = null;
			}

			//Rechtsklick sonst -> Menü
			else if (event.getClickCount() == 1 && event.getButton() == MouseButton.SECONDARY && currentLine == null)
			{
				menu.show(this, event.getScreenX(), event.getScreenY());
			}
		});

		editorPane.setOnKeyPressed((keyEvent) ->
		{
			if (keyEvent.getCode() == KeyCode.DELETE)
			{
				editorPane.getScene().setCursor(Cursor.DEFAULT);
				System.out.println(selectedNode);
				if (selectedNode != null)
					removeModuleGui(selectedNode);
			}
		});

		editorPane.setOnMouseMoved((event) ->
		{
			if (currentLine != null)
			{
				currentLine.setEnd(event.getX(), event.getY());
			}
		});
	}

	public void setBoundLine(BoundLine line)
	{
		if (line != null)
		{
			line.setMouseTransparent(true);
		}
		else if (line == null && currentLine != null)
		{
			currentLine.setMouseTransparent(false);
		}
		currentLine = line;
	}

	private void initModules()
	{
		addModuleByType(Module.OUTPUT_MODULE, "Audioausgabe", 100, 100);
		addModuleByGui(new ConstantGui(this, Module.CONSTANT, "Frequenz", 100000000, 0, 440), 200, 200);
	}

	public BoundLine getBoundLine()
	{
		return currentLine;
	}

	public void setSelectedNode(Node selectedNode)
	{
		this.selectedNode = selectedNode;
		for (Node node:nodes)
		{
			if (node == selectedNode)
			{
				//DropShadow borderGlow = new DropShadow();
				//				borderGlow.setColor(Color.LIGHTBLUE);
				//				borderGlow.setOffsetX(0f);
				//				borderGlow.setOffsetY(0f);
				//node.setEffect(borderGlow);
				//node.setStyle("-fx-border-color:lightblue; -fx-border-radius:5; -fx-border-width:2");
			}
			else
			{
				node.setStyle(null);
			}
		}
	}

	private void initMenu()
	{
		menu = new ContextMenu();
		Menu addModuleMenu = new Menu("Modul hinzufügen");
		menu.getItems().add(addModuleMenu);

		for (int i = 0; i < Module.CREATABLE_MODULES.length; i++)
		{
			int type = Module.CREATABLE_MODULES[i];
			MenuItem item = new MenuItem(Strings.MODULE_NAMES[type]);
			addModuleMenu.getItems().add(item);
			item.setOnAction((event) ->
			{
				addModuleByType(type, menu.getAnchorX(), menu.getAnchorY());
			});
		}
	}

	private void removeModuleGui(Node gui)
	{
		editorPane.getChildren().remove(gui);
	}

	public Pane getPane()
	{
		return editorPane;
	}

}

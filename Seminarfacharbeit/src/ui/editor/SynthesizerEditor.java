package ui.editor;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import resources.Strings;
import ui.MainApplication;
import engine.Module;
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
	}
	
	private void addModule(int type, double layoutX, double layoutY)
	{
		Node node = null;
		ModuleGui moduleGui = new ModuleGui(this, type);
		node = moduleGui.getGui();
		node.setLayoutX(layoutX);
		node.setLayoutY(layoutY);
		editorPane.getChildren().add(node);
		nodes.add(node);
	}

	private void initEditor()
	{
		editorPane = new Pane();
		editorPane.setOnMouseClicked((event) ->
		{
			if (event.getClickCount() == 1 && event.getButton() == MouseButton.SECONDARY && currentLine != null)
			{
				editorPane.getChildren().remove(currentLine);
			}
			
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
				addModule(type, menu.getAnchorX(), menu.getAnchorY());
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

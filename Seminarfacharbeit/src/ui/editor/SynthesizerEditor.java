package ui.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.Module;
import engine.ModuleContainer;
import engine.SynthesizerEngine;
import engine.Wire;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modules.Ids;
import modules.ModuleGenerator;
import modules.ModuleType;
import resources.Strings;
import ui.mainwindow.MainApplication;
import ui.utils.MultipleInputDialog;

public class SynthesizerEditor extends Stage
{
	private SynthesizerEngine engine;

	private Map<Node, ModuleGuiBackend> moduleGuis = new HashMap<Node, ModuleGuiBackend>();

	private ContextMenu menu;

	private Pane editorPane;

	private Node selectedNode;

	private BoundLine drawnLine;
	private BoundLine highlightedLine;
	
	private ModuleGuiBackend outputBackend;

	public SynthesizerEditor(MainApplication owner, SynthesizerEngine engine)
	{
		super();
		this.engine = engine;

		initOwner(owner.getPrimaryStage());

		initEditor();
		initMenu();

		Scene scene = new Scene(editorPane, 1000, 1000);
		setScene(scene);

		initModules();
	}

	public void addModuleByGui(ModuleGuiBackend moduleGui, double layoutX, double layoutY)
	{
		Pane node = moduleGui.getGui();
		node.setLayoutX(layoutX);
		node.setLayoutY(layoutY);
		editorPane.getChildren().add(node);
		moduleGuis.put(node, moduleGui);
	}

	private void addModuleByType(ModuleType type, double layoutX, double layoutY)
	{
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(Strings.MODULE_NAME_INPUT_DIALOG_TITLE);
		dialog.setContentText(Strings.MODULE_NAME_INPUT_DIALOG_TEXT);
		dialog.setHeaderText(null);
		dialog.initOwner(this);
		String name = dialog.showAndWait().get();

		addModuleByType(type, name, layoutX, layoutY);
	}

	private void addModuleByType(ModuleType type, String name, double layoutX, double layoutY)
	{
		ModuleGuiBackend moduleGui = null;
		if (type == ModuleType.CONSTANT)
		{	
			MultipleInputDialog floatDialog = new MultipleInputDialog("Parameter" , null, new String[]{"Maximaler Wert: ", "Minimaler Wert: ", "Standardwert: "});
			floatDialog.initOwner(this);
			Float[] result = floatDialog.showAndWait().get();
			moduleGui = createConstant(this, type, name, result[0], result[1], result[2]);
		}
		else
		{
			moduleGui = new ModuleGuiBackend(this, type, name);
		}

		addModuleByGui(moduleGui, layoutX, layoutY);
	}

	private ConstantGui createConstant(SynthesizerEditor editor, ModuleType type, String name, float minValue, float maxValue, float defaultValue)
	{
		ConstantGui moduleGui = new ConstantGui(editor, type, name, maxValue, minValue, defaultValue);	
		return moduleGui;
	}

	private void initEditor()
	{
		editorPane = new Pane();
		editorPane.setOnMouseClicked((event) ->
		{
			//Always request focus, otherwise you cant remove focus from constant text fields
			editorPane.requestFocus();
			//Rightclick with drawn line -> remove it
			if (event.getClickCount() == 1 && event.getButton() == MouseButton.SECONDARY && drawnLine != null)
			{
				drawnLine.getStartCircle().deleteConnectedLine();
				editorPane.getChildren().remove(drawnLine);
				drawnLine = null;
			}

			//Rightclick else -> context menu
			else if (event.getClickCount() == 1 && event.getButton() == MouseButton.SECONDARY && drawnLine == null)
			{
				initMenu();
				menu.show(this, event.getScreenX(), event.getScreenY());
			}
		});

		editorPane.setOnMouseMoved((event) ->
		{
			//If we draw a line -> connect
			if (drawnLine != null)
			{
				drawnLine.setEnd(event.getX(), event.getY());
			}
		});

		editorPane.setOnKeyPressed((event) ->
		{
			//If we draw a line and press delete -> delete line
			if (event.getCode() == KeyCode.DELETE)
			{
				if (highlightedLine != null)
				{
					removeBoundLine(highlightedLine);
				}
			}
			//Just for debug
			if (event.getCode() == KeyCode.ENTER)
			{
				ModuleContainer container = buildSynthesizer();
				System.out.println("Modules: ");
				for (Module module:container.getModules())
				{
					System.out.println("\t"  +  module);
				}
				System.out.println("Wires: ");
				for (Wire wire:container.getWires())
				{
					System.out.println("\t" + wire);
				}
			}
		});
	}

	private void initMenu()
	{
		menu = new ContextMenu();
		Menu addModuleMenu = new Menu("Modul hinzufügen");
		menu.getItems().add(addModuleMenu);

		for (int i = 0; i < ModuleType.values().length; i++)
		{
			ModuleType type = ModuleType.values()[i];
			if (!type.isCreatable())
			{
				continue;
			}
			MenuItem item = new MenuItem(Strings.MODULE_NAMES[type.getIndex()]);
			addModuleMenu.getItems().add(item);
			item.setOnAction((event) ->
			{
				addModuleByType(type, menu.getX(), menu.getY());
			});
		}

		if (selectedNode != null )
		{
			if (moduleGuis.get(selectedNode).isDestroyable())
			{
				MenuItem deleteItem = new MenuItem("Modul löschen");
				Node nodeSave = selectedNode;
				deleteItem.setOnAction((ev) -> {
					removeModuleGui(nodeSave);
				});
				menu.getItems().add(deleteItem);
			}
		}
	}

	private void initModules()
	{
		outputBackend = new ModuleGuiBackend(this, ModuleType.OUTPUT_MODULE, "Audio");
		outputBackend.setDestroyable(false);
		addModuleByGui(outputBackend, 100, 100);
		ConstantGui freqGui = new ConstantGui(this, ModuleType.CONSTANT, "Frequenz", 100000000, 0, 0);
		freqGui.setDestroyable(false);
		addModuleByGui(freqGui,200,100);
		ConstantGui ampliGui = new ConstantGui(this, ModuleType.CONSTANT, "Amplitude", 32768, -32767, 0);
		ampliGui.setDestroyable(false);
		addModuleByGui(ampliGui,300, 100);
	}

	private void removeModuleGui(Node gui)
	{
		editorPane.getChildren().remove(gui);
		ModuleGuiBackend moduleGui = moduleGuis.get(gui);
		for (PortCircle input:moduleGui.getInputs())
		{
			if (input.getBoundLine() != null)
				removeBoundLine(input.getBoundLine());
		}
		for (PortCircle output:moduleGui.getOutputs())
		{
			if (output.getBoundLine() != null)
				removeBoundLine(output.getBoundLine());
		}
	}

	public BoundLine getBoundLine()
	{
		return drawnLine;
	}

	public Pane getPane()
	{
		return editorPane;
	}

	public void setBoundLine(BoundLine line)
	{
		if (line != null)
		{
			line.setMouseTransparent(true);
		}
		else if (line == null && drawnLine != null)
		{
			drawnLine.setMouseTransparent(false);
		}
		drawnLine = line;
	}

	public void setSelectedNode(Node selectedNode)
	{
		this.selectedNode = selectedNode;
	}

	//TODO:Fix connection between Two Inputs! What to do there?
	public ModuleContainer buildSynthesizer()
	{
		ModuleContainer returnContainer = new ModuleContainer(engine, 1, 1, Ids.getNextId(), "ModuleContainer");
		List<PortCircle> checkedPorts = new ArrayList<PortCircle>();

		for (ModuleGuiBackend backend:moduleGuis.values())
		{
			if (backend.getModuleType() == ModuleType.OUTPUT_MODULE)
				continue;
			
			Module module = ModuleGenerator.createModule(backend.getModuleType(), engine, backend.getName(), backend.getId());
			returnContainer.addModule(module);
		}
		
		for (ModuleGuiBackend endBackend:moduleGuis.values())
		{
			if (endBackend.getModuleType() == ModuleType.OUTPUT_MODULE)
				continue;
			
			for (int indexDataIsSentTo = 0; indexDataIsSentTo < endBackend.getInputs().length; indexDataIsSentTo++)
			{
				PortCircle input = endBackend.getInputs()[indexDataIsSentTo];
				if (!checkedPorts.contains(input))
				{
					BoundLine line = input.getBoundLine();
					if (line != null)
					{
						PortCircle circle = line.getStartCircle();
						if (circle.getOwner() == endBackend)
						{
							circle = line.getEndCircle();
						}
						ModuleGuiBackend startBackend = circle.getOwner();
						
						if (startBackend.getModuleType() == ModuleType.OUTPUT_MODULE)
							continue;
						
						Module moduleDataIsGrabbedFrom = returnContainer.findModuleById(startBackend.getId());
						Module moduleDataIsSentTo = returnContainer.findModuleById(input.getOwner().getId());
						
						returnContainer.addConnection(moduleDataIsGrabbedFrom, moduleDataIsSentTo, circle.getIndex(), indexDataIsSentTo);
					}
				}
			}
		}
		
		BoundLine outputWire = outputBackend.getInputs()[0].getBoundLine();
		PortCircle circle = outputWire.getStartCircle();
		if (circle.getOwner() == outputBackend)
		{
			circle = outputWire.getEndCircle();
		}
		Module lastModule = returnContainer.findModuleById(circle.getOwner().getId());
		returnContainer.addConnection(lastModule, returnContainer, circle.getIndex(), ModuleContainer.SAMPLE_INPUT);
		
		return returnContainer;
	}

	public void removeBoundLine(BoundLine line)
	{
		editorPane.getChildren().remove(line);
		line.getStartCircle().deleteConnectedLine();
		line.getEndCircle().deleteConnectedLine();
	}

	public void setHighlightedLine(BoundLine line)
	{
		highlightedLine = line;
	}

}

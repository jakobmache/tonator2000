package ui.editor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import org.controlsfx.tools.Borders;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import containers.PlayableModuleContainer;
import containers.SynthesizerModuleContainer;
import engine.Module;
import engine.ModuleContainer;
import engine.SynthesizerEngine;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import modules.Constant;
import modules.Ids;
import modules.ModuleType;
import resources.Strings;
import ui.mainwindow.MainApplication;
import ui.mainwindow.UiUtils;
import ui.utils.MultipleInputDialog;
import ui.utils.NumberInputDialog;

//TODO: Recording
//TODO: Highpass fix (Synthesizer)
//TODO: Help for editor modules
//TODO: Fix scrolling 
//TODO: Layout in main window

public class SynthesizerEditor extends Stage
{

	public static final int WIDTH = 3000;
	public static final int HEIGHT = 3000;
	private SynthesizerEngine engine;

	private Map<Node, ModuleGuiBackend> moduleGuis = new HashMap<Node, ModuleGuiBackend>();

	private ContextMenu menu;
	private MenuBar menuBar;

	private Pane editorPane;
	private VBox mainLayout;

	private Node selectedNode;

	private BoundLine drawnLine;
	private BoundLine highlightedLine;

	private ModuleGuiBackend outputBackend;
	private ConstantGui freqBackend;
	private ConstantGui amplBackend;

	private Map<ModuleGuiBackend, Boolean> setToZeroOnStop;

	protected MainApplication owner;

	public SynthesizerEditor(MainApplication owner, SynthesizerEngine engine)
	{
		super();
		this.engine = engine;
		this.owner = owner;
		mainLayout = new VBox();

		initOwner(owner.getPrimaryStage());

		initEditor();
		initMenu();
		initModules();

		ScrollPane root = new ScrollPane(editorPane);
		root.setPrefSize(WIDTH, HEIGHT);

		editorPane.setMinHeight(HEIGHT);
		editorPane.setMinWidth(WIDTH);

		mainLayout.getChildren().add(root);
		Scene scene = new Scene(mainLayout);
		setScene(scene);

		editorPane.requestFocus();

		menuBar.prefWidthProperty().bind(scene.widthProperty());
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
			moduleGui = createConstant(this, type, name, result[1], result[0], result[2]);
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
				initContextMenu();
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

			if (event.getCode() == KeyCode.A)
			{
				moveGuis(10);
			}
		});
	}

	private void initMenu()
	{
		menuBar = new MenuBar();
		menuBar.prefWidthProperty().bind(mainLayout.widthProperty());
		Menu fileMenu = new Menu(Strings.MENU_FILE);
		MenuItem saveItem = new MenuItem(Strings.MENU_SAVE);
		MenuItem loadItem = new MenuItem(Strings.MENU_LOAD);
		SeparatorMenuItem separator = new SeparatorMenuItem();
		MenuItem buildItem = new MenuItem(Strings.MENU_BUILD);

		fileMenu.getItems().add(saveItem);
		fileMenu.getItems().add(loadItem);
		fileMenu.getItems().add(separator);
		fileMenu.getItems().add(buildItem);

		saveItem.setOnAction((value) -> 
		{
			PlayableModuleContainer container = null;
			try
			{
				container = buildSynthesizer();
			}
			catch (Exception e)
			{
				Alert alert = UiUtils.generateExceptionDialog(this, e, Strings.ERROR_TITLE, Strings.ERROR_HEADERS[Strings.ERROR_BUILDING_SYNTHESIZER], 
						Strings.ERROR_EXPLANATIONS[Strings.ERROR_BUILDING_SYNTHESIZER]);
				alert.showAndWait();
				return;
			}

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle(Strings.TITLE_LOAD_SYNTHESIZER);
			fileChooser.getExtensionFilters().add(new ExtensionFilter("XML-Dateien", "*.xml"));
			fileChooser.setInitialFileName(Strings.SAVE_SYNTH_DEFAULT_FILENAME);

			File file = fileChooser.showSaveDialog(this);

			if (file != null)
			{
				try 
				{
					writeToFile(file.getPath(), container);
				}
				catch (Exception e) 
				{
					Alert alert = UiUtils.generateExceptionDialog(this, e, Strings.ERROR_TITLE, Strings.ERROR_HEADERS[Strings.ERROR_WRITING_FILE], 
							Strings.ERROR_EXPLANATIONS[Strings.ERROR_WRITING_FILE]);
					alert.showAndWait();
					return;
				}
			}
		});

		loadItem.setOnAction((value) -> 
		{
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle(Strings.TITLE_SAVE_SYNTHESIZER);
			fileChooser.getExtensionFilters().add(new ExtensionFilter("XML-Dateien", "*.xml"));

			File file = fileChooser.showOpenDialog(this);

			if (file == null)
				return;

			try 
			{
				clear();
				loadSynthesizer(file.getPath());
			}
			catch (Exception e) 
			{
				Alert alert = UiUtils.generateExceptionDialog(this, e, Strings.ERROR_TITLE, Strings.ERROR_HEADERS[Strings.ERROR_LOADING_SYNTHESIZER], 
						Strings.ERROR_EXPLANATIONS[Strings.ERROR_LOADING_SYNTHESIZER]);
				alert.showAndWait();
				initModules();
			}
		});

		buildItem.setOnAction((value) -> 
		{
			try
			{
				owner.showSynthesizer();
			}
			catch (Exception e)
			{
				Alert alert = UiUtils.generateExceptionDialog(this, e, Strings.ERROR_TITLE, Strings.ERROR_HEADERS[Strings.ERROR_BUILDING_SYNTHESIZER], 
						Strings.ERROR_EXPLANATIONS[Strings.ERROR_BUILDING_SYNTHESIZER]);
				alert.showAndWait();
				return;
			}

		});

		menuBar.getMenus().add(fileMenu);
		mainLayout.getChildren().add(menuBar);
	}

	protected void moveGuis(int offset)
	{
		System.out.printf("Move guis %d px\n", offset);
		for (Node gui:moduleGuis.keySet())
		{
			gui.setLayoutX(gui.getLayoutX() + offset);
			//			gui.setLayoutX(gui.getLayoutX() - offset);
		}
	}

	private void initContextMenu()
	{
		menu = new ContextMenu();
		Menu addModuleMenu = new Menu(Strings.ADD_MODULE_MENU_ITEM_TEXT);
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
				//Mixer was selected
				if (type == ModuleType.MIXER)
				{
					TextInputDialog dialog = new TextInputDialog();
					dialog.setTitle(Strings.MODULE_NAME_INPUT_DIALOG_TITLE);
					dialog.setContentText(Strings.MODULE_NAME_INPUT_DIALOG_TEXT);
					dialog.setHeaderText(null);
					dialog.initOwner(this);
					String name = dialog.showAndWait().get();

					NumberInputDialog mixerDialog = new NumberInputDialog(this, Strings.TITLE_MIXER_DIALOG, Strings.HEADER_MIXER_DIALOG, 
							Strings.TEXT_MIXER_DIALOG, 1, 1000);
					Integer numInputs = Integer.valueOf(mixerDialog.showAndWait().get());
					MixerBackend backend = new MixerBackend(this, ModuleType.MIXER, name, Ids.getNextId(), numInputs);
					addModuleByGui(backend, menu.getAnchorX(), menu.getAnchorY());
				}
				else 
				{
					addModuleByType(type, menu.getX(), menu.getY());
				}
			});
		}

		MenuItem waveForm = new MenuItem(Strings.WAVEFORM_SELECTOR_NAME);
		addModuleMenu.getItems().add(waveForm);
		waveForm.setOnAction((event) -> 
		{
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle(Strings.MODULE_NAME_INPUT_DIALOG_TITLE);
			dialog.setContentText(Strings.MODULE_NAME_INPUT_DIALOG_TEXT);
			dialog.setHeaderText(null);
			dialog.initOwner(this);
			String name = dialog.showAndWait().get();

			addModuleByGui(new WaveformSelector(this, name, 0), menu.getX(), menu.getY());
		});

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

			ModuleType type = moduleGuis.get(selectedNode).getModuleType();
			if (type != ModuleType.CONSTANT && type != ModuleType.OUTPUT_MODULE)
			{

				MenuItem helpItem = new MenuItem("Hilfe");
				helpItem.setOnAction((event) -> 
				{

					//Init help
					PopOver popOver = new PopOver();
					popOver.setDetachable(true);
					popOver.setTitle(Strings.MODULE_NAMES[type.getIndex()]);
					popOver.setDetached(true);
					popOver.setArrowLocation(ArrowLocation.TOP_CENTER);

					VBox content = new VBox();
					Label mainInfo = new Label(Strings.MODULE_DESCRIPTIONS[type.getIndex()]);
					mainInfo.setMaxWidth(500);
					mainInfo.setWrapText(true);
					Node borderedInfo = Borders.wrap(mainInfo).lineBorder().buildAll();
					content.getChildren().add(borderedInfo);

					StringBuilder builder = new StringBuilder();
					//Modul hat Parameter zum Darstellen
					for (int index = 0; index < Strings.INPUT_NAMES_EDITOR[type.getIndex()].length; index++)
					{
						builder.append("Minimaler Wert: ");
						builder.append(Strings.MINS_EDITOR[type.getIndex()][index]);
						builder.append(System.lineSeparator());

						builder.append("Maximaler Wert: ");
						builder.append(Strings.MAXS_EDITOR[type.getIndex()][index]);

						Node label = MainApplication.createTitledPane(Strings.INPUT_NAMES_EDITOR[type.getIndex()][index], builder.toString());
						content.getChildren().add(label);
						
						builder.setLength(0);
					}
					
					popOver.setContentNode(content);
					popOver.setAnchorX(menu.getAnchorX());
					popOver.setAnchorY(menu.getAnchorY());

					popOver.show(editorPane);
				});
				menu.getItems().add(helpItem);
			}
		}
		
		SeparatorMenuItem sep = new SeparatorMenuItem();
		menu.getItems().add(sep);

		MenuItem clearItem = new MenuItem(Strings.CLEAR_MENU_ITEM_TEXT);
		clearItem.setOnAction((value) -> 
		{
			clear();
			initModules();
		});

		menu.getItems().add(clearItem);

		MenuItem setToZeroOnStopItem = new MenuItem(Strings.ZERO_MODULES_MENU_ITEM_TEXT);
		setToZeroOnStopItem.setOnAction((value) -> 
		{
			editSetToZeroOnStop();
		});

		menu.getItems().add(setToZeroOnStopItem);

	}

	//TODO: Fix constant loading
	public void loadSynthesizer(String path) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		Document document = builder.parse(path);

		org.w3c.dom.Node amplNode = document.getElementsByTagName(PlayableModuleContainer.XML_PROPERTY_AMPLITUDEID).item(0);
		org.w3c.dom.Node freqNode = document.getElementsByTagName(PlayableModuleContainer.XML_PROPERTY_FREQUENCYID).item(0);
		org.w3c.dom.Node containerNode = document.getElementsByTagName(PlayableModuleContainer.XML_PROPERTY_CONTAINERID).item(0); 
		org.w3c.dom.Node audioXNode = document.getElementsByTagName(PlayableModuleContainer.XML_PROPERTY_OUTPUTX).item(0);
		org.w3c.dom.Node audioYNode = document.getElementsByTagName(PlayableModuleContainer.XML_PROPERTY_OUTPUTX).item(0);

		float amplitudeId = Float.valueOf(amplNode.getTextContent());
		float frequencyId = Float.valueOf(freqNode.getTextContent());
		int containerId = Integer.valueOf(containerNode.getTextContent());
		double audioX = Double.valueOf(audioXNode.getTextContent());
		double audioY = Double.valueOf(audioYNode.getTextContent());

		setToZeroOnStop = new HashMap<>();

		//Create each module
		NodeList moduleNodes = document.getElementsByTagName(PlayableModuleContainer.XML_MODULE);
		for (int i = 0; i < moduleNodes.getLength(); i++)
		{
			Element node = (Element) moduleNodes.item(i);

			org.w3c.dom.Node idNode = node.getElementsByTagName(PlayableModuleContainer.XML_MODULE_ID).item(0);
			org.w3c.dom.Node nameNode = node.getElementsByTagName(PlayableModuleContainer.XML_MODULE_NAME).item(0);
			org.w3c.dom.Node typeNode = node.getElementsByTagName(PlayableModuleContainer.XML_MODULE_TYPE).item(0);
			org.w3c.dom.Node xPosNode = node.getElementsByTagName(PlayableModuleContainer.XML_MODULE_XPOS).item(0);
			org.w3c.dom.Node yPosNode = node.getElementsByTagName(PlayableModuleContainer.XML_MODULE_YPOS).item(0);

			int id = Integer.valueOf(idNode.getTextContent());
			String name = nameNode.getTextContent();
			ModuleType type = ModuleType.valueOf(typeNode.getTextContent());
			double xPos = Double.valueOf(xPosNode.getTextContent());
			double yPos = Double.valueOf(yPosNode.getTextContent());

			//ModuleGuiBackend backend = new ModuleGuiBackend(this, type, name);
			ModuleGuiBackend backend = null;

			if (type == ModuleType.CONSTANT)
			{
				org.w3c.dom.Node minValueNode = node.getElementsByTagName(PlayableModuleContainer.XML_MODULE_MIN_VALUE).item(0);
				org.w3c.dom.Node maxValueNode = node.getElementsByTagName(PlayableModuleContainer.XML_MODULE_MAX_VALUE).item(0);
				org.w3c.dom.Node defValueNode = node.getElementsByTagName(PlayableModuleContainer.XML_MODULE_DEFAULT_VALUE).item(0);
				org.w3c.dom.Node editableNode = node.getElementsByTagName(PlayableModuleContainer.XML_MODULE_IS_EDITABLE).item(0);
				org.w3c.dom.Node setToZeroNode = node.getElementsByTagName(PlayableModuleContainer.XML_MODULE_STOPTOZERO).item(0);

				float minValue = Float.valueOf(minValueNode.getTextContent());
				float maxValue = Float.valueOf(maxValueNode.getTextContent());
				float defaultValue = Float.valueOf(defValueNode.getTextContent());
				boolean isEditable = Boolean.valueOf(editableNode.getTextContent());
				boolean setToZero = Boolean.valueOf(setToZeroNode.getTextContent());

				//Look whether it is a waveform selector
				if (node.getElementsByTagName(PlayableModuleContainer.XML_MODULE_SUBTYPE).getLength() > 0)
				{
					backend = new WaveformSelector(this, name, (int) defaultValue);
				}
				else
				{
					backend = new ConstantGui(this, type, name, maxValue, minValue, defaultValue);
				}

				((ConstantGui) backend).setEditable(isEditable);

				//Set amplitude and freq backend
				if (id == amplitudeId)
				{
					backend.setDestroyable(false);
					amplBackend = (ConstantGui) backend;
				}
				else if (id == frequencyId)
				{
					backend.setDestroyable(false);
					freqBackend = (ConstantGui) backend;
				}

				//Check freq to zero
				setToZeroOnStop.put(backend, setToZero);
			}
			else 
			{
				if (type == ModuleType.MIXER)
				{
					org.w3c.dom.Node numInputsNode = node.getElementsByTagName(PlayableModuleContainer.XML_MODULE_NUM_INPUTS).item(0);
					Integer numInputs = Integer.valueOf(numInputsNode.getTextContent());
					backend = new MixerBackend(this, type, name, id, numInputs);
				}
				else
				{
					backend = new ModuleGuiBackend(this, type, name);
				}
			}

			backend.setId(id);


			System.out.printf("Loaded backend %s with name %s, ID %d, type %s : %s\n", backend, backend.getName(), backend.getId(), backend.getModuleType(), backend.getModule());

			addModuleByGui(backend, xPos, yPos);
		}

		//Add audio output
		outputBackend = new ModuleGuiBackend(this, ModuleType.OUTPUT_MODULE, "Audio");
		outputBackend.setDestroyable(false);
		outputBackend.setId(containerId);
		addModuleByGui(outputBackend, audioX, audioY);

		//Create each wire
		NodeList wireNodes = document.getElementsByTagName(PlayableModuleContainer.XML_WIRE);

		for (int i = 0; i < wireNodes.getLength(); i++)
		{
			Element wireElement = (Element) wireNodes.item(i);

			org.w3c.dom.Node fromIndexNode = wireElement.getElementsByTagName(PlayableModuleContainer.XML_WIRE_FROM_INDEX).item(0);
			org.w3c.dom.Node toIndexNode = wireElement.getElementsByTagName(PlayableModuleContainer.XML_WIRE_TO_INDEX).item(0);
			org.w3c.dom.Node fromIdNode = wireElement.getElementsByTagName(PlayableModuleContainer.XML_WIRE_FROM_ID).item(0);
			org.w3c.dom.Node toIdNode = wireElement.getElementsByTagName(PlayableModuleContainer.XML_WIRE_TO_ID).item(0);

			int fromIndex = Integer.valueOf(fromIndexNode.getTextContent());
			int toIndex = Integer.valueOf(toIndexNode.getTextContent());
			int fromId = Integer.valueOf(fromIdNode.getTextContent());
			int toId = Integer.valueOf(toIdNode.getTextContent());

			BoundLine line = new BoundLine(this);

			editorPane.getChildren().add(line);

			System.out.printf("Try to create wire from backend %s to %s, output %d to input %d\n", getBackendById(fromId).getName(),
					getBackendById(toId).getName(), fromIndex, toIndex);
			System.out.printf("Output wires: %s, Input wires:%s\n", Arrays.toString(getBackendById(fromId).getOutputs()), 
					Arrays.toString(getBackendById(toId).getInputs()));

			line.setStartCircle(getBackendById(fromId).getOutputs()[fromIndex]);
			line.setEndCircle(getBackendById(toId).getInputs()[toIndex]);

			line.update();
		}
	}

	public void clear()
	{
		for (Node node:moduleGuis.keySet())
		{
			removeModuleGui(node);
		}

		moduleGuis.clear();

		setToZeroOnStop = null;
	}

	public void editSetToZeroOnStop()
	{
		if (setToZeroOnStop == null)
		{
			setToZeroOnStop = new HashMap<>();
			for (ModuleGuiBackend backend:getBackends())
				setToZeroOnStop.put(backend, false);
		}

		CheckListViewDialog dialog = new CheckListViewDialog(this, Strings.TITLE_ZERO_DIALOG, Strings.HEADER_ZERO_DIALOG, setToZeroOnStop);
		Optional<Map<ModuleGuiBackend, Boolean>> optResult = dialog.showAndWait();
		if (optResult.isPresent())
			setToZeroOnStop = optResult.get();
		else 
			setToZeroOnStop = null;
	}

	public void writeToFile(String path, PlayableModuleContainer container) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, SAXException, IOException
	{
		if (setToZeroOnStop == null)
			editSetToZeroOnStop();

		container.writeToXmlFile(path);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(path);

		NodeList moduleNodes = document.getElementsByTagName(PlayableModuleContainer.XML_MODULE);

		for (int i = 0; i < moduleNodes.getLength(); i++)
		{
			Element moduleNode = (Element) moduleNodes.item(i);
			Integer id = Integer.valueOf(moduleNode.getElementsByTagName(
					PlayableModuleContainer.XML_MODULE_ID).item(0).getTextContent());
			ModuleType type = ModuleType.valueOf(moduleNode.getElementsByTagName(
					PlayableModuleContainer.XML_MODULE_TYPE).item(0).getTextContent());

			//If it's a constant -> write default / max / min value / editable to file
			if (type == ModuleType.CONSTANT)
			{
				ConstantGui backend = (ConstantGui) getBackendById(id);

				Element maxValue = document.createElement(PlayableModuleContainer.XML_MODULE_MAX_VALUE);
				Element minValue = document.createElement(PlayableModuleContainer.XML_MODULE_MIN_VALUE);
				Element defValue = document.createElement(PlayableModuleContainer.XML_MODULE_DEFAULT_VALUE);
				Element editNode = document.createElement(PlayableModuleContainer.XML_MODULE_IS_EDITABLE);

				maxValue.setTextContent(Float.toString(backend.getMaxValue()));
				minValue.setTextContent(Float.toString(backend.getMinValue()));
				defValue.setTextContent(Float.toString(backend.getDefaultValue()));
				editNode.setTextContent(Boolean.toString(backend.isEditable()));

				moduleNode.appendChild(maxValue);
				moduleNode.appendChild(minValue);
				moduleNode.appendChild(defValue);
				moduleNode.appendChild(editNode);

				if (backend.getClass() == WaveformSelector.class)
				{
					Element subtypeNode = document.createElement(PlayableModuleContainer.XML_MODULE_SUBTYPE);
					subtypeNode.setTextContent(backend.getClass().toString());

					moduleNode.appendChild(subtypeNode);
				}
			}

			//If it is a mixer we have to add number inputs
			if (getBackendById(id).getModuleType() == ModuleType.MIXER)
			{
				Element numInputsNode = document.createElement(PlayableModuleContainer.XML_MODULE_NUM_INPUTS);
				numInputsNode.setTextContent(Integer.toString(((MixerBackend) getBackendById(id)).getInputs().length));

				moduleNode.appendChild(numInputsNode);
			}

			//Write the gui position to the file
			Node gui = getModuleGuiById(id);

			if (gui != null)
			{
				Element xPos = document.createElement(PlayableModuleContainer.XML_MODULE_XPOS);
				xPos.setTextContent(Double.toString(gui.getLayoutX()));
				moduleNode.appendChild(xPos);
			}

			if (gui != null)
			{
				Element yPos = document.createElement(PlayableModuleContainer.XML_MODULE_YPOS);
				yPos.setTextContent(Double.toString(gui.getLayoutY()));
				moduleNode.appendChild(yPos);
			}

		}

		Element outputX = document.createElement(PlayableModuleContainer.XML_PROPERTY_OUTPUTX);
		Element outputY = document.createElement(PlayableModuleContainer.XML_PROPERTY_OUTPUTY);

		outputX.setTextContent(Double.toString(getModuleGuiByBackend(outputBackend).getLayoutX()));
		outputY.setTextContent(Double.toString(getModuleGuiByBackend(outputBackend).getLayoutY()));

		document.getFirstChild().appendChild(outputX);
		document.getFirstChild().appendChild(outputY);

		Transformer tr = TransformerFactory.newInstance().newTransformer();
		tr.setOutputProperty(OutputKeys.INDENT, "yes");
		tr.setOutputProperty(OutputKeys.METHOD, "xml");
		tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		tr.transform(new DOMSource(document), 
				new StreamResult(new FileOutputStream(path)));
	}

	private Node getModuleGuiById(int id)
	{
		for (Node node:moduleGuis.keySet())
		{
			Module module = moduleGuis.get(node).getModule();
			if (module != null && module.getId() == id)
				return node;
		}
		return null;
	}

	private Node getModuleGuiByBackend(ModuleGuiBackend backend)
	{
		for (Node node:moduleGuis.keySet())
		{
			if (moduleGuis.get(node) == backend)
				return node;
		}
		return null;
	}

	private ModuleGuiBackend getBackendById(int id)
	{
		for (ModuleGuiBackend backend:moduleGuis.values())
		{
			if (backend.getId() == id)
				return backend;
		}
		return null;
	}

	private void initModules()
	{
		outputBackend = new ModuleGuiBackend(this, ModuleType.OUTPUT_MODULE, "Audio");
		outputBackend.setDestroyable(false);
		addModuleByGui(outputBackend, 100, 100);
		freqBackend = new ConstantGui(this, ModuleType.CONSTANT, "Frequenz", 100000000, 0, 0);
		freqBackend.setDestroyable(false);
		freqBackend.setEditable(false);
		addModuleByGui(freqBackend, 200,100);
		amplBackend = new ConstantGui(this, ModuleType.CONSTANT, "Amplitude", 32768, -32767, 0);
		amplBackend.setDestroyable(false);
		amplBackend.setEditable(false);
		addModuleByGui(amplBackend, 300, 100);
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
	public PlayableModuleContainer buildSynthesizer()
	{
		System.out.println("Try to build synthesizer: " + setToZeroOnStop);
		SynthesizerModuleContainer returnContainer = new SynthesizerModuleContainer(engine, 1, 1, Ids.getNextId(), "ModuleContainer");
		Set<PortCircle> checkedPorts = new HashSet<PortCircle>();

		if (setToZeroOnStop == null)
			editSetToZeroOnStop();

		System.out.println("================Creating modules=====================");
		//Create each module
		for (int i = 0; i < getBackends().size(); i++)
		{
			ModuleGuiBackend backend = getBackends().get(i);
			if (backend.getModuleType() == ModuleType.OUTPUT_MODULE)
				continue;

			System.out.println("Added backend module: " + backend + "(" + backend.getModule() + ")");
			Module module = backend.getModule();
			returnContainer.addModule(module);

			System.out.println(setToZeroOnStop);
			System.out.println(module);
			if (module.getType() == ModuleType.CONSTANT)
				((Constant) module).setToZeroOnStop(setToZeroOnStop.get(backend));
		}

		System.out.println("================Creating wires=====================");
		//Create wires
		for (ModuleGuiBackend endBackend:moduleGuis.values())
		{
			if (endBackend.getModuleType() == ModuleType.OUTPUT_MODULE)
				continue;

			//Go from each input 
			for (int indexDataIsSentTo = 0; indexDataIsSentTo < endBackend.getInputs().length; indexDataIsSentTo++)
			{
				PortCircle input = endBackend.getInputs()[indexDataIsSentTo];
				if (!checkedPorts.contains(input))
				{
					BoundLine line = input.getBoundLine();
					if (line != null)
					{
						//First look for the start circle
						PortCircle startCircle = line.getStartCircle();

						//Line was drawn the other way
						if (startCircle.getOwner() == endBackend)
							startCircle = line.getEndCircle();

						ModuleGuiBackend startBackend = startCircle.getOwner();

						if (startBackend.getModuleType() == ModuleType.OUTPUT_MODULE)
							continue;

						int fromIndex = indexOf(startBackend.getOutputs(), startCircle);

						System.out.printf("Start circle index: %d, End circle index: %d\n", startCircle.getIndex(), input.getIndex());

						Module moduleDataIsGrabbedFrom = returnContainer.findModuleById(startBackend.getId());
						Module moduleDataIsSentTo = returnContainer.findModuleById(input.getOwner().getId());
						System.out.printf("Wire from %s.%d (%s) to %s.%d (%s)\n", moduleDataIsGrabbedFrom, fromIndex, startBackend.getName(), moduleDataIsSentTo, indexDataIsSentTo, endBackend.getName());
						returnContainer.addConnection(moduleDataIsGrabbedFrom, moduleDataIsSentTo, fromIndex, indexDataIsSentTo);
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

		System.out.printf("Wire from %s (%s) to %s (%s)\n", lastModule, lastModule.getName(), returnContainer, returnContainer.getName());

		returnContainer.setAmplitudeId(amplBackend.getId());
		returnContainer.setFrequencyId(freqBackend.getId());

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

	public SynthesizerEngine getEngine()
	{
		return engine;
	}

	public List<ModuleGuiBackend> getBackends()
	{
		return  new ArrayList<ModuleGuiBackend>(moduleGuis.values());
	}

	public static int indexOf(Object[] array, Object object)
	{
		for (int i = 0; i < array.length; i++)
		{
			if (object == array[i])
				return i;
		}
		return array.length;
	}

}

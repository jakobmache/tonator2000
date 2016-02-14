package containers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import engine.Module;
import engine.ModuleContainer;
import engine.SynthesizerEngine;
import engine.Wire;
import modules.Constant;
import modules.Ids;
import modules.Mixer;
import modules.ModuleGenerator;
import modules.ModuleType;

public abstract class PlayableModuleContainer extends ModuleContainer
{
	
	private int frequencyId;
	private int amplitudeId;
	
	public static final String XML_ROOT = "ModuleContainer";
	
	public static final String XML_MODULE = "Module";
	public static final String XML_MODULE_TYPE = "Type";
	public static final String XML_MODULE_ID = "Id";
	public static final String XML_MODULE_NAME = "Name";
	public static final String XML_MODULE_XPOS = "XPos";
	public static final String XML_MODULE_YPOS = "YPos";
	public static final String XML_MODULE_DEFAULT_VALUE = "DefaultValue";
	public static final String XML_MODULE_MAX_VALUE = "MaxValue";
	public static final String XML_MODULE_MIN_VALUE = "MinValue";
	public static final String XML_MODULE_IS_EDITABLE = "IsEditable";
	public static final String XML_MODULE_SUBTYPE = "Subtype";
	public static final String XML_MODULE_NUM_INPUTS = "NumInputs";
	
	public static final String XML_WIRE = "Wire";
	public static final String XML_WIRE_FROM_ID = "FromId";
	public static final String XML_WIRE_TO_ID = "ToId";
	public static final String XML_WIRE_FROM_INDEX = "FromIndex";
	public static final String XML_WIRE_TO_INDEX = "ToIndex";
	
	public static final String XML_MODULE_STOPTOZERO = "FreqToZeroOnStop";
	public static final String XML_PROPERTY_AMPLITUDEID = "AmplitudeId";
	public static final String XML_PROPERTY_FREQUENCYID = "FrequencyId";
	public static final String XML_PROPERTY_CONTAINERID = "ContainerId";
	public static final String XML_PROPERTY_OUTPUTX = "OutputX";
	public static final String XML_PROPERTY_OUTPUTY = "OutputY";
	
	public PlayableModuleContainer(SynthesizerEngine parent, int numInputWires, int numOutputWires, int id, String name, PlayableModuleContainer container)
	{
		super(parent, numInputWires, numOutputWires, id, name);
		
		for (Module module:container.getModules())
		{
			Module copy;
			if (module.getType() == ModuleType.MIXER)
				copy = ModuleGenerator.createMixer(parent, module.getName(), module.getId(), ((Mixer) module).getNumInputs());
			else
				copy = ModuleGenerator.createModule(module.getType(), parent, module.getName(), module.getId());

			if (module.getType() == ModuleType.CONSTANT)
			{
				((Constant) copy).setValue(((Constant)module).requestNextSample(Constant.VALUE_OUTPUT));
				((Constant) copy).setToZeroOnStop(((Constant) module).isToZeroOnStop());
			}
			
			modules.add(copy);
		}
	
		for (Wire wire:container.getWires())
		{
			Module moduleDataIsGrabbedFrom = wire.getModuleDataIsGrabbedFrom();
			Module moduleDataIsSentTo = wire.getModuleDataIsSentTo();
			
			Module moduleDataIsGrabbedFromCopy = findModuleById(moduleDataIsGrabbedFrom.getId());
			Module moduleDataIsSentToCopy = findModuleById(moduleDataIsSentTo.getId());
			
			if (moduleDataIsSentTo == container)
			{
				moduleDataIsSentToCopy = this;
			}
			
			addConnection(moduleDataIsGrabbedFromCopy, moduleDataIsSentToCopy, wire.getIndexDataIsGrabbedFrom(), wire.getIndexDataIsSentTo());
		}
		
		this.frequencyId = container.getFrequencyId();
		this.amplitudeId = container.getAmplitudeId();
	}
	
	public PlayableModuleContainer(SynthesizerEngine parent, int numInputWires, int numOutputWires, int id, String name)
	{
		super(parent, numInputWires, numOutputWires, id, name);
	}
	
	/**
	 * Creates a ModuleContainer from an XML-File.
	 * Structure:
	 * 
	 * <ModuleContainer>
	 * 			<Module>
	 * 				<Type>OSCILLATOR</Type>
	 * 				<XPos>0</YPos>
	 * 				<YPos>0</YPos>
	 * 				<Id>1</Id>
	 * 			</Module>
	 * 			<Wire>
	 * 				<FromId>1</FromId>
	 * 				<ToId>2</ToId>
	 * 			</Wire>
	 * 			<FreqToZeroOnStop>true</FreqToZeroOnStop>
	 * </ModuleContainer>
	 * 
	 * @param parent
	 * @param numInputWires
	 * @param numOutputWires
	 * @param id
	 * @param name
	 * @param xmlPath
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public PlayableModuleContainer(SynthesizerEngine parent, int numInputWires, int numOutputWires, String name, String xmlPath) throws ParserConfigurationException, SAXException, IOException
	{
		super(parent, numInputWires, numOutputWires, Ids.getNextId(), name);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document document = builder.parse(xmlPath);
		
		Node freqIdNode = document.getElementsByTagName(XML_PROPERTY_FREQUENCYID).item(0);
		setFrequencyId(Integer.valueOf(freqIdNode.getTextContent()));
		
		Node amplIdNode = document.getElementsByTagName(XML_PROPERTY_AMPLITUDEID).item(0);
		setAmplitudeId(Integer.valueOf(amplIdNode.getTextContent()));
		
		
		
		Node containerIdNode = document.getElementsByTagName(XML_PROPERTY_CONTAINERID).item(0);
		this.moduleId = Integer.valueOf(containerIdNode.getTextContent());
		
		NodeList moduleNodes = document.getElementsByTagName(XML_MODULE);
		
		for (int i = 0; i < moduleNodes.getLength(); i++)
		{
			Element node = (Element) moduleNodes.item(i);
			
			String moduleName = node.getElementsByTagName(XML_MODULE_NAME).item(0).getTextContent();
			Integer moduleId = Integer.valueOf(node.getElementsByTagName(XML_MODULE_ID).item(0).getTextContent());
			ModuleType moduleType = ModuleType.valueOf(node.getElementsByTagName(XML_MODULE_TYPE).item(0).getTextContent());
			
			Module module = ModuleGenerator.createModule(moduleType, parent, moduleName, moduleId);
			addModule(module);
			
			if (moduleType == ModuleType.CONSTANT)
			{
				Float value = Float.valueOf(node.getElementsByTagName(XML_MODULE_DEFAULT_VALUE).item(0).getTextContent());
				Node freqToZeroNode = document.getElementsByTagName(XML_MODULE_STOPTOZERO).item(0);
				((Constant) module).setValue(value);
				((Constant) module).setToZeroOnStop(Boolean.valueOf(freqToZeroNode.getTextContent()));
			}
		}
		
		NodeList wireNodes = document.getElementsByTagName(XML_WIRE);
				
		for (int i = 0; i < wireNodes.getLength(); i++)
		{
			Element node = (Element) wireNodes.item(i);
			
			Integer fromId = Integer.valueOf(node.getElementsByTagName(XML_WIRE_FROM_ID).item(0).getTextContent());
			Integer toId = Integer.valueOf(node.getElementsByTagName(XML_WIRE_TO_ID).item(0).getTextContent());
			Integer fromIndex = Integer.valueOf(node.getElementsByTagName(XML_WIRE_FROM_INDEX).item(0).getTextContent());
			Integer toIndex = Integer.valueOf(node.getElementsByTagName(XML_WIRE_TO_INDEX).item(0).getTextContent());
			
			Module fromModule = findModuleById(fromId);
			Module toModule = findModuleById(toId);
			
			if (toId == moduleId)
				toModule = this;
			
			addConnection(fromModule, toModule, fromIndex, toIndex);
		}
	}
	
	public void writeToXmlFile(String path) throws ParserConfigurationException, TransformerFactoryConfigurationError, FileNotFoundException, TransformerException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.newDocument();
		
		Element rootElement = document.createElement(XML_ROOT);
		
		for (Module module:modules)
		{
			Element element = document.createElement(XML_MODULE);
			Element name = document.createElement(XML_MODULE_NAME);
			Element id = document.createElement(XML_MODULE_ID);
			Element type = document.createElement(XML_MODULE_TYPE);

			name.setTextContent(module.getName());
			id.setTextContent(Integer.toString(module.getId()));
			type.setTextContent(module.getType().toString());
			
			element.appendChild(name);
			element.appendChild(id);
			element.appendChild(type);
			
			if (module.getType() == ModuleType.CONSTANT)
			{
				Element defaultValue = document.createElement(XML_MODULE_DEFAULT_VALUE);
				Float value = ((Constant) module).requestNextSample(Constant.VALUE_OUTPUT);
				defaultValue.setTextContent(Float.toString(value));
				
				Element moduleToZeroNode = document.createElement(XML_MODULE_STOPTOZERO);
				moduleToZeroNode.setTextContent(Boolean.toString(((Constant) module).isToZeroOnStop()));
				
				element.appendChild(defaultValue);
				element.appendChild(moduleToZeroNode);
			}
			
			rootElement.appendChild(element);
		}
		
		for (Wire wire:wires)
		{
			Element element = document.createElement(XML_WIRE);
			Element fromId = document.createElement(XML_WIRE_FROM_ID);
			Element toId = document.createElement(XML_WIRE_TO_ID);
			Element fromIndex = document.createElement(XML_WIRE_FROM_INDEX);
			Element toIndex = document.createElement(XML_WIRE_TO_INDEX);

			fromId.setTextContent(Integer.toString(wire.getModuleDataIsGrabbedFrom().getId()));
			toId.setTextContent(Integer.toString(wire.getModuleDataIsSentTo().getId()));
			fromIndex.setTextContent(Integer.toString(wire.getIndexDataIsGrabbedFrom()));
			toIndex.setTextContent(Integer.toString(wire.getIndexDataIsSentTo()));
			
			element.appendChild(fromId);
			element.appendChild(toId);
			element.appendChild(fromIndex);
			element.appendChild(toIndex);
			
			rootElement.appendChild(element);
		}
		
		Element freqIdNode = document.createElement(XML_PROPERTY_FREQUENCYID);
		freqIdNode.setTextContent(Integer.toString(getFrequencyId()));
		
		Element amplIdNode = document.createElement(XML_PROPERTY_AMPLITUDEID);
		amplIdNode.setTextContent(Integer.toString(getAmplitudeId()));
		
		Element containerIdNode = document.createElement(XML_PROPERTY_CONTAINERID);
		containerIdNode.setTextContent(Integer.toString(getId()));
		
		rootElement.appendChild(freqIdNode);
		rootElement.appendChild(amplIdNode);
		rootElement.appendChild(containerIdNode);
		
		document.appendChild(rootElement);
		
        Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty(OutputKeys.INDENT, "yes");
        tr.setOutputProperty(OutputKeys.METHOD, "xml");
        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        tr.transform(new DOMSource(document), 
                             new StreamResult(new FileOutputStream(path)));
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

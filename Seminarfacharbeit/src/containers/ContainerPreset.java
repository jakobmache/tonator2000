package containers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ContainerPreset 
{
	public static final String ROOT_TAG = "params";
	public static final String PARAM_TAG = "param";
	public static final String ATTRIBUTE_TAG = "id";
	
	public Map<Integer, Float> params = new HashMap<Integer, Float>();
	
	public ContainerPreset()
	{
	}
	
	public ContainerPreset(String xmlPath) throws ParserConfigurationException, SAXException, IOException
	{
		loadFromXml(xmlPath);
	}
	
	public void loadFromXml(String path) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document document = builder.parse(path);
		
		NodeList paramNodes = document.getElementsByTagName(PARAM_TAG);
		for (int i = 0; i < paramNodes.getLength(); i++)
		{
			Node node = paramNodes.item(i);
			int id = Integer.parseInt(node.getAttributes().getNamedItem(ATTRIBUTE_TAG).getNodeValue());
			System.out.println(node);
			float value = Float.parseFloat(node.getTextContent());
			params.put(id, value);
		}
	}
	
	public void setParam(int id, float value)
	{
		params.put(id, value);
	}
	
	public float getParam(int id)
	{
		return params.get(id);
	}
	
	public List<Integer> getIds()
	{
		return new ArrayList<Integer>(params.keySet());
	}
	
	public void writeToFile(String path) throws ParserConfigurationException, FileNotFoundException, TransformerException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.newDocument();
		
		Element rootElement = document.createElement(ROOT_TAG);
		
		for (Integer param:params.keySet())
		{
			Element element = document.createElement(PARAM_TAG);
			element.setAttribute(ATTRIBUTE_TAG, param.toString());
			element.appendChild(document.createTextNode(params.get(param).toString()));
			rootElement.appendChild(element);
		}
		
		document.appendChild(rootElement);
		
        Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty(OutputKeys.INDENT, "yes");
        tr.setOutputProperty(OutputKeys.METHOD, "xml");
        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        tr.transform(new DOMSource(document), 
                             new StreamResult(new FileOutputStream(path)));
	}

}

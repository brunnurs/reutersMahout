package com.zuehlke.reuters.mahout.importer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.zuehlke.reuters.mahout.ReutersMessage;



public class ReutersMessageParser extends DefaultHandler
{
	protected final static String FILE = "ADDYOURFILEPATH";

	
	private boolean isInReuters;
	private boolean isInTopics;

	private List<ReutersMessage> allMessages = new ArrayList<ReutersMessage>();
	
	private String currentMessageId;
	private String currentBody;
	private List<String> currentTopics = new ArrayList<String>();
	
	private StringBuffer content = new StringBuffer();

	private void setImporterToXMLReader(XMLReader xmlReader)
			throws IOException, SAXException, FileNotFoundException {
		xmlReader.setContentHandler(this);
		xmlReader.setErrorHandler(this);
	}

	private static XMLReader createSAXxmlReader() throws ParserConfigurationException, SAXException {
		XMLReader xmlReader = null;
		
		SAXParserFactory spfactory = SAXParserFactory.newInstance();
		// Get SAXParser from the factory
		SAXParser saxParser = spfactory.newSAXParser();
		
		// The SAXParser wraps an XMLReader
		xmlReader = saxParser.getXMLReader();
		xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		xmlReader.setFeature("http://xml.org/sax/features/validation", false);
		

		return xmlReader;
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes)
	{
		if(qName.equalsIgnoreCase("REUTERS")) {
			currentMessageId = attributes.getValue("NEWID");
			isInReuters = true;
		} else if(isInReuters && qName.equalsIgnoreCase("TOPICS")) {
			isInTopics = true;
		}
	}

	public void endElement(String nameSpaceURI, String localName, String qName)
	{
		if(qName.equalsIgnoreCase("REUTERS")) {
			createNewReutersMessages();			
			cleanCurrentFields();
			
			isInReuters = false;
		} else if(isInReuters && qName.equalsIgnoreCase("TOPICS")) {
			isInTopics = false;
		} else if(isInTopics && qName.equalsIgnoreCase("D")) {
			currentTopics.add(StringUtils.trim(content.toString()));
		} else if(isInReuters && qName.equalsIgnoreCase("BODY")) {
			currentBody = content.toString();
		}
		
		content.delete(0, content.length());
	}

	private void createNewReutersMessages() {
		for (String topic : currentTopics) {
			allMessages.add(new ReutersMessage(currentMessageId,topic,currentBody));
		}
	}

	private void cleanCurrentFields() {
		currentTopics.clear();
		currentBody = null;
		currentMessageId = null;
	}

	public void characters(char [] text, int start, int length)
	{
		content.append(text,start,length);
	}

	public void endDocument()
	{
	}

	
	public List<ReutersMessage> parseData(InputStream inputStream) throws ParseException {

		try {
			XMLReader xmlReader = createSAXxmlReader();
			setImporterToXMLReader(xmlReader);
			xmlReader.parse(new InputSource(inputStream));
			return allMessages;
			
		} catch (IOException e) {
			throw new ParseException(e);
		} catch (SAXException e) {
			throw new ParseException(e);
		} catch (ParserConfigurationException e) {
			throw new ParseException(e);
		}
	}
	

}
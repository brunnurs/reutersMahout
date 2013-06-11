package com.zuehlke.reuters.mahout.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;



public class ReutersDataImporter extends DefaultHandler
{
	protected final static String FILE = "ADDYOURFILEPATH";

	private StringBuffer content = new StringBuffer();

	public static void main (String[] args) throws FileNotFoundException, IOException, SAXException 
	{
		XMLReader xmlReader = createSAXxmlReader();
		createDataImporter(xmlReader);
		
		xmlReader.parse(new InputSource(new FileReader(new File(FILE))));
	}

	private static void createDataImporter(XMLReader xmlReader)
			throws IOException, SAXException, FileNotFoundException {
		ReutersDataImporterTest importer = new ReutersDataImporterTest();
//		xmlReader.setContentHandler(importer);
//		xmlReader.setErrorHandler(importer);
	}

	private static XMLReader createSAXxmlReader() {
		XMLReader xmlReader = null;
		
		try {
			SAXParserFactory spfactory = SAXParserFactory.newInstance();
			spfactory.setNamespaceAware(true);
			spfactory.setValidating(true);
			// Get SAXParser from the factory
			SAXParser saxParser = spfactory.newSAXParser();
			// The SAXParser wraps an XMLReader
			xmlReader = saxParser.getXMLReader();
		}
		catch (Exception e) { e.printStackTrace(System.err);}
		return xmlReader;
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes)
	{
		if(qName.equals("PERSON"))
		{
			if(attributes.getValue("KATEGORIE").equalsIgnoreCase("Privat"))
			{
			}
		}
	}

	public void endElement(String nameSpaceURI, String localName, String qName)
	{
		if(qName.equals("ORT"))
		{
				
		}
		content.delete(0, content.length());
	}

	public void characters(char [] text, int start, int length)
	{
		content.append(text,start,length);

	}

	public void endDocument()
	{
	}
}
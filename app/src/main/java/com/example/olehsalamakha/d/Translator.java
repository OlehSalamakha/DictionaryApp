package com.example.olehsalamakha.d;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Created by olehsalamakha on 1/16/15.
 */
public class Translator {
	public String mWord = "";
	final String LINK = "https://translate.yandex.net/api/v1.5/tr/translate?key=trnsl.1.1.20150115T201508Z.e1a75fff07a29d86.0f1b562cfeb6f9305cc52081db7139cd41fec327&lang=en-ukr&text=";

	public void translate(String word) throws MalformedURLException, ParserConfigurationException, SAXException, IOException {
		mWord =  getTranslatedWrod(getXML(word));
	}

	public String getTranslatedWord() {
		return mWord;
	}

	private String getXML(String word) throws IOException {
		String result ="";
		InputStream is;
		is = new URL(LINK+word).openStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		result = readAll(rd);
		return result;

	}

	private String getTranslatedWrod(String xml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));

		NodeList l = doc.getElementsByTagName("text");
		return l.item(0).getTextContent();
	}

	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

}






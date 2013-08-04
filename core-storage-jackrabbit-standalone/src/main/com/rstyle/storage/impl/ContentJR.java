package main.com.rstyle.storage.impl;

import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.Property;

import main.com.rstyle.storage.api.Content;
import main.com.rstyle.storage.api.StorageException;

import org.apache.jackrabbit.JcrConstants;

/**
 * Реализация контента как в файловой системе.
 * 
 * @author Vladislav.Dremin
 */

public class ContentJR implements Content {

	private final String name;
	private final String mimeType;
	//private final String path;
	private InputStream captureSource;

	public ContentJR(String name, String mimeType, String path) {
		this.name = name;
		//this.path = path;
		this.mimeType = mimeType;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getMimeType() {
		return null;
	}

	@Override
	public InputStream accessContentStream() throws StorageException {
		return null;
	}

	@Override
	public void setCaptureSource(InputStream inputStream) {
		this.captureSource = inputStream;
	}

	// Utility method to persist content

	public void save(Node doc) throws Exception {
		if (captureSource == null) { // если источник не установлен
			return;
		}

		Node fileNode = doc.addNode(name, "nt:file");

		Node resNode = fileNode.addNode(JcrConstants.JCR_CONTENT,
				JcrConstants.NT_RESOURCE);
		resNode.setProperty(JcrConstants.JCR_MIMETYPE, mimeType);
		resNode.setProperty(JcrConstants.JCR_ENCODING, "utf-8");
		resNode.setProperty(JcrConstants.JCR_DATA, captureSource);	
		//resNode.getProperty(JcrConstants.JCR_DATA);
	}

}

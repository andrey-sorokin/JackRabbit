package main.com.rstyle.storage.impl;

import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import main.com.rstyle.storage.api.Content;
import main.com.rstyle.storage.api.Document;
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
	private final Document doc;
	private InputStream captureSource;

	public ContentJR(String name, String mimeType, DocumentJR doc) {
		this.name = name;
		this.doc = doc;
		this.mimeType = mimeType;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getMimeType() {
		return mimeType;
	}

	@Override
	public InputStream accessContentStream() throws StorageException {
		InputStream contentStream = null;

		try {
			Node root = ((StoreJR) ((DocumentJR) doc).getStoreJR()).getRoot();
			Node contentNode = root.getNode(doc.getId()).getNode(name);

			contentStream = contentNode.getProperty(JcrConstants.JCR_DATA)
					.getBinary().getStream();
		} catch (ValueFormatException e) {
			e.printStackTrace();
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}

		return contentStream;

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
	}

}

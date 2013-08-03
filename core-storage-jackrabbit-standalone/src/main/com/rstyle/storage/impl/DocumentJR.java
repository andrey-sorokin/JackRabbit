package main.com.rstyle.storage.impl;

import java.util.UUID;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

import main.com.rstyle.storage.api.AbstractDocument;
import main.com.rstyle.storage.api.Content;

import org.apache.jackrabbit.JcrConstants;

/**
 * Реализация документа как набора узлов в JackRabbit.
 * 
 * @author Andrey.Sorokin
 * @author Andrey Sorokin <Andrey.Sorokin@R-Style.com>
 * 
 * @version 1.0
 */

public class DocumentJR extends AbstractDocument {

	private StoreJR storeJR;

	public DocumentJR(StoreJR storeJR) {
		super(UUID.randomUUID().toString());
		this.storeJR = storeJR;
	}

	public DocumentJR(StoreJR storeJR, String id) {
		super(id);
		this.storeJR = storeJR;
	}

	@Override
	public Content createContentInstance(String name, String mimeType) {
		return new ContentJR("content", mimeType, "/" + id);
	}

	public void load() {
		Node root = storeJR.getRoot();

		try {
			Node doc = root.getNode(id);
			NodeIterator entries = doc.getNodes("content");

			while (entries.hasNext()) {
				Node entry = entries.nextNode();

				// get node mime type
				//Property mimeType = entry
				//		.getProperty(JcrConstants.JCR_MIMETYPE);
				String mimeType_ = entry.getProperty(JcrConstants.JCR_MIMETYPE)
						.getString();
				// entry.getm
				//contents.put("key", new ContentJR("content", "mimeType", ""));
			}

		} catch (RepositoryException e) {
			e.printStackTrace();
		}

	}

}

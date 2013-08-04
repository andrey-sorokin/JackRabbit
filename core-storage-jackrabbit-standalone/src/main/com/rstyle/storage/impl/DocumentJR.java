package main.com.rstyle.storage.impl;

import java.util.UUID;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import main.com.rstyle.storage.api.AbstractDocument;
import main.com.rstyle.storage.api.Content;
import main.com.rstyle.storage.api.Store;
import main.util.Constants;
import main.util.StringUtil;

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

	private Store storeJR;

	public DocumentJR(Store storeJR) {
		super(UUID.randomUUID().toString());
		this.storeJR = storeJR;
	}

	public DocumentJR(Store storeJR, String id) {
		super(id);
		this.storeJR = storeJR;
	}

	public Store getStoreJR() {
		return storeJR;
	}

	@Override
	public Content createContentInstance(String name, String mimeType) {
		return new ContentJR("content_" + name, mimeType, this);
	}

	public void load() {
		Node root = ((StoreJR) storeJR).getRoot();

		try {
			Node doc = root.getNode(id);
			NodeIterator entries = doc.getNodes(Constants.CONTENTS_NODE_PREFIX
					.concat("*"));

			while (entries.hasNext()) {
				Node entry = entries.nextNode();

				Node file = entry.getNode(JcrConstants.JCR_CONTENT);

				String mimeType = file.getProperty(JcrConstants.JCR_MIMETYPE)
						.getString();

				String name = StringUtil.getRightSubString(entry.getName(),
						Constants.CONTENTS_NODE_PREFIX.length());

				contents.put(name, new ContentJR(name, mimeType, this));
			}

		} catch (RepositoryException e) {
			e.printStackTrace();
		}

	}

}

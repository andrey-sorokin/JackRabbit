package main.com.rstyle.storage.impl;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import main.com.rstyle.storage.api.AttributeFilter;
import main.com.rstyle.storage.api.Content;
import main.com.rstyle.storage.api.Document;
import main.com.rstyle.storage.api.StorageException;
import main.com.rstyle.storage.api.Store;

import org.apache.jackrabbit.core.TransientRepository;

public class StoreJR implements Store {

	final private static Repository repository = new TransientRepository();

	private Session session;
	private Node root;

	public StoreJR() {
		try {
			this.session = repository.login(new SimpleCredentials("admin",
					"admin".toCharArray()));
			this.root = session.getRootNode();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return session.getWorkspace().getName();
	}

	public Node getRoot() {
		return root;
	}
	
	@Override
	public Document findDocument(String id) throws StorageException {
		DocumentJR doc;

		try {
			Node node = root.getNode(id);
			doc = new DocumentJR(this, node.getName());
			doc.load();
		} catch (PathNotFoundException e) {
			throw new StorageException("", e);
		} catch (RepositoryException e) {
			throw new StorageException("", e);
		}

		return doc;
	}

	@Override
	public void deleteDocument(String id) {
	}

	@Override
	public List<Document> find(AttributeFilter filter) {
		return null;
	}

	@Override
	public void commit(Document document) throws StorageException {

		try {
			// create Document root node
			Node doc = root.addNode(document.getId());

			for (Content content : document.getContentList()) {

				// add child documents for each children
				((ContentJR) content).save(doc);
			}
			session.save();
		} catch (Exception e) {
			throw new StorageException("Error document creation or updating", e);
		}

	}

	@Override
	public Document createInstance() {
		return new DocumentJR(this);
	}

}

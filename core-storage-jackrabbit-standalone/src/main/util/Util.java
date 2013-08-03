package main.util;

import javax.jcr.AccessDeniedException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import javax.jcr.lock.Lock;
import javax.jcr.lock.LockException;
import javax.jcr.lock.LockManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {

	private static Logger log = LoggerFactory.getLogger(Util.class);

	public static synchronized Lock getNodeLock(Session session, String path,
			String user) throws UnsupportedRepositoryOperationException,
			LockException, AccessDeniedException, RepositoryException {

		Lock lock = null;
		LockManager lockManager = session.getWorkspace().getLockManager();

		try {
			lock = lockManager.getLock(path);
			if (lock.getLockToken() != null) {
				return lock;
			}
		} catch (LockException e) {
			log.error(e.getMessage());
		}
		lock = null;
		long sleepTime = 100;
		int tries = 0;
		while (tries++ < 300) {
			try {
				return lockManager.lock(path, true, true, 3000, user);
			} catch (Exception ex) {
				if (sleepTime < 500) {
					sleepTime = sleepTime + 10;
				}
				try {
					if (tries % 100 == 0) {
						log.info(Thread.currentThread() + " Waiting for "
								+ sleepTime + " ms " + tries);
					}
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					log.error(e.getMessage());
				}
			}
		}
		throw new Error("Failed to lock node");
	}

	public static void nodeUnlock(Session session, String path, String user)
			throws UnsupportedRepositoryOperationException, LockException,
			AccessDeniedException, RepositoryException {

		LockManager lockManager = session.getWorkspace().getLockManager();

		try {
			lockManager.unlock(path);
		} catch (LockException e) {
			log.error(e.getMessage());
		}

	}

	/**
	 * Recursively outputs the contents of the given node.
	 */
	private static void dump(Node node) throws RepositoryException {
		// First output the node path
		System.out.println(node.getPath());
		// Skip the virtual (and large!) jcr:system subtree
		if (node.getName().equals("jcr:system")) {
			return;
		}

		// Then output the properties
		PropertyIterator properties = node.getProperties();
		while (properties.hasNext()) {
			Property property = properties.nextProperty();
			if (property.getDefinition().isMultiple()) {
				// A multi-valued property, print all values
				Value[] values = property.getValues();
				for (Value value : values) {
					System.out.println(property.getPath() + " = "
							+ value.getString());
				}
			} else {
				// A single-valued property
				System.out.println(property.getPath() + " = "
						+ property.getString());
			}
		}

		// Finally output all the child nodes recursively
		NodeIterator nodes = node.getNodes();
		while (nodes.hasNext()) {
			dump(nodes.nextNode());
		}
	}

}

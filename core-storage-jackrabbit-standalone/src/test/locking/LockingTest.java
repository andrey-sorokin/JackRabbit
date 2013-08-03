package test.locking;

import static java.lang.System.out;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.lock.LockManager;

import main.util.Util;

import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.core.TransientRepository;

public class LockingTest {

	static private TransientRepository repository = new TransientRepository();

	public static void main(String[] args) {
		new LockingTest().init();
	}

	private void init() {
		new Thread(new Producer(), "producer").start();
		new Thread(new Setter(), "setter").start();
	}

	class Producer implements Runnable {

		public void run() {
			Session session = null;
			try {
				session = login(repository);
				Node root = session.getRootNode();

				Node hello = root.addNode("hello");
				int index = hello.getIndex();
				session.save();

				OrderNumberHolder.setNode(index);

				hello.addMixin(JcrConstants.MIX_LOCKABLE);
				session.save();

				LockManager lockManager = session.getWorkspace()
						.getLockManager();

				String blockedNode = "/hello[" + index + "]";

				lockManager.lock(blockedNode, true, true, 3000, "admin");

				session.save();

				out.println("The node blocked status in Producer is "
						+ root.getNode("hello[" + index + "]").isLocked()
						+ " for node Hello" + index);

				while (true) {
					Thread.sleep(1000);
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				if (session != null && session.isLive()) {
					logout(session);
				}
			}
		}

	}

	class Setter implements Runnable {
		public void run() {

			Session session = null;

			try {
				while (true) {
					Thread.sleep(5000);
					session = login(repository);
					Node root = session.getRootNode();

					int index = OrderNumberHolder.getNode();

					Node hello = root.getNode("hello[" + index + "]");

					out.println("The node blocked status in Setter is "
							+ hello.isLocked() + " for node Hello" + index);

					// get lock
					if (Util.getNodeLock(session, "/hello[" + index + "]",
							"admin") != null) {
						// make changes
						hello.addNode("xxx");
						// persist changes
						session.save();
						// unlock node
						Util.nodeUnlock(session, "/hello[" + index + "]",
								"admin");
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				if (session != null && session.isLive()) {
					logout(session);
				}
			}
		}
	}

	static public class OrderNumberHolder {

		private static int node;

		public static synchronized int getNode() {
			return node;
		}

		public static synchronized void setNode(int nodee) {
			node = nodee;
		}

	}

	public static void logout(Session session) {
		session.logout();
	}

	public Session login(Repository repository) {
		Session session = null;
		try {
			session = repository.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return session;

	}
}
package test.load;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;

public class MultiLoadTest implements Runnable {

	static private TransientRepository repository = new TransientRepository();
	private static final int NTHREDS = 100;

	public static void main(String[] args) {
		for (int i = 0; i < NTHREDS; i++) {
			new Thread(new MultiLoadTest()).start();
		}
	}

	public void run() {
		Random generator = new Random();
		Session session = null;
		String thread = Thread.currentThread().getName();

		while (true) {
			try {
				// generate shape number
				int shape = generator.nextInt(6);

				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss");
				Date date = new Date();

				System.out
						.println((dateFormat.format(date)
								+ " the shape number " + shape
								+ "  is processing " + thread));

				session = new Util().login(repository);
				// produce shapes and store in the repository
				Shapes.produce(session.getRootNode(), shape, thread);
				// make changes persistent
				session.save();

				// consume shapes
				Shapes.consume(session.getRootNode(), shape, thread);

				// delete shapes
				Shapes.delete(session.getRootNode(), shape, thread);
				// make changes persistent
				session.save();
				new Util().logout(session);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				if (session != null && session.isLive()) {
					new Util().logout(session);
				}
			}

		}

	}

	public static class Shapes {

		public static synchronized void produce(Node root, int shape,
				String thread) throws Exception {

			if (shape == 1) {
				Node shape1 = root.addNode("shape1" + thread);
				Node a = shape1.addNode("a" + thread);

				Node c = a.addNode("c" + thread);
				c.addNode("d" + thread);
				c.addNode("e" + thread);

				Node b = a.addNode("b" + thread);
				b.addNode("f" + thread);
				b.addNode("g" + thread);
			}

			if (shape > 1) {
				Node shape2 = root.addNode("shape2" + thread);
				Node a = shape2.addNode("a" + thread);

				Node c = a.addNode("c" + thread);
				c.addNode("d" + thread);
				c.addNode("e" + thread);

				Node b = a.addNode("b" + thread);
				b.addNode("f" + thread);
				b.addNode("g" + thread);

			}
			if (shape > 2) {
				Node shape3 = root.addNode("shape3" + thread);
				Node a = shape3.addNode("a" + thread);

				Node c = a.addNode("c" + thread);
				c.addNode("d" + thread);
				c.addNode("e" + thread);

				Node b = a.addNode("b" + thread);
				b.addNode("f" + thread);
				b.addNode("g" + thread);

			}
			if (shape > 3) {
				Node shape4 = root.addNode("shape4" + thread);
				Node a = shape4.addNode("a" + thread);

				Node c = a.addNode("c" + thread);
				c.addNode("d" + thread);
				c.addNode("e" + thread);

				Node b = a.addNode("b" + thread);
				b.addNode("f" + thread);
				b.addNode("g" + thread);

			}

			if (shape > 4) {
				Node shape5 = root.addNode("shape5" + thread);
				Node a = shape5.addNode("a" + thread);

				Node c = a.addNode("c" + thread);
				c.addNode("d" + thread);
				c.addNode("e" + thread);

				Node b = a.addNode("b" + thread);
				b.addNode("f" + thread);
				b.addNode("g" + thread);

			}

		}

		public static synchronized void consume(Node root, int shape,
				String thread) throws Exception {
			if (shape == 1) {
				Node shape1 = root.getNode("shape1" + thread);
				Node a = shape1.getNode("a" + thread);

				Node c = a.getNode("c" + thread);
				c.getNode("d" + thread);
				c.getNode("e" + thread);

				Node b = a.getNode("b" + thread);
				b.getNode("f" + thread);
				b.getNode("g" + thread);
			}

			if (shape > 1) {
				Node shape2 = root.getNode("shape2" + thread);
				Node a = shape2.getNode("a" + thread);

				Node c = a.getNode("c" + thread);
				c.getNode("d" + thread);
				c.getNode("e" + thread);

				Node b = a.getNode("b" + thread);
				b.getNode("f" + thread);
				b.getNode("g" + thread);

			}
			if (shape > 2) {
				Node shape3 = root.getNode("shape3" + thread);
				Node a = shape3.getNode("a" + thread);

				Node c = a.getNode("c" + thread);
				c.getNode("d" + thread);
				c.addNode("e" + thread);

				Node b = a.getNode("b" + thread);
				b.getNode("f" + thread);
				b.getNode("g" + thread);

			}
			if (shape > 3) {
				Node shape4 = root.getNode("shape4" + thread);
				Node a = shape4.getNode("a" + thread);

				Node c = a.getNode("c" + thread);
				c.getNode("d" + thread);
				c.getNode("e" + thread);

				Node b = a.getNode("b" + thread);
				b.getNode("f" + thread);
				b.getNode("g" + thread);

			}

			if (shape > 4) {
				Node shape5 = root.getNode("shape5" + thread);
				Node a = shape5.getNode("a" + thread);

				Node c = a.getNode("c" + thread);
				c.getNode("d" + thread);
				c.getNode("e" + thread);

				Node b = a.getNode("b" + thread);
				b.getNode("f" + thread);
				b.getNode("g" + thread);

			}
		}

		public static synchronized void delete(Node root, int shape,
				String thread) throws Exception {

			if (shape == 1) {
				root.getNode("shape1" + thread).remove();
			}

			if (shape > 1) {
				root.getNode("shape2" + thread).remove();
			}
			if (shape > 2) {
				root.getNode("shape3" + thread).remove();
			}
			if (shape > 3) {
				root.getNode("shape4" + thread).remove();
			}

			if (shape > 4) {
				root.getNode("shape5" + thread).remove();
			}

		}
	}

	public class Util {

		public Session login(Repository repository) {
			Session session = null;
			try {
				session = repository.login(new SimpleCredentials("admin",
						"admin".toCharArray()));
			} catch (LoginException e) {
				e.printStackTrace();
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
			return session;

		}

		public void logout(Session session) {
			session.logout();
		}

	}

}
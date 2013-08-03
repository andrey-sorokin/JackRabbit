package main.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.value.StringValue;
import sun.net.www.MimeTable;

/**
 * <code>JCRWiki</code>
 *
 * @author Vadim Solonovich <Vadim.Solonovich@R-Style.com>
 * @version 1.0
 */
public class JCRWiki {


    public static void main(String[] args) throws Exception {
        Repository repository = new TransientRepository();

        // Listing 7. Obtaining a repository, workspace,
        // and root node with the TransientRepository
        Session session = repository.login(
                new SimpleCredentials("admin", "admin".toCharArray()));
        try {
            Workspace ws = session.getWorkspace();
            Node rn = session.getRootNode();

            ws.getNamespaceRegistry().registerNamespace
                    ("wiki", "http://www.barik.net/wiki/1.0");

            // Adding content to a JCR repository
            {

                Node encyclopedia = rn.addNode("wiki:encyclopedia");

                Node p = encyclopedia.addNode("wiki:entry");
                p.setProperty("wiki:title",
                        new StringValue("rose"));
                p.setProperty("wiki:content",
                        new StringValue("A rose is a flowering shrub."));
                p.setProperty("wiki:category", new Value[]{
                        new StringValue("flower"),
                        new StringValue("plant"),
                        new StringValue("rose")});

                Node n = encyclopedia.addNode("wiki:entry");
                n.setProperty("wiki:title", new StringValue("Shakespeare"));
                n.setProperty("wiki:content", new
                        StringValue("A famous poet who likes roses."));
                n.setProperty("wiki:category", new StringValue("poet"));

                session.save();
            }


            //  Listing 9. Browsing a content repository
            {
                Node encyclopedia = rn.getNode("wiki:encyclopedia");
                NodeIterator entries = encyclopedia.getNodes("wiki:entry");

                while (entries.hasNext()) {

                    Node entry = entries.nextNode();

                    System.out.println(entry.getName());
                    System.out.println(entry.getProperty("wiki:title").getString());
                    System.out.println(entry.getProperty("wiki:content").getString());
                    System.out.println(entry.getPath());

                    Property category = entry.getProperty("wiki:category");

                    try {
                        String c = category.getValue().getString();
                        System.out.println("Category: " + c);
                    } catch (ValueFormatException e) {

                        Value[] categories = category.getValues();

                        for (Value c : categories) {
                            System.out.println("Category: " + c.getString());
                        }
                    }
                }
            }

            //  Listing 10. Searching content with XPath

            {
                QueryManager qm = ws.getQueryManager();
                {
                    Query q = qm.createQuery
                            ("//wiki:encyclopedia/wiki:entry[@wiki:title = 'rose']",
                                    Query.XPATH);

                    QueryResult result = q.execute();
                    NodeIterator it = result.getNodes();

                    while (it.hasNext()) {
                        Node n = it.nextNode();

                        System.out.println(n.getName());
                        System.out.println(n.getProperty("wiki:title").getString());
                        System.out.println(n.getProperty("wiki:content").getString());
                    }
                }

                Query q = qm.createQuery
                        ("//wiki:encyclopedia/" +
                                "wiki:entry[jcr:contains(@wiki:content, 'rose')]",
                                Query.XPATH);

                QueryResult result = q.execute();
                NodeIterator it = result.getNodes();
                while (it.hasNext()) {
                    Node n = it.nextNode();

                    System.out.println(n.getName());
                    System.out.println(n.getProperty("wiki:title").getString());
                    System.out.println(n.getProperty("wiki:content").getString());
                }


            }

//            Listing 11. Exporting data
            {


                File outputFile = new File("systemview.xml");
                FileOutputStream out = new FileOutputStream(outputFile);
                session.exportSystemView("/wiki:encyclopedia", out, false, false);

            }

//            Listing 12. Transferring data

            {
                File inputFile = new File("systemview.xml");
                FileInputStream in = new FileInputStream(inputFile);
                session.importXML
                        ("/", in, ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
                session.save();
            }

//            Listing 13. Adding binary content
            {


                File file = new File("rose.jpg");
                MimeTable mt = MimeTable.getDefaultTable();
                String mimeType = mt.getContentTypeFor(file.getName());
                if (mimeType == null) mimeType = "application/octet-stream";

                Node roseMode = rn.getNode("wiki:encyclopedia/wiki:entry[1]");
                System.out.println(roseMode.getName());

                Node fileNode = roseMode.addNode(file.getName(), "nt:file");
                Node resNode = fileNode.addNode("jcr:content", "nt:resource");
                resNode.setProperty("jcr:mimeType", mimeType);
                resNode.setProperty("jcr:encoding", "");
                resNode.setProperty("jcr:data", new FileInputStream(file));
                Calendar lastModified = Calendar.getInstance();
                lastModified.setTimeInMillis(file.lastModified());
                resNode.setProperty("jcr:lastModified", lastModified);


                session.save();

            }
        } finally {
            session.logout();

        }

    }


}

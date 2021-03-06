package main.examples.hops;

import java.io.FileInputStream;
import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;

import org.apache.jackrabbit.core.TransientRepository;

/**
 * Third Jackrabbit example application. Imports an example XML file
 * and outputs the contents of the entire workspace.
 */
public class ThirdHop {

    /**
     * The main entry point of the example application.
     *
     * @param args command line arguments (ignored)
     * @throws Exception if an error occurs
     */
    public static void main(String[] args) throws Exception {
        Repository repository = new TransientRepository();
        Session session = repository.login(
                new SimpleCredentials("admin", "admin".toCharArray()));
        try {
            Node root = session.getRootNode();

            // Import the XML file unless already imported
            if (!root.hasNode("importxml")) {
                System.out.print("Importing xml... ");

            // Create an unstructured node under which to import the XML
                Node node = root.addNode("importxml", "nt:unstructured");

            // Import the file "test.xml" under the created node
                FileInputStream xml = new FileInputStream("test.xml");
                session.importXML(
                        node.getPath(), xml, ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
                xml.close();
                session.save();
                System.out.println("done.");
            }

            //output the repository content
            dump(root);
        } finally {
            session.logout();
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
                    System.out.println(
                            property.getPath() + " = " + value.getString());
                }
            } else {
        // A single-valued property
                System.out.println(
                        property.getPath() + " = " + property.getString());
            }
        }

        // Finally output all the child nodes recursively
        NodeIterator nodes = node.getNodes();
        while (nodes.hasNext()) {
            dump(nodes.nextNode());
        }
    }

} 
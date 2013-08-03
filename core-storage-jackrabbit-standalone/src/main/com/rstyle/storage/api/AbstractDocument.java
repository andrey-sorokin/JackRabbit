package main.com.rstyle.storage.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <code>AbstractDocument</code>
 *
 * @author Vadim Solonovich <Vadim.Solonovich@R-Style.com>
 * @version 1.0
 */
public abstract class AbstractDocument implements Document {

    protected final String id;
    protected final Map<String, Content> contents = new HashMap<String, Content>();
    protected Map<String, Object> attributes = new HashMap<String, Object>();
    protected List<String> parents = new ArrayList<String>();
    protected List<String> children = new ArrayList<String>();

    public AbstractDocument() {
        this.id = UUID.randomUUID().toString();
    }

    public AbstractDocument(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<Content> getContentList() {
        return new ArrayList<Content>(contents.values());
    }

    @Override
    public Content getContent(String name) throws IOException {
        return contents.get(name);
    }

    @Override
    public abstract Content createContentInstance(String name, String mimeType);

    @Override
    public Document addContent(Content content) {
        contents.put(content.getName(), content);
        return this;
    }

    @Override
    public void deleteContent(String name) {
        contents.remove(name);
    }


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Document putAttribute(String name, Object value) {
        getAttributes().put(name, value);
        return this;
    }

    @Override
    public Document setAttributes(Map<String, Object> attrMap) {
        attributes = attrMap;
        return this;
    }

    @Override
    public List<String> getParentDocuments() {
        return parents;
    }

    @Override
    public void setParentDocuments(List<String> parents) {
        this.parents = parents;
    }

    @Override
    public List<String> getChildDocuments() {
        return children;
    }

    @Override
    public void setChildDocuments(List<String> children) {
        this.children = children;
    }

}

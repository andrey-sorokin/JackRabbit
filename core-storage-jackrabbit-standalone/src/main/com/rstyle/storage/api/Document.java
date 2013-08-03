package main.com.rstyle.storage.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Документ. Содержит список объектов контента и коллекцию свойств.
 * @author Vladislav.Dremin
 * @author Vadim Solonovich <Vadim.Solonovich@R-Style.com>
 * @version 1.0
 */
public interface Document {

    /**
     * Идентификатор документа
     * @return идентификатор
     */
    public String getId();

    /**
     * Список содержимого документа
     * @return список объектов контента
     */
    public List<Content> getContentList() ;

    /**
     * Получить контент документа по имени
     * @param name имя контента
     * @return контент
     */
    public Content getContent(String name) throws IOException;

    /**
     * Создать новый экземпляр контента документа
     * @param name наименование контента
     * @return контент
     */
    public Content createContentInstance(String name, String mimeType);

    /**
     * Добавить новый контент в документ
     * @param content контент
     */
    public Document addContent(Content content);

    /**
     * Удалить контент из документа
     * @param name имя контента
     */
    public void deleteContent(String name);

    /**
     * Получить список атрибутов
     * @return список атрибутов
     */
    public Map<String, Object> getAttributes() ;

    /**
     * Получить список атрибутов
     * @return список атрибутов
     */
    public Document putAttribute(String name, Object value);

    /**
     * Установить список атрибутов
     * @param attrs список атрибутов
     */
    public Document setAttributes(Map<String, Object> attrs);

    /**
     * Получить список идентификаторов родительских документов
     * @return список идентификаторов
     */
    public List<String> getParentDocuments();

    /**
     * Установить список идентификаторов родительских документов
     * @param parents список идентификаторов
     */
    public void setParentDocuments(List<String> parents);

    /**
     * Получить список идентификаторов дочерних документов
     * @return список идентификаторов
     */
    public List<String> getChildDocuments();

    /**
     * Установить список идентификаторов дочерних документов
     * @param children список идентификаторов
     */
    public void setChildDocuments(List<String> children);

}

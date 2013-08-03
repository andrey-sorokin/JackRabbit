package main.com.rstyle.storage.api;

import java.util.List;

/**
 * Хранилище документов
 * @author Vladislav.Dremin
 * @author Vadim Solonovich <Vadim.Solonovich@R-Style.com>
 * @version 1.0
 */
public interface Store {

    /**
     * Наименование хранилища
     * @return наименование
     */
    public String getName();

    /**
     * Получить документ по идентификатору
     *
     * @param id идентификатор
     * @return документ
     */
    public Document findDocument(String id) throws StorageException;

    /**
     * Удалить документ из хранилища
     * @param id идентификатор документа
     */
    public void deleteDocument(String id);

    /**
     * Поиск документов по списку свойств
     * @param filter список свойств
     * @return список документов
     */
    public List<Document> find(AttributeFilter filter);

    /**
     * Операция создания или обновления документа в хранилище
     * @param document документ
     */
    public void commit(Document document) throws StorageException;

    /**
     * Создать новый документ (factory method)
     * @return документ
     */
    public Document createInstance();
}

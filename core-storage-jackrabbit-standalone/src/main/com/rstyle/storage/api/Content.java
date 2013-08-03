package main.com.rstyle.storage.api;

import java.io.InputStream;

/**
 * Контент (часть содержимого либо представление) документа (i.e. скан-образ, XML, etс. ).
 * @author Vladislav.Dremin
 * @author Vadim Solonovich <Vadim.Solonovich@R-Style.com>
 * @version 1.0
 */
public interface Content {

    /**
     * Наименование контента
     * @return наименование
     */
    public String getName();

    /**
     * MIME тип контента
     * @return значение типа
     */
    public String getMimeType();

    /**
     * Получение потока для чтения контента.
     * @return экземпляр потока
     */
    public InputStream accessContentStream() throws StorageException;

    /**
     * Установить поток для загрузки контента.
     * @param inputStream экземпляр потока загрузки
     */
    public void setCaptureSource(InputStream inputStream);

}

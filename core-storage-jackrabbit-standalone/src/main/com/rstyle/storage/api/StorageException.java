package main.com.rstyle.storage.api;

import javax.jcr.RepositoryException;

/**
 * Исключение хранилища
 * @author Vladislav.Dremin
 * @version 1.0
 */
public class StorageException extends RepositoryException {

	private static final long serialVersionUID = 6394909653903034766L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

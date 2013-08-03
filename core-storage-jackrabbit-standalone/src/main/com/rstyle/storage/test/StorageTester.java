package main.com.rstyle.storage.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import main.com.rstyle.storage.api.Content;
import main.com.rstyle.storage.api.Document;
import main.com.rstyle.storage.api.StorageException;
import main.com.rstyle.storage.api.Store;
import main.com.rstyle.storage.impl.StoreJR;
import sun.net.www.MimeTable;

public class StorageTester {

	public static void main(String[] args) throws StorageException {

		try {
			File file = new File("c://temp/rose.jpg");
			InputStream is = new FileInputStream(file);
			MimeTable mt = MimeTable.getDefaultTable();
			String mimeType = mt.getContentTypeFor(file.getName());
			if (mimeType == null) mimeType = "application/octet-stream";
			
			// создает хранилище
			final Store store = new StoreJR();
			// создает экземпляр документа
			final Document document = store.createInstance();
			// создает экземпляр контента [utility methods to write/read, etc]
			final Content content = document.createContentInstance(
					"content", mimeType);
			// устанавливает источник данных для данного контента
			content.setCaptureSource(is);
			// формирует коллекцию контента, контент хранит источник данных
			document.addContent(content);
			// проходит по коллекции и сохраняет контент в store
			store.commit(document);
			
			Document doc = store.findDocument(document.getId());
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}

package main.com.rstyle.storage.test;

import static java.lang.System.out;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import main.com.rstyle.storage.api.Content;
import main.com.rstyle.storage.api.Document;
import main.com.rstyle.storage.api.StorageException;
import main.com.rstyle.storage.api.Store;
import main.com.rstyle.storage.impl.StoreJR;
import main.util.StringUtil;
import sun.net.www.MimeTable;

public class StorageTester {
	// создает хранилище
	static final Store store = new StoreJR();

	private static Document createDocument() throws StorageException {
		Document document = null;

		try {
			File file = new File("c://temp/rose.jpg");
			String fileName = file.getName();

			InputStream is = new FileInputStream(file);
			MimeTable mt = MimeTable.getDefaultTable();
			String mimeType = mt.getContentTypeFor(fileName);
			if (mimeType == null)
				mimeType = "application/octet-stream";

			// создает экземпляр документа
			document = store.createInstance();
			// создает экземпляр контента [utility methods to write/read, etc]

			final Content content = document.createContentInstance(
					StringUtil.getLeftSubString(fileName,
							StringUtil.getFileExtlength(fileName)), mimeType);

			// устанавливает источник данных для данного контента
			content.setCaptureSource(is);
			// формирует коллекцию контента, контент хранит источник данных
			document.addContent(content);
			// проходит по коллекции и сохраняет контент в store
			store.commit(document);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return document;
	}

	private static Document findDocument(String id) throws StorageException {
		return store.findDocument(id);
	}

	public static void main(String[] args) throws StorageException {

		args = new String[] { "find" };

		if (args[0].equals("create")) {
			out.println(createDocument().getId());
		} else if (args[0].equals("find")) {
			findDocument("fabb4a8d-ee05-4de0-a626-4fa8555a9183");
		}

	}
}

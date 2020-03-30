package DictionaryListPaths;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DictionaryList implements Serializable {

	private double fileSize;
	private String fileName;
	private String filePath;

	public DictionaryList(File f) {

		fileSize = ((double) f.length() / (1024 * 1024));
		fileName = f.getName();
		filePath = f.getPath();
	}

	/**
	 * @return the fileSize
	 */
	public double getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(double fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String toString() {
		return this.getFileName() + "     " + this.getFilePath() + "     Size: " + this.fileSize + "mb";

	}

}

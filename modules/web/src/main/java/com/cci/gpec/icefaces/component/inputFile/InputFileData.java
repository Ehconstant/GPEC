package com.cci.gpec.icefaces.component.inputFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.cci.gpec.web.backingBean.habilitation.SalarieHabilitationsFormBB;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.context.ByteArrayResource;

/**
 * <p>
 * The InputFileData Class is a simple wrapper/storage for object that are
 * returned by the inputFile component. The FileInfo Class contains file
 * attributes that are associated during the file upload process. The File
 * Object is a standard java.io File object which contains the uploaded file
 * data.
 * </p>
 * 
 * @since 1.0
 */
public class InputFileData {

	// file info attributes
	private FileInfo fileInfo;
	// file that was uplaoded
	private File file;
	private String accessPath;

	private ByteArrayResource byteArrayResource;

	private String path;

	private boolean fileError;

	public InputFileData() {
		this.fileInfo = null;
		this.file = null;
		this.byteArrayResource = new ByteArrayResource(null);
		this.path = "";
		this.accessPath = "";
	}

	/**
	 * Create a new InputFileDat object.
	 * 
	 * @param fileInfo
	 *            fileInfo object created by the inputFile component for a given
	 *            File object.
	 */
	public InputFileData(FileInfo fileInfo, String url) throws IOException {
		this.fileInfo = fileInfo;
		this.file = new File(fileInfo.getPhysicalPath());
		this.fileError = false;
		this.byteArrayResource = new ByteArrayResource(null);
		this.path = fileInfo.getPhysicalPath();
		this.accessPath = url + fileInfo.getFileName();

	}

	public FileInfo getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Method to return the file size as a formatted string For example, 4000
	 * bytes would be returned as 4kb
	 * 
	 * @return formatted file size
	 */
	public String getSizeFormatted() {
		long ourLength = file.length();

		// Generate formatted label, such as 4kb, instead of just a plain number
		if (ourLength >= SalarieHabilitationsFormBB.MEGABYTE_LENGTH_BYTES) {
			return ourLength / SalarieHabilitationsFormBB.MEGABYTE_LENGTH_BYTES
					+ " MB";
		} else if (ourLength >= SalarieHabilitationsFormBB.KILOBYTE_LENGTH_BYTES) {
			return ourLength / SalarieHabilitationsFormBB.KILOBYTE_LENGTH_BYTES
					+ " KB";
		} else if (ourLength == 0) {
			return "0";
		} else if (ourLength < SalarieHabilitationsFormBB.KILOBYTE_LENGTH_BYTES) {
			return ourLength + " B";
		}

		return Long.toString(ourLength);
	}

	public ByteArrayResource getByteArrayResource() throws IOException {

		BufferedInputStream from = new BufferedInputStream(new FileInputStream(
				file));

		byte[] byteArray = toByteArray(from);

		byteArrayResource = new ByteArrayResource(byteArray);

		return byteArrayResource;
	}

	public static byte[] toByteArray(BufferedInputStream input)
			throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buf = new byte[4096];
		int len = 0;
		while ((len = input.read(buf)) > -1)
			output.write(buf, 0, len);
		return output.toByteArray();
	}

	public void setByteArrayResource(ByteArrayResource byteArrayResource) {
		this.byteArrayResource = byteArrayResource;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAccessPath() {
		return accessPath;
	}

	public void setAccessPath(String accessPath) {
		this.accessPath = accessPath;
	}

	public boolean isFileError() {
		if (this.file != null) {
			if (this.file.exists() && this.file.isFile() && this.file.canRead())
				return false;
			else
				return true;
		} else
			return false;
	}

}

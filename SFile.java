package skyline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SFile {

	private BufferedReader reader;
	private BufferedWriter writer;

	public BufferedReader getReader() {
		return reader;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	public BufferedWriter getWriter() {
		return writer;
	}

	public void setWriter(BufferedWriter writer) {
		this.writer = writer;
	}

	public void openRFile(String sFile) {
		try {
			reader = new BufferedReader(new FileReader(sFile));
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}

	public void openWFile(String sFile) {
		try {
			writer = new BufferedWriter(new FileWriter(sFile));
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}

	public String readLine() {
		String sLine = null;

		try {
			if ((sLine = reader.readLine()) != null)
				return sLine;
			else
				return null;
		} catch (IOException ioex) {
			ioex.printStackTrace();
			return null;
		}
	}

	public void writeLine(String sLine) {
		try {
			writer.write(sLine);
			writer.newLine();
			writer.flush();
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}

	public void closeRFile() {
		try {
			reader.close();
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}

	public void closeWFile() {
		try {
			writer.close();
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}
}

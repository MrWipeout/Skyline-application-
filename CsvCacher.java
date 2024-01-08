package skyline;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CsvCacher {
	private String inputFile;
	public SFile file_dataset = new SFile();
	private Reader in;
	Iterable<CSVRecord> records;

	public CsvCacher(String inputFile) {
		this.inputFile = inputFile;
	}

	public void startProcess() {
		file_dataset.openRFile(inputFile);

		in = file_dataset.getReader();
		
		CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build();
		try {
			records = csvFormat.parse(in);
		} catch (IOException e) {
			e.printStackTrace();
		};
	}

	public Entry nextEntry(ArrayList<Field> usefulFields) {
		while (true) {
			if (!records.iterator().hasNext()) {
				return null;
			} else {
				Entry newEntry = Entry.isolateColumns(records.iterator().next(), usefulFields);
				if (newEntry.hasNKeys(usefulFields.size())) {
					return newEntry;
				}
			}
		}
	}

	public void endProcess() {
		file_dataset.closeRFile();
	}
}

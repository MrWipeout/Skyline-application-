package skyline;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class CSVParser extends TimeCounter {

	private String inputFilePath, outputFilePath;
	private SFile file_dataset = new SFile();
	private int fields_count = 0;
	private ArrayList<Field> fields;
	public Configuration config;

	public CSVParser(String inputFilePath) {

		start("CSVDataParser");

		this.inputFilePath = inputFilePath;
		outputFilePath = new File(inputFilePath).getParent() + "/parsed_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv";

		stop("CSVDataParser");
	}

	public String getOutputFilePath() {
		return outputFilePath;
	}

	public void startProcess() throws IOException {
		start("StartProcess");

		file_dataset.openRFile(inputFilePath);
		file_dataset.openWFile(outputFilePath);

		Reader in = file_dataset.getReader();
		Writer out = file_dataset.getWriter();

		CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build();
		org.apache.commons.csv.CSVParser csvParser = csvFormat.parse(in);
		Iterable<CSVRecord> records = csvParser;

		List<String> fieldsNames = csvParser.getHeaderNames();
		config = new Configuration(fieldsNames);
		if (config.noFields()) {
			System.exit(0);
		}

		int id = 0;
		fields = config.getUsefulFields();
		fields_count = fields.size();
		CSVPrinter csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT.builder().setHeader(config.getUsefulFieldsNames().stream().toArray(String[]::new)).build());

		for (CSVRecord record : records) {
			Entry newEntry = Entry.isolateColumns(record, fields, id);
			if (newEntry.hasNKeys(fields_count)) {
				csvPrinter.printRecord(newEntry.values.values());
				id++;
			}

			if (record.getRecordNumber() % 10000 == 0)
				System.out.println(id);
		}

		csvPrinter.flush();
		csvPrinter.close();
		file_dataset.closeRFile();
		file_dataset.closeWFile();
		stop("StartProcess");
	}
	
	public Configuration getConfig() {
		return config;
	}
}

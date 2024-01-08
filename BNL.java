package skyline;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class BNL {
	public Configuration config;
	private String resultPath;
	private ArrayList<Entry> resultSkyline;

	public BNL(Configuration config, String inputFile) throws ClassNotFoundException, IOException {

		Long start = System.currentTimeMillis();

		this.config = config;

		String newCache = new File(inputFile).getParent() + "/newCache_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv";
		String tempPath = new File(inputFile).getParent() + "/tempPath_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv";
		resultPath = new File(inputFile).getParent() + "/result_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv";

		CsvCacher cache = new CsvCacher(inputFile);
		int TempFileEntries = 0, CacheEntriesCount = 0, windowMaxEntries = 100000;
		Boolean mainFinished = false, switcher = true, isDominated = false, writeTempOnly = false;
		resultSkyline = new ArrayList<>();
		ArrayList<Entry> window = new ArrayList<Entry>();
		Entry newEntry;

		ObjectInputStream input = null;
		ObjectOutputStream output = newOutputFrom(tempPath);

		while (true) {
			if (mainFinished) {
				resultSkyline.addAll(window);
				if (TempFileEntries == 0) {
					break;
				} else {
					writeTempOnly = false;
					CacheEntriesCount = TempFileEntries;
					TempFileEntries = 0;
					input = newInputFrom(switcher ? tempPath : newCache);
					output = newOutputFrom(switcher ? newCache : tempPath);
					window = new ArrayList<Entry>();
				}
			} else {
				cache.startProcess();
			}

			for (int i = 0; true; i++) {
				isDominated = false;
				if (i >= CacheEntriesCount && mainFinished)
					break;
				if (mainFinished) {
					newEntry = (Entry) input.readObject();
				} else {
					newEntry = cache.nextEntry(config.getUsefulFields());
				}

				if (newEntry == null) {
					cache.endProcess();
					output.flush();
					output.close();
					mainFinished = true;
					break;
				}

				if (window.size() == 0) {
					if (writeTempOnly == false) {
						window.add(newEntry);
						continue;
					}
				}

				for (Iterator<Entry> iterator = window.iterator(); iterator.hasNext();) {
					Entry eleEntry = iterator.next();
					if (isDominatedBy(newEntry, eleEntry)) {
						isDominated = true;
						break;
					}
					if (isDominatedBy(eleEntry, newEntry))
						iterator.remove();
				}

				if (isDominated == false) {
					if (writeTempOnly == true) {
						output.writeObject(newEntry);
						TempFileEntries++;
					} else {
						if (window.size() > windowMaxEntries) {
							output.writeObject(newEntry);
							TempFileEntries++;
							writeTempOnly = true;
						} else {
							window.add(newEntry);
						}
					}
				}
			}
			if (CacheEntriesCount != 0) {
				switcher = !switcher;
				input.close();
				output.flush();
				output.close();
			}
		}

		exportResults();
		System.out.println(resultSkyline);

		long elapsedTimeMillis = System.currentTimeMillis() - start;
		System.out.printf("\nElapsed Time is %d ms,  Space used is %f GB", elapsedTimeMillis,
				(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024.0 * 1024.0 * 1024.0));

	}
	
	public void exportResults() {
		SFile file_dataset = new SFile();
		file_dataset.openWFile(resultPath);
		Writer out = file_dataset.getWriter();
		try {
			CSVPrinter csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT.builder().setHeader(config.getUsefulFieldsNames().stream().toArray(String[]::new)).build());
			for (Entry entry : resultSkyline)
				csvPrinter.printRecord(entry.values.values());
			csvPrinter.flush();
			csvPrinter.close();
			file_dataset.closeWFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Entry> getResults() {
		return resultSkyline;
	}

	public Boolean isDominatedBy(Entry a, Entry b) {
		
		return a.isDominatedBy(b, config.getCalculatableFields());
	}

	public static ObjectOutputStream newOutputFrom(String filePath) {
		try {
			File out = new File(filePath);
			out.delete();
			FileOutputStream f = new FileOutputStream(out);
			return new ObjectOutputStream(f);
		} catch (FileNotFoundException e) {
			System.out.println("DOESNT EXIST");
		} catch (IOException e) {
			System.out.println("CANT BE WRITTEN");
			e.getCause();
			e.printStackTrace();
		}
		return null;
	}

	public static ObjectInputStream newInputFrom(String filepath) {
		try {
			File newInputFile = new File(filepath);
			FileInputStream fi = new FileInputStream(newInputFile);
			return new ObjectInputStream(fi);
		} catch (FileNotFoundException e) {
			System.out.println("DOESNT EXIST");
		} catch (IOException e) {
			System.out.println("CANT BE WRITTEN");
			e.getCause();
			e.printStackTrace();
		}
		return null;
	}
}
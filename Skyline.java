package skyline;

import java.io.IOException;

import javax.swing.JFileChooser;

public class Skyline {

	public static void main(String[] args) {

		JFileChooser fc = new JFileChooser();
		int i = fc.showOpenDialog(null);
		String file;

		while (i != JFileChooser.APPROVE_OPTION) {
			System.exit(0);
		}
		file = fc.getSelectedFile().toString();

		CSVParser parser = new CSVParser(file);
		try {
			parser.startProcess();
		} catch (IOException e) {
			e.printStackTrace();
		}
		parser.printResults();

		BNL bnl = null;
		try {
			bnl = new BNL(parser.config, parser.getOutputFilePath());
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		if(parser.config.getDisplayMap() && bnl != null)
			new Map(bnl.getResults(), parser.config.getUserLat(),
					parser.config.getUserLon(), parser.config.getRadius()).setVisible(true);
	}
}

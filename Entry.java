package skyline;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.commons.csv.CSVRecord;

public class Entry {
	public LinkedHashMap<String, Object> values;

	public Entry() {
		this.values = new LinkedHashMap<>();
	}

	public static Entry isolateColumns(CSVRecord record, ArrayList<Field> fields) {
		
		Entry newEntry = new Entry();

		for (Field tempField : fields) {
			String singleValue = record.get(tempField.getName());
			if (!singleValue.isBlank())
				newEntry.values.put(tempField.getName(), singleValue);
		}

		return newEntry;
	}

	public static Entry isolateColumns(CSVRecord record, ArrayList<Field> fields, int newId) {
		
		Entry newEntry = new Entry();

		for (Field tempField : fields) {
			if(tempField.getName() == "skyline_id") {
				newEntry.values.put("skyline_id", newId);
			} else {
				String singleValue = record.get(tempField.getName());
				if (!singleValue.isBlank())
					newEntry.values.put(tempField.getName(), singleValue);
			}
		}

		return newEntry;
	}

	public boolean hasNKeys(int fields_count) {
		return fields_count == values.size();
	}
	
	public Object get(String key) {
		return values.get(key);
	}

	public boolean isDominatedBy(Entry other, ArrayList<Field> calculatableFields) {
		int totalDomination = 0;

		for ( Field field : calculatableFields) {
			int fieldDomination = this.isDominatedInFieldBy(other, field);
			if (fieldDomination < 0) {
				return false;
			} else {
				totalDomination += fieldDomination;
			}
		}
		return totalDomination > 0 ? true : false;
	}

	private int isDominatedInFieldBy(Entry other, Field field) {
		Long thisValue = Long.parseLong((String) this.get(field.name));
		Long otherValue = Long.parseLong((String) other.get(field.name));

		if (field.ascending) {
			if (otherValue > thisValue)
				return 1;
			else if (otherValue == thisValue)
				return 0;
		} else {
			if (otherValue < thisValue)
				return 1;
			else if (otherValue == thisValue)
				return 0;
		}
		return -1;
	}
	
	public String toString() {
		return values.toString();
	}
}

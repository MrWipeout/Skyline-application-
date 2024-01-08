package skyline;

public class Field {
	protected String name;
	protected boolean displayed;
	protected boolean calculatable;
	protected boolean ascending;
	protected int position;
	protected boolean lat;
	protected boolean lon;

	public String getName() {
		return name;
	}

	public boolean isDisplayed() {
		return displayed;
	}

	public boolean isCalculatable() {
		return calculatable;
	}

	public boolean isAscending() {
		return ascending;
	}

	public boolean isLat() {
		return lat;
	}

	public boolean isLon() {
		return lon;
	}

	public Field() {}

	public Field(boolean id) {
		super();
		if (id == true) {
			this.name = "skyline_id";
			this.displayed = true;
			this.calculatable = false;
			this.ascending = false;
			this.lat = false;
			this.lon = false;
		}
	}

	public Field(String name, boolean displayed, boolean calculable, boolean ascending, boolean lat, boolean lon) {
		super();
		this.name = name;
		this.displayed = displayed;
		this.calculatable = calculable;
		this.ascending = ascending;
		this.lat = lat;
		this.lon = lon;
	}

	public String toString() {
		return name;
	}
}

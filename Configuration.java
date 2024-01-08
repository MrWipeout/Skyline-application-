package skyline;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class Configuration extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	protected ArrayList<Field> fields;
	private boolean displayMap;
	private double userLat;
	private double userLon;
	private double radius;

	/**
	 * Create the frame.
	 */
	public Configuration(List<String> fields_labels) {
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 650, 500);
		contentPane = new JPanel();
		setContentPane(contentPane);
		displayMap = false;

		fields = new ArrayList<Field>();

		JPanel headers_panel = new JPanel();
		headers_panel.setLayout(new GridLayout(0, 6, 0, 0));
		headers_panel.setPreferredSize(new Dimension(600, 20));
		headers_panel.add(new JLabel("Field name"));
		JLabel temp_label = new JLabel("Display");
		temp_label.setHorizontalAlignment(SwingConstants.CENTER);
		headers_panel.add(temp_label);
		temp_label = new JLabel("Calculate");
		temp_label.setHorizontalAlignment(SwingConstants.CENTER);
		headers_panel.add(temp_label);
		temp_label = new JLabel("Ascending");
		temp_label.setToolTipText(
				"If checked, the algorithm takes lower values as dominant ones. By default, higher values are dominant.");
		temp_label.setHorizontalAlignment(SwingConstants.CENTER);
		headers_panel.add(temp_label);
		temp_label = new JLabel("Latitude");
		temp_label.setHorizontalAlignment(SwingConstants.CENTER);
		headers_panel.add(temp_label);
		temp_label = new JLabel("Longitude");
		temp_label.setHorizontalAlignment(SwingConstants.CENTER);
		headers_panel.add(temp_label);
		contentPane.add(headers_panel);

		JPanel fields_panel = new JPanel();
		ArrayList<JLabel> labels = new ArrayList<JLabel>();
		ArrayList<JCheckBox> display = new ArrayList<JCheckBox>();
		ArrayList<JCheckBox> calculate = new ArrayList<JCheckBox>();
		ArrayList<JCheckBox> descending = new ArrayList<JCheckBox>();
		ArrayList<JRadioButton> latitude = new ArrayList<JRadioButton>();
		ArrayList<JRadioButton> longitude = new ArrayList<JRadioButton>();
		fields_panel.setLayout(new GridLayout(0, 6, 0, 0));

		ButtonGroup latRadioGroup = new ButtonGroup();
		ButtonGroup lonRadioGroup = new ButtonGroup();

		for (String field : fields_labels) {
			temp_label = new JLabel(field);
			labels.add(temp_label);
			fields_panel.add(temp_label);

			JCheckBox temp_display = new JCheckBox();
			temp_display.setHorizontalAlignment(SwingConstants.CENTER);
			display.add(temp_display);
			fields_panel.add(temp_display);

			JCheckBox temp_calculate = new JCheckBox();
			temp_calculate.setHorizontalAlignment(SwingConstants.CENTER);
			calculate.add(temp_calculate);
			fields_panel.add(temp_calculate);

			JCheckBox temp_ascending = new JCheckBox();
			temp_ascending.setHorizontalAlignment(SwingConstants.CENTER);
			descending.add(temp_ascending);
			fields_panel.add(temp_ascending);

			JRadioButton temp_latitude = new JRadioButton();
			temp_latitude.setHorizontalAlignment(SwingConstants.CENTER);
			latitude.add(temp_latitude);
			latRadioGroup.add(temp_latitude);
			fields_panel.add(temp_latitude);

			JRadioButton temp_longitude = new JRadioButton();
			temp_longitude.setHorizontalAlignment(SwingConstants.CENTER);
			longitude.add(temp_longitude);
			lonRadioGroup.add(temp_longitude);
			fields_panel.add(temp_longitude);
		}
		JScrollPane fields_scrollable_panel = new JScrollPane(fields_panel);
		fields_scrollable_panel.setPreferredSize(new Dimension(600, 400));
		contentPane.add(fields_scrollable_panel);

		JButton btnCalculate = new JButton("Calculate");
		btnCalculate.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (latRadioGroup.getSelection() != null
						&& lonRadioGroup.getSelection() != null)
					setWantedRadius();

				fields.add(new Field(true));
				for (int i = 0; i < labels.size(); i++) {
					fields.add(new Field(labels.get(i).getText(), display.get(i).isSelected(),
							calculate.get(i).isSelected(), descending.get(i).isSelected(),
							latitude.get(i).isSelected(), longitude.get(i).isSelected()));
				}
				dispose();
			}
		});

		contentPane.add(btnCalculate);
		setLocationRelativeTo(null);
		setModal(true);
		setVisible(true);
	}
	
	private void setWantedRadius() {
		displayMap = true;
		userLat = 200;
		userLon = 200;
		radius = 200;
		while(userLat == 200)
			userLat = Double.parseDouble(JOptionPane.showInputDialog("What's your latitude?"));
		while(userLon == 200)
			userLon = Double.parseDouble(JOptionPane.showInputDialog("What's your longitude?"));
		while(radius == 200)
			radius = Integer.parseInt(JOptionPane.showInputDialog("What's the wanted radius in km?")) / 110.574;
	}

	public double getUserLat() {
		return userLat;
	}

	public double getUserLon() {
		return userLon;
	}

	public double getRadius() {
		return radius;
	}

	public boolean getDisplayMap() {
		return displayMap;
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	public ArrayList<Field> getUsefulFields() {
		ArrayList<Field> usefulFields = new ArrayList<>();
		for(Field field : this.fields) {
			if (field.isCalculatable() || field.isDisplayed())
				usefulFields.add(field);
		}
		return usefulFields;
	}

	public ArrayList<String> getFieldsNames() {
		ArrayList<String> fieldsNames = new ArrayList<>();
		for(Field field : fields) {
			fieldsNames.add(field.getName());
		}
		return fieldsNames;
	}

	public ArrayList<String> getUsefulFieldsNames() {
		ArrayList<String> fieldsNames = new ArrayList<>();
		for(Field field : fields) {
			if (field.isCalculatable() || field.isDisplayed())
				fieldsNames.add(field.getName());
		}
		return fieldsNames;
	}

	public ArrayList<Field> getCalculatableFields() {
		ArrayList<Field> calculatableFields = new ArrayList<>();
		for(Field field : this.fields) {
			if (field.isCalculatable())
				calculatableFields.add(field);
		}
		return calculatableFields;
	}

	public boolean noFields() {
		return fields.isEmpty();
	}

}

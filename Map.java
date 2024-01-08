package skyline;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.*;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.*;

public class Map extends JFrame implements JMapViewerEventListener {

    private static final long serialVersionUID = 1L;

    private final JMapViewerTree treeMap;

    public Map(ArrayList<Entry> entries, double userLat, double userLon, double radius) {
        super("Skyline Map");
        setSize(700, 700);

        treeMap = new JMapViewerTree("");

        // Listen to the map viewer for user operations so components will
        // receive events and update
        map().addJMVListener(this);

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelBottom = new JPanel();
        JPanel helpPanel = new JPanel();

        add(panel, BorderLayout.NORTH);
        add(helpPanel, BorderLayout.SOUTH);
        panel.add(panelBottom, BorderLayout.SOUTH);
        JLabel helpLabel = new JLabel("Use right mouse button to move,\n "
                + "left double click or mouse wheel to zoom.");
        helpPanel.add(helpLabel);

        add(treeMap, BorderLayout.CENTER);
        
        // User position and radius
        MapMarkerCircle wantedRadius = new MapMarkerCircle(userLat, userLon, radius);
        Style wantedRadiusStyle = new Style();
        wantedRadiusStyle.setBackColor(new Color(1f,1f,1f,0.3f));
        wantedRadiusStyle.setColor(Color.black);
        wantedRadius.setStyle(wantedRadiusStyle);
        map().addMapMarker(wantedRadius);

        Style mapMarkerStyle = new Style();
        mapMarkerStyle.setBackColor(new Color(1f,1f,1f,0.7f));
        
        // Markers
        for(Entry entry : entries) {
    		MapMarkerDot tempMD = new MapMarkerDot(
    				"" + entry.values.get("id") + entry.values.get("manufacturer")
    					+ entry.values.get("model") + entry.values.get("year"),
    				new Coordinate(Double.parseDouble((String) entry.values.get("lat")),
    						Double.parseDouble((String) entry.values.get("long")))
    		);
			tempMD.setStyle(wantedRadiusStyle);
            map().addMapMarker(tempMD);
        }

        map().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    map().getAttribution().handleAttribution(e.getPoint(), true);
                }
            }
        });

        map().addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                boolean cursorHand = map().getAttribution().handleAttributionCursor(p);
                if (cursorHand) {
                    map().setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    map().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
//                if (showToolTip.isSelected()) map().setToolTipText(map().getPosition(p).toString());
            }
        });
        
        map().setDisplayToFitMapMarkers();
    }

    private JMapViewer map() {
        return treeMap.getViewer();
    }

    @Override
    public void processCommand(JMVCommandEvent command) {
        if (command.getCommand().equals(JMVCommandEvent.COMMAND.ZOOM) ||
        		command.getCommand().equals(JMVCommandEvent.COMMAND.MOVE)) {
        }
    }
}

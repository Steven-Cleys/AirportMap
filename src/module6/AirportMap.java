package module6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PImage;


/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airportList;
	private List<Marker> routeList;
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	private static final long serialVersionUID = 1L; //shutup eclipse
	HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
	MarkerManager<Marker> markermanager;
	List<ShapeFeature> routes;
	
	
	public void setup() {
		// setting up PAppler
		size(800,600, OPENGL);
		
		// setting up map and default events
		map = new UnfoldingMap(this, 50, 50, 750, 550,  new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		markermanager = map.getDefaultMarkerManager();
		
		PImage img = loadImage("airportMarkerIcon.png");
		
		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		routes = ParseFeed.parseRoutes(this, "routes.dat");
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();

		
		// create markers from features
		for (PointFeature feature : features) {
			for (ShapeFeature shape : routes) {

				 
				if (feature.getStringProperty("code").equals(
						shape.getStringProperty("code"))) {
					AirportMarker m = new AirportMarker(feature,img);

					m.setRadius(8);
					airportList.add(m);
					//System.out.println(m.getLocation() + " " + feature.getLocation());
					// put airport in hashmap with OpenFlights unique id for key
					airports.put(Integer.parseInt(feature.getId()),
							feature.getLocation());
				}
			}
		}
		
		
		// parse route data
//		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
//		routeList = new ArrayList<Marker>();
//		for(ShapeFeature route : routes) {
//			
//			// get source and destination airportIds
//			int source = Integer.parseInt((String)route.getProperty("source"));
//			int dest = Integer.parseInt((String)route.getProperty("destination"));
//			
//			// get locations for airports on route
//			if(airports.containsKey(source) && airports.containsKey(dest)) {
//				route.addLocation(airports.get(source));
//				route.addLocation(airports.get(dest));
//			}
//			
//			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
//		
//			System.out.println(sl.getProperties());
//			
//			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
//			routeList.add(sl);
//		}
		
		
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		//map.addMarkers(routeList);
		
		markermanager.addMarkers(airportList);
		
	}
	
	public void draw() {
		background(0);
		map.draw();
		
		
	}
	
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(airportList);
		//selectMarkerIfHover(cityMarkers);
		//loop();
	}
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				
				marker.setSelected(true);
				return;
			}
		}
	}
	
	@Override
	public void mouseClicked()
	{
		if (lastClicked != null) {
			unhideMarkers();
			removeRoutes();
			lastClicked = null;
		}
			checkRoutes();
	}
	
	public void checkRoutes() {
		if (lastClicked != null) return;
		// Loop over the airport markers to see if one of them is selected
		for (Marker m : airportList) {
			AirportMarker marker = (AirportMarker)m;
			if (marker.isInside(map, mouseX, mouseY)) {
				lastClicked = marker;
				
				drawRoutes(marker.getCode());
				return;
			}
		}
	}
	
	public void drawRoutes(String code) {
		
		
		routeList = new ArrayList<Marker>();
		
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			String routeCode = (String)route.getProperty("code");
			//System.out.println(code + " " + routeCode);
			// get locations for airports on route
			if(code.equals(routeCode)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
				SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
				System.out.println("test " + sl.getLocations());
				routeList.add(sl);
			}
			else {
				//System.out.println("routes not found");
			}
		}
		
		hideMarkers();
		
		for (Marker m : airportList) { 
			for (Marker route : routeList) {
				Location loc = ((SimpleLinesMarker)route).getLocations().get(0);
				Location loc1 = ((SimpleLinesMarker)route).getLocations().get(1);
				Location loc2 = m.getLocation();	
				if ( loc1 == loc2 || loc2 == loc) {
					//System.out.println("check" +loc2);
					m.setHidden(false);
				}	
				
		}
		}
		markermanager.addMarkers(routeList);
		//map.addMarkers(routeList);
		
	}
	
	public void removeRoutes() {
		for (Marker m: routeList) {
			
			markermanager.removeMarker(m);
		}
	}
	
	private void unhideMarkers() {
		for(Marker marker : airportList) {
			marker.setHidden(false);
		}
	}
	
	private void hideMarkers() {
		for(Marker marker : airportList) {
			marker.setHidden(true);
		}
	}
}

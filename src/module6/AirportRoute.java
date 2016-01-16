package module6;

import java.util.HashMap;
import java.util.List;

import processing.core.PGraphics;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;
import de.fhpotsdam.unfolding.geo.Location;

public class AirportRoute extends SimpleLinesMarker {

	public AirportRoute(List<Location> locations,HashMap<String,Object> props) {
		super(locations,props);
	}
	
	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPos) {
		
		pg.pushStyle();
		
		for(MapPosition pos : mapPos) {
			System.out.println(pos);
		}
		
		pg.fill(200,100,50);
		
		
		pg.popStyle();
		
	}
}

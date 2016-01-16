package module6;

import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMarker extends CommonMarker {
	public static int MARKER_SIZE = 13;
	public static int TRI_SIZE = 5;
	public static List<SimpleLinesMarker> routes;
	private PImage img;
	
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		//System.out.println(city.getProperties());
	
	}
	
	public AirportMarker(Feature city, PImage img) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		this.img = img;
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		
		pg.pushStyle();
		
		pg.image(img,x-MARKER_SIZE/2,y-MARKER_SIZE/2,MARKER_SIZE,MARKER_SIZE);
//		pg.fill(11);	
//		pg.ellipse(x, y, 5, 5);
		
		pg.popStyle();
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		
		String name = getName();
		String pop = getCity() + " in " + getCountry();
		
		pg.pushStyle();
		
		pg.fill(255, 255, 255);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x, y-TRI_SIZE-39, Math.max(pg.textWidth(name), pg.textWidth(pop)) + 6, 39);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(name, x+3, y-TRI_SIZE-33);
		pg.text(pop, x+3, y -TRI_SIZE-18);
		
		pg.popStyle();
		 // show rectangle with title
		
		// show routes
		
		
	}
	
	public String getName()
	{
		return getStringProperty("name");
	}
	
	private String getCity()
	{
		return getStringProperty("city");
	}
	
	private String getCountry()
	{
		return getStringProperty("country");
	}
	
	public String getCode()
	{
		return getStringProperty("code");
	}
	


	
}

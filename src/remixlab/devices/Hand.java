/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Hand Class, this class store an ArrayList of points, with these points we calculate the gesture
 * Modification of the Hands example of the library SimpleOpenNI, see (http://code.google.com/p/simple-openni/)
 * */
package remixlab.devices;

import java.awt.Color;
import java.util.ArrayList;
import processing.core.PVector;

public class Hand {
	ArrayList<PVector> points;
	int maxPoints;
	Color color;
	
	public Hand(){
		points= new ArrayList<PVector>();
		maxPoints=10;
	}
	public Hand(int max,Color col){
		points= new ArrayList<PVector>();
		maxPoints=max;
		color=col;
	}
	/**
	 * Return the last point
	 * */
	public PVector getLast(){
		if(points.size()>0){
			return points.get(0);
		}else{
			return new PVector(0,0,0);
		}
		
	}
	/**
	 * Add a new point to the points list, if the quantity of points i greater than
	 * maxPoints, delete the first added point
	 * @param point: point to add in the ArrayList 
	 * */
	public void addPoint(PVector point){
		points.add(0,point);      
	    if(points.size()>maxPoints){
	    	points.remove(points.size()-1);
	    }
	}
	/**
	 * Reset the ArrayList of points
	 * */
	public void reset(){
		points.clear();
	}
}

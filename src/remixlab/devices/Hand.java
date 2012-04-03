/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Hand Class, this class store a point with the position of the hand
 * */
package remixlab.devices;

import java.awt.Color;
import processing.core.PVector;

public class Hand {
	PVector point;
	Color color;
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	public Hand(){
		point=new PVector();
	}
	public Hand(Color col){
		point=new PVector();
		color=col;
	}
	
	/////////////////////////////////////// GET AND SET ///////////////////////////////////////
	/**
	 * Return the point
	 * */
	public PVector getPoint(){
		return point;
	}
	/**
	 * Set the point
	 * @param point:
	 * */
	public void setPoint(PVector pointIn){
		point=pointIn;
	}
}

/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * GestureDetector Class, this class detect if the user do any gesture supported.
 * First the user must record the standard position of the hands, then can move
 * to generate gestures. By now get an array of hands, but only use two of them.
 * */
package remixlab.devices;

public class GestureDetector {
	
	Gesture base;		//Base gesture to compare input gestures
	
	
	public GestureDetector(){
		base=new Gesture();
	}
	
	
	/**
	 * Set the base gesture, the gesture could be with two hands at same
	 * high and in front of the sensor
	 * */
	public void setBase(Gesture baseGesture){
		base=baseGesture;
	}
	
	
	/**
	 * Return a PVector defined by the relative position of the hands in the input Gesture and the base
	 * 	1. Define a plane using both hands
	 * 	2. Calculate the distance to 
	 * */
	
}

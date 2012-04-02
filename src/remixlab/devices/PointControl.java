/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Modification of the Hands example of the library SimpleOpenNI, see (http://code.google.com/p/simple-openni/)
 * */
package remixlab.devices;

import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.XnVHandPointContext;
import SimpleOpenNI.XnVPointControl;
class PointControl extends XnVPointControl{
	Kinect kinect;
	/**
	 * PointControl constructor
	 * @param k: parent object where the callbacks return data from the sensor
	 * */
	public PointControl(Kinect k){
		kinect=k;
	}
	/**
	 * Hands callbacks
	 * */
	public void OnPointCreate(XnVHandPointContext cxt){
		PApplet.println("OnPointCreate, handId: " + cxt.getNID());
		PVector vector = new PVector(cxt.getPtPosition().getX(), cxt.getPtPosition().getY(), cxt.getPtPosition().getZ());
		kinect.setHands(cxt.getNID(),vector);
	}
	public void OnPointUpdate(XnVHandPointContext cxt){
		PVector vector = new PVector(cxt.getPtPosition().getX(), cxt.getPtPosition().getY(), cxt.getPtPosition().getZ());
		kinect.setHands(cxt.getNID(),vector);
	}
	public void OnPointDestroy(long nID){
		PApplet.println("OnPointDestroy, handId: " + nID);
	}
}

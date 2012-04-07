/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Modification of the Hands example of the library SimpleOpenNI, see (http://code.google.com/p/simple-openni/)
 * Return the data of callbacks functions of the Kinect
 * */
package remixlab.devices;

import processing.core.PVector;
import SimpleOpenNI.XnVHandPointContext;
import SimpleOpenNI.XnVPointControl;
class PointControl extends XnVPointControl{
	Kinect kinect;
	int cantHands;
	PVector[] hands;
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	/**
	 * PointControl constructor
	 * @param k: parent object where the callbacks return data from the sensor
	 * */
	public PointControl(Kinect k){
		kinect=k;
		cantHands=0;
		hands=new PVector[2];
		hands[0]=new PVector();
		hands[1]=new PVector();
	}
	/////////////////////////////////////// GET AND SET ///////////////////////////////////////
	/**
	 * Get the quantity of hands registered 
	 * */
	public int getHands(){
		return cantHands;
	}
	/////////////////////////////////////// CALLBAKS ///////////////////////////////////////
	public void OnPointCreate(XnVHandPointContext cxt){
		System.out.println("OnPointCreate, handId: " + cxt.getNID());
		PVector vector = new PVector(cxt.getPtPosition().getX(), cxt.getPtPosition().getY(), cxt.getPtPosition().getZ());
		kinect.setStarting(vector);
		cantHands++;
	}
	public void OnPointUpdate(XnVHandPointContext cxt){
		PVector vector = new PVector(cxt.getPtPosition().getX(), cxt.getPtPosition().getY(), cxt.getPtPosition().getZ());
		// Assign the point to the hand using module
		hands[(int) (cxt.getNID() % 2)]=vector;
	}
	public void OnPointDestroy(long nID){
		System.out.println("OnPointDestroy, handId: " + nID);
		cantHands--;
	}
}

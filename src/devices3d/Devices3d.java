/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Example of use: draw a 3D scene and manipulate with the Sensor 
 * */

package devices3d;

import java.awt.Color;
import processing.core.PApplet;
import processing.core.PVector;
import remixlab.devices.*;
import remixlab.proscene.*;

/**
 * TODO: Define the movement to rotate in x axis 
 * TODO: Build the tunnel 
 * TODO: Constrain the movement to the tunnel 
 * TODO: Start the movement in the start of the tunnel
 * TODO: Only move forward 
 * TODO: Measure:
 *  - time
 *  - start point
 *  - end point 
 *  - curves in the bezier tunnel 
 *  - deviation of the original path 
 *  - collisions 
 *  - stop times 
 *  - lost sensing hands - 
 * TODO: Calculate:
 *  - Quantitative Throughput 
 *  -
 * TODO:
 *  - Compare Kinect aerial view
 *  - Mouse and keyboard interaction
 *  - Wiimote interaction
 *  - SpaceNavigator integration
 * */

@SuppressWarnings("serial")
public class Devices3d extends PApplet {
	//Enum of devices
	public enum Device {KINECT,WIIMOTE,MOUSE,SPACENAVIGATOR}
	
	Kinect kinect; 	// Kinect device class
	Wiimote wiimote;// Wiimote device class
	Scene scene; 	// 3D Scene
	Avatar avatar; 	// Sight of the camera. Avatar is used to control the camera
	Device device;	// Current Device 
	
	PVector left; 	// Vector with screen position of the Left Hand returned by the device
	PVector right; 	// Vector with screen position of the Right Hand returned by the device
	PVector trans; 	// Vector with translation values returned by the device
	PVector rotat; 	// Vector with rotation values returned by the device
	
	Marker target;
	int cantMarkers;
	Marker[] markers;
	
	public void setup() {
		size(1910,800, P3D);
		scene = new Scene(this);
		
		//Initialize the vectors
		left=right=trans=rotat= new PVector(0, 0, 0);
		
		//Configure the avatar
		avatar=new Avatar(this,scene,new PVector(50,100,10),new Quaternion(new PVector(0, 0, 1), new PVector(0,10,0)));
		
		//Define the current device
		device=Device.KINECT;
		switch (device){
			case KINECT:
				kinect = new Kinect(this,scene);
				scene.addDevice(kinect.getDevice());
				scene.disableMouseHandling();
				break;
			case WIIMOTE:
				wiimote=new Wiimote(this,scene);
				scene.addDevice(wiimote.getDevice());
				scene.disableMouseHandling();
				avatar.drawHands(false);
				break;
			default : 
				println("device: "+device);
		}
				

		//Create the markers and the target
		PVector direction=new PVector(0,0,1);
		Quaternion orientation=new Quaternion(new PVector(0, 0, 1), direction);
		Color color=new Color(255,0,0);
		target=new Marker(this,scene,new PVector(0,-200,0),orientation,50,color);
		
		cantMarkers=10;
		markers=new Marker[cantMarkers];
		float min=-200,max=200;
		for(int i=0;i<cantMarkers;i++){
			PVector directionM=new PVector(random(min,max),random(min,max),random(min,max));
			Quaternion orientationM=new Quaternion(new PVector(0, 0, 1), directionM);
			Color colorM=new Color((int)random(255),(int)random(255),(int)random(255));
			markers[i]=new Marker(this,scene,new PVector(random(min,max),random(min,max),random(min,max)),orientationM,10,colorM);
		}
		smooth();
	}

	public void draw() {
		background(0);
		//Draw the markers and the target
		target.draw();
		for(int i=0;i<cantMarkers;i++){
			markers[i].draw();
		}
		//Execute the draw an load data from the device selected
		switch (device){
			case KINECT:
				kinect.draw();
				loadKinectData();
				break;
			case WIIMOTE:
				wiimote.draw();
				loadWiimoteData();
				break;
			default : 
				
		}
		// Draw the graphics elements of the Avatar
		avatar.draw(trans,rotat,left,right);
	}
	
	/////////////////////////////////////// LOADERS ///////////////////////////////////////
	/**
	 * Load the Kinect data to display in the avatar
	 * */
	public void loadKinectData(){
		left = kinect.screenHand("left");
		right = kinect.screenHand("right");
		trans = kinect.translationVector();
		rotat = kinect.rotationVector();
	}
	/**
	 * Load the Wiimote data to display in the avatar
	 * */
	public void loadWiimoteData(){
		trans = wiimote.translationVector();
		rotat = wiimote.rotationVector();
	}
}

/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * SpaceNavigator Class, this class manage the SpaceNavigator control interaction
 * */
package remixlab.devices;

import processing.core.PApplet;
import processing.core.PVector;
import procontroll.*;
import remixlab.proscene.HIDevice;
import remixlab.proscene.Scene;

public class SpaceNavigator{
	PApplet parent; 				// Processing Object
	Scene scene;					// Scene proscene 
	HIDevice device; 				// Object to control the scene with the device
	PVector trans;					// The vector with translation values returned by the device
	PVector rotat;					// The vector with rotation values returned by the device
	
	//SpaceNavigator variables
	ControllIO controllIO;
	ControllDevice space;			// SpaceNavigator procontroll object	
	ControllSlider slider0;			// Slider 0 (Translation x)
	ControllSlider slider1;			// Slider 1 (Translation z)
	ControllSlider slider2;			// Slider 2 (Translation y)
	ControllSlider slider3;			// Slider 3 (Rotation x)
	ControllSlider slider4;			// Slider 4 (Rotation z)
	ControllSlider slider5;			// Slider 5 (Rotation y)
	ControllButton button0;			// button (Not used)
	ControllButton button1;			// button (Not used)
	
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	/**
	 * SpaceNavigator Constructor 
	 * @param p: PApplet parent
	 * */
	public SpaceNavigator(PApplet p,Scene s) {
		parent = p;
		scene=s;
		//Initialize the device to send data
		device = new HIDevice(scene);
		device.addHandler(this,"feed");
		device.setTranslationSensitivity(0.003f, 0.003f, 0.002f);
		device.setRotationSensitivity(0.00005f, 0.00005f, 0.00005f);
		//Initialize the controls of the device
		controllIO = ControllIO.getInstance(parent);
		space = controllIO.getDevice("3Dconnexion SpaceNavigator");
		//Load sliders
		slider0 = space.getSlider(0);
		slider1 = space.getSlider(1);
		slider2 = space.getSlider(2);
		slider3 = space.getSlider(3);
		slider4 = space.getSlider(4);
		slider5 = space.getSlider(5);
		//Load buttons
		button0=space.getButton(0);
		button1=space.getButton(1);
		//Initialize movements vectors
		trans=rotat=new PVector(0,0,0);
		//Defined to hide the mouse pointer
		parent.noCursor();
	}
	/////////////////////////////////////// GET AND SET ///////////////////////////////////////
	/**
	 * Return the Wiimote as the input device
	 * */
	public HIDevice getDevice(){
		return device;
	}
	/////////////////////////////////////// METHODS ///////////////////////////////////////
	/**
	 * 
	 * */
	public void draw() {
	}
	/**
	 * Feed the translations and rotations to the scene, gives the hand positions
	 * */
	public void feed(HIDevice d) {
		PVector transV = translationVector();
		PVector rotatV = rotationVector();
		d.feedTranslation(transV.x, transV.y, transV.z);
		d.feedRotation(rotatV.x, rotatV.y, rotatV.z);
	}
	/**
	 * Return the vector of translation
	 * */
	public PVector translationVector(){
		float multip=500;
		trans=new PVector(0,0,0);
		trans.x=slider0.getValue()*multip;
		trans.y=slider2.getValue()*multip;
		trans.z=-slider1.getValue()*multip;
		return trans;
	}
	/**
	 * Return the vector of rotations
	 * */
	public PVector rotationVector(){
		float multip=500;
		rotat=new PVector(0,0,0);
		rotat.x=slider3.getValue()*multip;
		rotat.y=slider5.getValue()*multip;
		rotat.z=-slider4.getValue()*multip;
		return rotat;
	}
}

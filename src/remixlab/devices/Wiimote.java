/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Wiimote Class, this class manage the Wii control interaction
 * */
package remixlab.devices;

import java.awt.AWTException;
import java.awt.Robot;

import processing.core.PApplet;
import processing.core.PVector;
import procontroll.*;
import remixlab.proscene.HIDevice;
import remixlab.proscene.Scene;

public class Wiimote{
	PApplet parent; 				// Processing Object
	Scene scene;					// Scene proscene 
	HIDevice device; 				// Object to control the scene with the device
	PVector trans;					// The vector with translation values returned by the device
	PVector rotat;					// The vector with rotation values returned by the device
	
	//Wii variables
	ControllIO controllIO;
	ControllDevice wii;				// Wiimote procontroll object
	ControllSlider acc;				// Accelerometer stick ()
	ControllStick pad;				// Pad stick (translation x and z axis)
	ControllStick ir;				// IR slider (rotation x and y axis)
	ControllButton aButton;			// Up/A button (translation along positive y)
	ControllButton bButton;			// Down/B button (translation along negative y)
	
	Robot robot;					//Object to block the mouse pointer
	
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	/**
	 * Wiimote Constructor 
	 * @param p: PApplet parent
	 * */
	public Wiimote(PApplet p,Scene s) {
		parent = p;
		scene=s;
		
		//Initialize the device to send data
		device = new HIDevice(scene);
		device.addHandler(this,"feed");
		device.setTranslationSensitivity(0.003f, 0.003f, 0.01f);
		device.setRotationSensitivity(0.0001f, 0.00005f, 0.00005f);
		
		//Initialize the controls of the device
		controllIO = ControllIO.getInstance(parent);
		wii = controllIO.getDevice("Nintendo Wiimote");
		//Load sticks
		acc = wii.getSlider(0);
		acc.setMultiplier(PApplet.PI);
		pad = wii.getStick(1);
		//Load buttons
		aButton=wii.getButton(0);
		bButton=wii.getButton(1);
		//Load Sliders
		ir=wii.getStick(0);
		
		//Initialize movements vectors
		trans=rotat=new PVector(0,0,0);
		
		//Defined by control the mouse pointer
		try {
			robot=new Robot();
		} catch (AWTException e) {}
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
		//Lock the mouse pointer
		robot.mouseMove(parent.width/2,parent.height/2);
		
//		PApplet.println("ACC: "+acc.getValue()+" :: IR: "+ir.getX()+","+ir.getY());
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
		trans=new PVector(0,0,0);
		trans.x=pad.getY()*500;
		trans.y=-pad.getX()*500;
		if(bButton.pressed())
			trans.z=bButton.getValue()*62;
		if(aButton.pressed())
			trans.z=-aButton.getValue()*62;
		return trans;
	}
	/**
	 * Return the vector of rotations
	 * */
	public PVector rotationVector(){
		float multip=160;
		rotat=new PVector(0,0,0);
//		//TODO: Define a control to y-rotation
		rotat.x=ir.getX()*multip;
		rotat.y=ir.getY()*multip;
		rotat.z=0;
		return rotat;
	}
}

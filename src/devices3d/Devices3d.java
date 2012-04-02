/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * */

package devices3d;


import processing.core.PApplet;
import processing.core.PVector;
import remixlab.devices.Hand;
import remixlab.devices.Kinect;
import remixlab.proscene.HIDevice;
import remixlab.proscene.Scene;

@SuppressWarnings("serial")
public class Devices3d extends PApplet {
	Kinect kinect;
	Scene scene;
	
	HIDevice dev;
	
	
	
	public void setup(){
		size(800, 600, P3D);
		scene = new Scene(this);
		
		kinect = new Kinect(this);
		//Resize the window from the depth map size
		//size(kinect.width(), kinect.height()); 
		
		
		// Define the RELATIVE mode HIDevice.
		  dev = new HIDevice(scene);
		  dev.addHandler(this, "feed");
		  dev.setTranslationSensitivity(0.01f, 0.01f, 0.01f);
		  dev.setRotationSensitivity(0.00001f, 0.00001f, 0.00001f);
		  dev.setCameraMode(HIDevice.CameraMode.GOOGLE_EARTH);
		  scene.addDevice(dev);
		
		
		
		smooth();
	}

	public void draw(){
		background(0,0,0);
		
		//Update the kinect data from the sensor
		kinect.update();
	}
	
	public void feed(HIDevice d) {
		if(kinect.getNumberHands()>0){
			Hand hand=kinect.getHand(1);
			PVector last=hand.getLast();
			println("Hand: "+last);
			  d.feedTranslation(last.x,last.y,last.z);
			  d.feedRotation(last.x,last.y,last.z);
		}
	}
	
	
	
	
	
	
	
}

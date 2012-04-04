/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Example of use: draw a 3D scene and manipulate with the Sensor 
 * */

package devices3d;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import remixlab.devices.Kinect;
import remixlab.proscene.*;

/**
 * TODO: Draw the hands in the screen TODO: Return Position from the Kinect
 * class TODO: Return Rotation from the Kinect class TODO: Define the movement
 * to rotate in x axis TODO: Build the tunnel TODO: Constrain the movement to
 * the tunnel TODO: Start the movement in the start of the tunnel TODO: Only
 * move forward TODO: Measure: - time - start point - end point - curves in the
 * bezier tunnel - deviation of the original path - collisions - stop times -
 * lost sensing hands - TODO: Calculate: - Quantitative Throughput -
 * */

@SuppressWarnings("serial")
public class Devices3d extends PApplet {
	Kinect kinect; // Kinect device class
	Scene scene; // 3D Scene
	HIDevice dev; // Object to control the scene with the device
	InteractiveAvatarFrame avatar; // Sight of the camera. Avatar used to control the camera
	
	PVector left; // Vector with screen position of the Left Hand returned by the device
	PVector right; // Vector with screen position of the Right Hand returned by the device
	PVector trans; // Vector with translation values returned by the device
	PVector rotat; // Vector with rotation values returned by the device

	public void setup() {
		size(1200,800, P3D);
		scene = new Scene(this);
		scene.registerCameraProfile(new CameraProfile(scene, "THIRD_PERSON",CameraProfile.Mode.THIRD_PERSON));
		//Configure the avatar
		avatar = new InteractiveAvatarFrame(scene);
		avatar.setPosition(50, 100, 10);
		avatar.setOrientation(new Quaternion(new PVector(0, 0, 1), new PVector(0,10,0)));
		avatar.setInclination(0);
		// Add the avatar to the scene
		scene.setInteractiveFrame(avatar);
		// Set the camera profile
		scene.setCurrentCameraProfile("THIRD_PERSON");

		// Create the kinect object
		int zinit=1500;
		kinect = new Kinect(this,zinit);

		// Define the RELATIVE mode HIDevice.
		dev = new HIDevice(scene);
		dev.addHandler(this,"feed");
		dev.setTranslationSensitivity(0.01f, 0.01f, 0.01f);
		dev.setRotationSensitivity(0.001f, 0.0001f, 0.0001f);
		scene.addDevice(dev);
		
		left = new PVector(0, 0, 0);
		right = new PVector(0, 0, 0);
		trans = new PVector(0, 0, 0);
		rotat = new PVector(0, 0, 0);
		smooth();
	}

	public void draw() {
		background(0);
		
		// Update and draw the kinect data from the sensor
		kinect.draw();

		//Draw a cube
		pushMatrix();
		    pushStyle();
		    	fill(200,100,0);
		    	translate(10,20,10);
		    	box(5);
		    popStyle();
	    popMatrix();
		
		// Draw the translation and rotation values
		drawMovements();
	}

	/**
	 * Feed the translations and rotations to the scene, gives the hand positions
	 * */
	public void feed(HIDevice d) {
		left = kinect.screenHand("left");
		right = kinect.screenHand("right");
		trans = kinect.translationVector();
		rotat = kinect.rotationVector();
		d.feedTranslation(trans.x, trans.y, trans.z);
		d.feedRotation(rotat.x, rotat.y, rotat.z);
	}
	
	/**
	 * Draw the translation and rotation vectors in the screen
	 * */
	public void drawMovements() {
		float alpha=80;
		scene.beginScreenDrawing();
			pushStyle();
				noFill();
				strokeWeight(4);
		
				// X
				stroke(255, 0, 0);
				line(width / 2, height - 2, (width / 2) + trans.x, height - 2);
				stroke(255, 70, 70);
				line(width / 2, height - 6, (width / 2) + rotat.x, height - 6);
		
				// Y
				stroke(0, 255, 0);
				line(2, (height / 2), 2, (height / 2) + trans.y);
				stroke(70, 255, 70);
				line(6, (height / 2), 6, (height / 2) + rotat.y);
		
				// Z
				stroke(0, 0, 255);
				line(width - 2, (height / 2), width - 2, (height / 2) - trans.z);
				stroke(70, 70, 255);
				line(width - 6, (height / 2), width - 6, (height / 2) + rotat.z);
				
				//Hands position
				stroke(100, 255, 0, alpha);
				point(left.x, left.y);
				point(right.x, right.y);
				
				// Draw the sight
				float halfWidth=width/2;
				float halfHeight=height/2;
				noFill();
				strokeWeight(2);
				stroke(100, 255, 0, alpha);
				ellipse(halfWidth,halfHeight,30,30);
				line(halfWidth-25,halfHeight,halfWidth+25,halfHeight);
				line(halfWidth,halfHeight-25,halfWidth,halfHeight+25);
				
				// Draw the depth image of the sensor
				PImage depthImage=kinect.getDepthImage();
				image(depthImage,10,0,kinect.width()/3,kinect.height()/3);
				PImage rgbImage=kinect.getRGBImage();
				image(rgbImage,10,kinect.height()/3,kinect.width()/3,kinect.height()/3);
			popStyle();
		scene.endScreenDrawing();
	}

}

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
import remixlab.experiments.*;
import remixlab.proscene.*;

/**
 * TODO: Define the movement to rotate in x axis 
 * TODO: Build the path of spheres
 * TODO: Constrain the movement in the space
 * TODO: Draw floor
 * TODO: Draw background
 * TODO: Draw an indicator arrow, show the next sphere, at last the target
 * TODO: Change the color of the sphere when is active
 * TODO: Measure for each interval of each trial:
 *  - time: time starting when enter
 *  - speed
 *  - start point
 *  - end point
 *  - radius ini
 *  - radius end
 *  - real trajectory (list of points)
 *  - deviation of the original path
 *  - stop times 
 *  - lost sensing hands
 *  - input point (where avatar enter in the marker sphere)
 *  - error in with (We) (distance between axis of the trajectory and input point)
 *  - error id distance (De) (distance between start point and the input point)
 *  - error in trajectory (Te) (average of the distance between trajectory and real trajectory)
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
	SpaceNavigator space;// SpaceNavigator device class
	Scene scene; 	// 3D Scene
	Avatar avatar; 	// Sight of the camera. Avatar is used to control the camera
	Device device;	// Current Device 
	
	PVector left; 	// Vector with screen position of the Left Hand returned by the device (only for Kinect)
	PVector right; 	// Vector with screen position of the Right Hand returned by the device (only for Kinect)
	PVector trans; 	// Vector with translation values returned by the device
	PVector rotat; 	// Vector with rotation values returned by the device
	
	//PARAMETERS
	float size;			//Size of the space (x and y)
	float high;			//High of the space (z)
	int divisions;		//Number of divisions of the floor and ceiling
	Color spaceColor;	//Color of the space
	
	Trial trial;	//Testing trials
	
	
	public void setup() {
		size(1910,1080, P3D);
		scene = new Scene(this);
		scene.setGridIsDrawn(false);
		
		//Set the space of the experiments
		size=1000;
		high=300;
		divisions=60;
		spaceColor=new Color(180,252,254,100);
		
		//Initialize the vectors
		left=right=trans=rotat= new PVector(0, 0, 0);
		
		//Configure the avatar
		avatar=new Avatar(this,scene,new PVector(size,size,10));
		
		//Define the current device
		device=Device.SPACENAVIGATOR;
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
			case SPACENAVIGATOR:
				space=new SpaceNavigator(this,scene);
				scene.addDevice(space.getDevice());
				scene.disableMouseHandling();
				avatar.drawHands(false);
				break;
			default : 
				//Using the mouse
		}
		println("Device: "+device);		

		//Trial for testing
		trial=new Trial(this,scene,20);
		
		String[] lines = new String[3];
		lines[0] = "hola";
		lines[1] = "in";
		lines[2] = "mundo";
		saveStrings("data/lines.txt", lines);
		
		smooth();
	}

	public void draw() {
		background(0);
		
		//Draw the markers and the target
		trial.draw();
		
		//Draw the experiment space
		drawSpace(size,high,divisions,avatar.getRadius(),spaceColor);
		
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
			case SPACENAVIGATOR:
				loadSpaceData();
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
	/**
	 * Load the SpaceNavigator data to display in the avatar
	 * */
	public void loadSpaceData(){
		trans = space.translationVector();
		rotat = space.rotationVector();
	}
	
	/**
	 * Draw the space: ceiling, floor, columns and set the camera constraints
	 * */
	public void drawSpace(float size,float high,int divisions,float avatarRadius,Color color){
		//Draw the floor and ceiling
		drawGrid(size,divisions,0,0,0,color);
		drawGrid(size,divisions,0,0,high,color);
		//Draw the columns
		drawColumns(size,high,color);
		//Constraint the camera to the box
		boxConstraint(avatarRadius,size-avatarRadius,avatarRadius,size-avatarRadius,avatarRadius,high-avatarRadius);
	}
	
	/**
	 * Draw four columns, one in each corner
	 * */
	public void drawColumns(float size,float high,Color color){
		pushStyle();
			stroke(color.getRGB(),color.getAlpha());
			strokeWeight(3);
			line(0,0,0,0,0,high);
			line(size,0,0,size,0,high);
			line(size,size,0,size,size,high);
			line(0,size,0,0,size,high);
		popStyle();
	}
	/**
	 * Draws a grid with @size in the XY plane, centered on (x,y) with high z (defined in the current
	 * coordinate system).
	 * */
	public void drawGrid(float size,int divisions,float x,float y,float z,Color color){
		pushStyle();
			stroke(color.getRGB(),color.getAlpha());
			strokeWeight(1);
			beginShape(LINES);
				float midSize=size/2;
				for (int i = 0; i <= divisions; ++i) {
					final float pos = (midSize * (2.0f * i / divisions - 1.0f));
					float diffX=midSize+x;
					float diffY=midSize+y;
					vertex(diffX+pos , diffY-midSize,z);
					vertex(diffX+pos , diffY+midSize,z);
					vertex(diffX-midSize, diffY+pos,z);
					vertex(diffX+midSize, diffY+pos,z);
				}
			endShape();
		popStyle();
	}
	/**
	 * Constraint the camera in the specified box
	 * */
	public void boxConstraint(float minX,float maxX,float minY,float maxY,float minZ,float maxZ){
		PVector pos=scene.camera().position();
		//Correct X position
		if(pos.x<=minX)
			pos.x=minX;
		if(pos.x>=maxX)
			pos.x=maxX;
		//Correct Y position
		if(pos.y<=minY)
			pos.y=minY;
		if(pos.y>=maxY)
			pos.y=maxY;
		//Correct Z position
		if(pos.z<=minZ)
			pos.z=minZ;
		if(pos.z>=maxZ)
			pos.z=maxZ;
		//Restore the camera position
		scene.camera().setPosition(pos);
	}
}

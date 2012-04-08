/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Sketch Class, inherit class from Scene
 * */
package remixlab.experiments;

import java.awt.Color;

//import devices3d.Devices3d.Device;
import processing.core.PApplet;
import processing.core.PVector;
import remixlab.devices.Avatar;
import remixlab.devices.Kinect;
import remixlab.devices.SpaceNavigator;
import remixlab.devices.Wiimote;
import remixlab.proscene.Scene;

public class Sketch extends Scene {
	PApplet p;
	String identification;		//User identification
	
	//Enum of devices
	public enum Devices {KINECT,WIIMOTE,MOUSE,SPACENAVIGATOR}
	
	Kinect kinect; 	// Kinect device class
	Wiimote wiimote;// Wiimote device class
	SpaceNavigator space;// SpaceNavigator device class
//	Scene scene; 	// 3D Scene
	Avatar avatar; 	// Sight of the camera. Avatar is used to control the camera
	Devices device;	// Current Device 
	
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
	
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	public Sketch(PApplet p){
		super(p);
	}
	/////////////////////////////////////// GET AND SET ///////////////////////////////////////
	public void setIdentification(String identif){
		identification=identif;
	}

	/////////////////////////////////////// METHODS ///////////////////////////////////////
	public void init() {
		p=super.parent;
		this.setGridIsDrawn(false);
		
		//Set the space of the experiments
		size=2000;
		high=300;
		divisions=60;
		spaceColor=new Color(180,252,254,100);
		
		//Initialize the vectors
		left=right=trans=rotat= new PVector(0, 0, 0);
		
		//Configure the avatar
		PVector start=new PVector(10,10,150);
		avatar=new Avatar(p,this,start);
		
		
		
		long identification=00;
		Experiment exp=new Experiment(p,this,identification);
//		exp.printBlocks();
		
		
		
		//Define the current device
		device=Devices.SPACENAVIGATOR;
		switch (device){
			case KINECT:
				kinect = new Kinect(p,this);
				this.addDevice(kinect.getDevice());
//				scene.disableMouseHandling();
				break;
			case WIIMOTE:
				wiimote=new Wiimote(p,this);
				this.addDevice(wiimote.getDevice());
//				scene.disableMouseHandling();
				avatar.drawHands(false);
				break;
			case SPACENAVIGATOR:
				space=new SpaceNavigator(p,this);
				this.addDevice(space.getDevice());
//				scene.disableMouseHandling();
				avatar.drawHands(false);
				break;
			default : 
				//Using the mouse
		}
		PApplet.println("Device: "+device);		

		
		
		
		
		
		parent.smooth();
	}
	//Draw replace
	public void proscenium() {
		p.background(0);
		
		
		PApplet.println("identification: "+identification);
		
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
		p.pushStyle();
			p.stroke(color.getRGB(),color.getAlpha());
			p.strokeWeight(3);
			p.line(0,0,0,0,0,high);
			p.line(size,0,0,size,0,high);
			p.line(size,size,0,size,size,high);
			p.line(0,size,0,0,size,high);
		p.popStyle();
	}
	/**
	 * Draws a grid with @size in the XY plane, centered on (x,y) with high z (defined in the current
	 * coordinate system).
	 * */
	public void drawGrid(float size,int divisions,float x,float y,float z,Color color){
		p.pushStyle();
			p.stroke(color.getRGB(),color.getAlpha());
			p.strokeWeight(1);
			p.beginShape(LINES);
				float midSize=size/2;
				for (int i = 0; i <= divisions; ++i) {
					final float pos = (midSize * (2.0f * i / divisions - 1.0f));
					float diffX=midSize+x;
					float diffY=midSize+y;
					p.vertex(diffX+pos , diffY-midSize,z);
					p.vertex(diffX+pos , diffY+midSize,z);
					p.vertex(diffX-midSize, diffY+pos,z);
					p.vertex(diffX+midSize, diffY+pos,z);
				}
			p.endShape();
		p.popStyle();
	}
	/**
	 * Constraint the camera in the specified box
	 * */
	public void boxConstraint(float minX,float maxX,float minY,float maxY,float minZ,float maxZ){
		PVector pos=this.camera().position();
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
		this.camera().setPosition(pos);
	}
}

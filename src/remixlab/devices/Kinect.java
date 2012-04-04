/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Kinect Class, this class manage the Kinect interaction
 * are supported.
 * */
package remixlab.devices;

import java.awt.Color;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;
import SimpleOpenNI.XnVFlowRouter;
import SimpleOpenNI.XnVSessionManager;

public class Kinect {
	PApplet parent; 					// Processing Object
	SimpleOpenNI context; 				// Simple OpenNI Context
	XnVSessionManager sessionManager; 	// NITE Object
	XnVFlowRouter flowRouter; 			// NITE Object
	PointControl ctrlPoint; 			// Return the points from sensor
	Hand left,right;					// Hands position
	PVector trans;						// The vector with translation values returned by the device
	PVector rotat;						// The vector with rotation values returned by the device
	
	PVector starting;					// Start position, set when two hands are detected
	Hand[] hands;
	
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	/**
	 * Kinect Constructor
	 * 
	 * @param p
	 *            : PApplet parent
	 * */
	public Kinect(PApplet p) {
		parent = p;
		starting=new PVector(0,0,1200);
		context = new SimpleOpenNI(parent);
		// mirror is by default enabled
		context.setMirror(true);
		// enable depthMap generation
		context.enableDepth();
		context.enableRGB();
		// enable the hands + gesture
		context.enableGesture();
		context.enableHands();
		// Setup NITE
		sessionManager = context.createSessionManager("Click,Wave","RaiseHand");
		// Instantiate the control point
		ctrlPoint = new PointControl(this);
		flowRouter = new XnVFlowRouter();
		flowRouter.SetActive(ctrlPoint);
		// Set the session manager
		sessionManager.AddListener(flowRouter);
		// Initialize the hands
		left = new Hand(new Color(255, 0, 0));
		right = new Hand(new Color(0, 255, 0));
		//Initialize movements vectors
		trans=new PVector(0,0,0);
		rotat=new PVector(0,0,0);
		
		
		hands=new Hand[2];
		hands[0]=new Hand();
		hands[1]=new Hand();
	}
	
	/////////////////////////////////////// GET AND SET ///////////////////////////////////////
	/**
	 * Return the depth map of the context
	 * */
	public int[] getDepthMap(){
		return context.depthMap();
	}
	/**
	 * Return the Image of the depth map
	 * */
	public PImage getDepthImage(){
		return context.depthImage();
	}
	/**
	 * Return the Image of the depth map
	 * */
	public PImage getRGBImage(){
		return context.rgbImage();
	}
	/**
	 * Return the vector of the depth map in real world coordinates
	 * TODO: break the update of hands
	 * */
	public PVector getVectorDepthMap(int index){
		return context.depthMapRealWorld()[index];
	}
	/**
	 * Return the Width provided by the context
	 * */
	public int width() {
		return context.depthWidth();
	}
	/**
	 * Return the Height provided by the context
	 * */
	public int height() {
		return context.depthHeight();
	}
	/**
	 * Get the specified hand, based in the position in the x axis
	 * */
	public Hand getHand(String hand) {
		if (hand == "left"){
			return left;
		}else{
			return right;
		}
	}
	/**
	 * Get the number of hands registered
	 * */
	public int getNumberHands() {
		return ctrlPoint.getHands();
	}
	/**
	 * Return the point with World coordinates in screen (projective) coordinates
	 * */
	public PVector getScreen(PVector point) {
		PVector screenPos = new PVector();
		context.convertRealWorldToProjective(point, screenPos);
		return screenPos;
	}
	
	/////////////////////////////////////// METHODS ///////////////////////////////////////
	/**
	 * Update the context and the session manager
	 * */
	public void draw() {
		// update the context
		context.update();
		// update nite
		context.update(sessionManager);
	}	
	/**
	 * Return the vector of screen position of the specified hand
	 * */
	public PVector screenHand(String hand){
		PVector pos=new PVector(0,0,0);
		if(getNumberHands()==2){
			if(hand=="left"){
				pos=getScreen(left.getPoint());
			}else{
				pos=getScreen(right.getPoint());
			}
			//Relative to the screen size
			pos.x=(parent.width/2)-(width()/2)+pos.x;
			pos.y=(parent.height/2)-(height()/2)+pos.y;
		}
		return pos;
	}
	/**
	 * Return the vector of translation calculated using the position of hands
	 * */
	public PVector translationVector(){
		trans=new PVector(0,0,0);
		if(getNumberHands()==2){
			trans.x=((left.getPoint().x+right.getPoint().x)/2)-starting.x;
			trans.y=(-(left.getPoint().y+right.getPoint().y)/2)+starting.y;
			trans.z=starting.z-((left.getPoint().z+right.getPoint().z)/2);
		}
		return trans;
	}
	/**
	 * Return the vector of rotations calculated using the position of hands
	 * */
	public PVector rotationVector(){
		rotat=new PVector(0,0,0);
		if(getNumberHands()==2){
			//TODO: Define a gesture to x-rotation rotation.x=(left.getPoint().x-right.getPoint().x);
			rotat.x=0;
			rotat.y=-(left.getPoint().z-right.getPoint().z);
			rotat.z=(left.getPoint().y-right.getPoint().y);
		}
		return rotat;
	}
	
	/////////////////////////////////////// CALLBAKS ///////////////////////////////////////
	/**
	 * Receive the detection of one hand, if no one is set, set the starting position
	 * */
	public void setStarting(PVector handPoint){
		if(getNumberHands()==1){
			PVector setedHand=new PVector();
			if(left.getPoint().x!=0&&left.getPoint().y!=0&&left.getPoint().z!=0){
				setedHand=left.getPoint();
			}else{
				setedHand=right.getPoint();
			}
			starting.x=((handPoint.x+setedHand.x)/2);
			starting.y=((handPoint.y+setedHand.y)/2);
			starting.z=((handPoint.z+setedHand.z)/2);
		}
	}
	/**
	 * Get the callback signal when a hand is updated in the sensor
	 * TODO: Optimize the pass of the handPoint to left/right, many steps
	 * */
	public void setHands(long handId, PVector handPoint) {
		// Assign the point to the hand using module
		hands[(int) (handId % 2)].setPoint(handPoint);
		//PApplet.println("handPoint["+handId+"]: "+handPoint);
		if(hands[0].getPoint().x < hands[1].getPoint().x) {
			left = hands[0];
			right = hands[1];
		}else{
			left = hands[1];
			right = hands[0];
		}
	}
	/**
	 * Unset the hands when once is undetected by the sensor
	 * */
	public void unsetHands(){
		
	}
}

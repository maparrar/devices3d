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
import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;
import SimpleOpenNI.XnVFlowRouter;
import SimpleOpenNI.XnVSessionManager;

public class Kinect{
	PApplet parent;						//Processing Object
	SimpleOpenNI context;				//Simple OpenNI Context
	XnVSessionManager sessionManager;	//NITE Object
	XnVFlowRouter     flowRouter;		//NITE Object
	PointControl ctrlPoint;				//Return the points from sensor
	
	int numHands;						//Quantity of hands
	Hand[] hands;						//Array of hands
	

	/**
	 * Kinect Constructor
	 * @param p: PApplet parent
	 * */
	public Kinect(PApplet p) {
		parent = p;
		context = new SimpleOpenNI(parent);
		
		// mirror is by default enabled
		context.setMirror(true);
		
		// enable depthMap generation 
		//context.enableDepth();
		
		// enable the hands + gesture
		context.enableGesture();
		context.enableHands();
		
		// setup NITE 
		sessionManager = context.createSessionManager("Click,Wave", "RaiseHand");
		
		//Instantiate the control point
		ctrlPoint = new PointControl(this);
		flowRouter = new XnVFlowRouter();
		flowRouter.SetActive(ctrlPoint);
		//Set the session manager
		sessionManager.AddListener(flowRouter);
		
		//Set the array of hands
		numHands=2;
		hands=new Hand[numHands];
		hands[0]=new Hand(20,new Color(255,0,0));
		hands[1]=new Hand(20,new Color(0,255,0));
		
		
		
	}
	/**
	 * Return the Width provided by the context
	 * */
	public int width(){
		return context.depthWidth();
	}
	/**
	 * Return the Height provided by the context
	 * */
	public int height(){
		return context.depthHeight();
	}
	
	/**
	 * Get the specified hand
	 * */
	public Hand getHand(int idHand){
		return hands[idHand];
	}
	/**
	 * Get the number of hands registered
	 * */
	public int getNumberHands(){
		return ctrlPoint.getHands();
	}
	/**
	 * Update the context and the session manager
	 * */
	public void update(){
		// update the context
		context.update();
		// update nite
		context.update(sessionManager);
		// draw depthImageMap
		//parent.image(context.depthImage(),0,0);
		
		
		
		//drawHands();
		
	}
	
	/**
	 * Get the callback signal when a hand is updated in the sensor
	 * */
	public void setHands(long handId,PVector handPoint){
		//Assign the point to the hand using module
		hands[(int) (handId%2)].addPoint(handPoint);
	}
	/**
	 * Remove the list of points of a hand if it is destroyed in the context
	 * */
	public void deleteHand(long handId){
		//Delete the points of the hand using module
		hands[(int) (handId%2)].reset();
	}
	
	/**
	 * Draw the points of the set of hands
	 * */
	public void drawHands(){
		for(int i=0;i<numHands;i++){
			if(hands[i].points.size()>0){
				parent.pushStyle();
					parent.noFill();
					PVector vec;
					PVector firstVec;
					PVector screenPos = new PVector();
					parent.strokeWeight(2);
					parent.stroke(hands[i].color.getRGB());
					// draw line
					firstVec = null;
					Iterator<PVector> itr = hands[i].points.iterator();
					parent.beginShape();
						while (itr.hasNext()){
							vec = itr.next();
							if(firstVec == null){
								firstVec = vec;
							}
							// calculate the screen position
							context.convertRealWorldToProjective(vec,screenPos);
							parent.vertex(screenPos.x,screenPos.y);
						}
					parent.endShape();
					// draw current position of the hand
					if(firstVec != null){
						parent.strokeWeight(8);
						context.convertRealWorldToProjective(firstVec,screenPos);
						parent.point(screenPos.x,screenPos.y);
					}
				parent.popStyle();			
			}
		}
	}
}

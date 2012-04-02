/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * */

package devices3d;


import processing.core.PApplet;
import remixlab.devices.Kinect;

import SimpleOpenNI.*;


@SuppressWarnings("serial")
public class Devices3d extends PApplet {
	

	


	SimpleOpenNI      context;

	// NITE
	XnVSessionManager sessionManager;
	XnVFlowRouter     flowRouter;

	Kinect pointDrawer;

	public void setup()
	{
		
		
	  context = new SimpleOpenNI(this);
	   
	  // mirror is by default enabled
	  context.setMirror(true);
	  
	  // enable depthMap generation 
	  context.enableDepth();
	  
	  // enable the hands + gesture
	  context.enableGesture();
	  context.enableHands();
	 
	  // setup NITE 
	  sessionManager = context.createSessionManager("Click,Wave", "RaiseHand");
	  


	  pointDrawer = new Kinect(this, context);
	  flowRouter = new XnVFlowRouter();
	  flowRouter.SetActive(pointDrawer);
	  
	  sessionManager.AddListener(flowRouter);
	           
	  size(context.depthWidth(), context.depthHeight()); 
	  smooth();
	}

	public void draw()
	{
	  background(200,0,0);
	  // update the cam
	  context.update();
	  
	  // update nite
	  context.update(sessionManager);
	  
	  // draw depthImageMap
	  image(context.depthImage(),0,0);
	  
	  // draw the list
	  pointDrawer.draw();
	}

//	public void keyPressed(){
////		switch(key){
////			case 'e':
////				// end sessions
////				sessionManager.EndSession();
////				println("end session");
////			break;
////		}
//	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// session callbacks

//	void onStartSession(PVector pos)
//	{
//	  println("onStartSession: " + pos);
//	}
//
//	void onEndSession()
//	{
//	  println("onEndSession: ");
//	}
//
//	void onFocusSession(String strFocus,PVector pos,float progress)
//	{
//	  println("onFocusSession: focus=" + strFocus + ",pos=" + pos + ",progress=" + progress);
//	}


}

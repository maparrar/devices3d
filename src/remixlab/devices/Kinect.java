/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Kinect Class, this class manage the Kinect interaction, now only two hands
 * are supported.
 * */
package remixlab.devices;

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
	
	
	PVector auxiliar; // Vector temporal para almacenar el punto anterior de una mano (solo para pruebas)
	

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
		context.enableDepth();
		
		// enable the hands + gesture
		context.enableGesture();
		context.enableHands();
		
		// setup NITE 
		sessionManager = context.createSessionManager("Click,Wave", "RaiseHand");
		
		//Instantiate the control point
		ctrlPoint = new PointControl(this);
		flowRouter = new XnVFlowRouter();
		flowRouter.SetActive(ctrlPoint);
		
		
		sessionManager.AddListener(flowRouter);
		
		
		auxiliar = new PVector();
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
	 * Update the context and the session manager
	 * */
	public void update(){
		// update the context
		context.update();
		// update nite
		context.update(sessionManager);
		// draw depthImageMap
		parent.image(context.depthImage(),0,0);
	}
	
	/**
	 * Get the callback signal when a hand is updated in the sensor
	 * */
	public void setHands(long handId,PVector handPoint){
		//PApplet.println("Updating the hands");
		
		// 1. Almacenar los últimos n puntos de la mano
		// 2. Hacer una operación para determinar el vector de cambio
		// El vector de cambio es un vector tridimensional que apunta hacia
		// donde se hizo el movimiento
		// la longitud del vector representa la velocidad de cambio
		// 3. Se unen los dos vectores de cambio que aparecen en la pantalla
		// 4. Se comparan los valores de los vectores de cambio con algunos
		// almacenados
		// 5. Si corresponden a alguno en particular, se ejecuta el gesto

		// println(colorIndex+":: "+firstVec);

		// PRUEBA DE GESTOS
		// Mover hacia el frente
		if (Math.abs(auxiliar.z - handPoint.z) > 50) {
			PApplet.println("hand: " + handId + " -> click");
		}
		// Mover a la derecha
		if ((handPoint.x - auxiliar.x) > 50) {
			PApplet.println("hand: " + handId + " -> derecha");
		}
		// Mover hacia arriba
		if ((handPoint.y - auxiliar.y) > 50) {
			PApplet.println("hand: " + handId + " -> arriba");
		}

		auxiliar = handPoint;
	}
}

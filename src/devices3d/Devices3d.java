/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Example of use: draw a 3D scene and manipulate with the Sensor 
 * */

package devices3d;

import controlP5.*;
import processing.core.PApplet;
import remixlab.experiments.*;

/**
 * TODO: Draw an indicator arrow, show the next sphere, at last the target
 * TODO: Change the color of the sphere when is active
 * TODO: Measure for each interval of each trial:
 * 	- insideInterval: inside point in interval coordinates
 * 	- insideWorld: inside point in world coordinates
 * 	- start point
 *  - end point
 *  - radius
 *  - time: time starting when enter
 * -------
 *  - deviation of the original path
 * TODO: Calculate:
 *  - Quantitative Throughput 
 *  TODO: Improve the sensitivity of all devices
 * */

@SuppressWarnings("serial")
public class Devices3d extends PApplet {
	Sketch sketch;				//Inherit Scene
	ControlP5 controlP5;		//GUI manager
	Textfield myTextfield;		//Identification textfield
	public void setup() {
		size(1910,870,P3D);
		//Setup the identification textfield
		controlP5 = new ControlP5(this);
		myTextfield = controlP5.addTextfield("identif",100,160,500,25);
		myTextfield.setFocus(true);
	}
	public void draw() {
		background(0);
		controlP5.draw();
	}
	/**
	 * Callback of the textfield, pass the identification to the 3D scene
	 * */
	public void identif(String identification) {
		if(identification.length()>5){
			sketch = new Sketch(this);
			sketch.setIdentification(identification);
		}
	}
}

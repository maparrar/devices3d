/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * */

package devices3d;
import processing.core.PApplet;
import remixlab.devices.Kinect;

@SuppressWarnings("serial")
public class Devices3d extends PApplet {
	Kinect kinect;

	public void setup(){
		kinect = new Kinect(this);
		//Resize the window from the depth map size
		size(kinect.width(), kinect.height()); 
		smooth();
	}

	public void draw(){
		background(200,0,0);
		
		//Update the kinect data from the sensor
		kinect.update();
	}
}

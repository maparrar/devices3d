/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Experiment Class
 * */
package remixlab.experiments;

import java.awt.Color;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PVector;
import remixlab.devices.*;

public class Experiment {
	public enum Device {KINECT,WIIMOTE,MOUSE,SPACENAVIGATOR,NONE}
	public enum State {BEFORE,RUNNING,AFTER,INSIDE}
	State state;		//State of the block
	PApplet p;
	Sketch sketch;
	
	//D E V I C E S
	Kinect kinect; 	// Kinect device class
	Wiimote wiimote;// Wiimote device class
	SpaceNavigator space;// SpaceNavigator device class
	Device device;	// Current Device 
	//Vectors
	PVector left; 	// Vector with screen position of the Left Hand returned by the device (only for Kinect)
	PVector right; 	// Vector with screen position of the Right Hand returned by the device (only for Kinect)
	PVector trans; 	// Vector with translation values returned by the device
	PVector rotat; 	// Vector with rotation values returned by the device
	
	//P A R A M E T E R S
	String identification;//Identification of the participant
	int nBlocks;		//Block per device
	int nTrials;		//Trials per Block
	int nIntervals;		//Intervals per trial
	int currentBlock;	//Block running
	ArrayList<Block> blocks = new ArrayList<Block>();
	
	//S T Y L E
	Color intervalColor;
	
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	public Experiment(PApplet parent,Sketch s){
		p = parent;
		sketch = s;
		intervalColor=new Color(255,0,0);
		
		//Initialize the vectors
		left=right=trans=rotat= new PVector(0, 0, 0);
		
		//Set the starting state
		state=State.RUNNING;
		
		//Set the first device
//		device=Device.MOUSE;
//		device=Device.WIIMOTE;
//		device=Device.SPACENAVIGATOR;
		device=Device.KINECT;

		
		
		initDevice(device);
		
		//Parameters
		nBlocks=2;
		nTrials=9;
		nIntervals=3;
		currentBlock=0;
		
		//Load the data blocks
		for(int i=0;i<nBlocks;i++){
			loadBlock(i);
		}
	}
	/////////////////////////////////////// GET AND SET ///////////////////////////////////////
	public void setIdentification(String identif){
		identification=identif;
	}
	public State state(){
		return state;
	}
	public Device getDevice(){
		return device;
	}
	/////////////////////////////////////// METHODS ///////////////////////////////////////
	public void draw(){
		//Execute the draw an load data from the device selected
		switch (device){
			case WIIMOTE:
				wiimote.draw();
				loadWiimoteData();
				break;
			case SPACENAVIGATOR:
				loadSpaceData();
				break;
			case KINECT:
				kinect.draw();
				loadKinectData();
				break;
			default : 
				
		}
		//Show the current block
		if(currentBlock<nBlocks){
			if(blocks.get(currentBlock).state()==State.BEFORE){
				blocks.get(currentBlock).init();
			}else if(blocks.get(currentBlock).state()==State.RUNNING){
				blocks.get(currentBlock).draw(sketch.camera().position());
			}else{
				saveBlock(blocks.get(currentBlock));
				currentBlock++;
			}
		}else{
			changeDevice();
			//If can't change device, finish the experiment
			if(device==Device.NONE){
				state=State.AFTER;
			}else{
				reinit();
			}
		}
	}
	/**
	 * Change to the next device
	 * */
	public void changeDevice(){
		PApplet.println("changeDevice(): "+device);
		switch (device){
			case MOUSE:
				device=Device.WIIMOTE;
				break;
			case WIIMOTE:
				device=Device.SPACENAVIGATOR;
				break;
			case SPACENAVIGATOR:
				device=Device.KINECT;
				break;
			case KINECT: 
				device=Device.NONE;
				break;
		}
		initDevice(device);
	}
	/**
	 * Initialize the current device
	 * */
	public void initDevice(Device currentDevice){
		switch (currentDevice){
			case WIIMOTE:
				wiimote=new Wiimote(p,sketch);
				sketch.addDevice(wiimote.getDevice());
				sketch.disableMouseHandling();
				break;
			case SPACENAVIGATOR:
				space=new SpaceNavigator(p,sketch);
				sketch.addDevice(space.getDevice());
				sketch.disableMouseHandling();
				break;
			case KINECT:
				kinect = new Kinect(p,sketch);
				sketch.addDevice(kinect.getDevice());
				sketch.disableMouseHandling();
				break;
			default :
//				sketch.toggleCursorHiddenOnFirstPerson();
				sketch.camera().setFlySpeed(2);
//				sketch.camera().setTranslationSensitivity(2);
				sketch.camera().setRotationSensitivity(3);
				
		}
		PApplet.println("Started device: "+currentDevice);	
	}
	/////////////////////////////////////// LOADERS ///////////////////////////////////////
	/**
	 * Initialize the blocks, the trials and the intervals
	 * */
	public void reinit(){
		currentBlock=0;
		for(int i=0;i<blocks.size();i++){
			blocks.get(i).reinit();
		}
	}
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
	 * Save the block with name: identification_device_block.txt
	 * block
	 * trial
	 * interval
	 * ini.x			// Position of the last interval sphere
	 * ini.y	
	 * ini.z
	 * end.x			// Position vector of the Frame
	 * end.y
	 * end.z
	 * insideInterval.x	// Point where inside the camera in Interval frame coordinates
	 * insideInterval.y
	 * insideInterval.z
	 * insideWorld.x	// Point where inside the camera in World frame
	 * insideWorld.y
	 * insideWorld.z
	 * D				// Distance between ini and end
	 * W				// Diameter of the end-sphere
	 * ID				//Index of difficulty
	 * startTime		//Time of the last interval
	 * insideTime		// Time since the applet starts to the camera inside
	 * */
	public void saveBlock(Block block){
		int index=0;
		String[] lines = new String[nTrials*nIntervals];
		for(int i=0;i<block.trials.size();i++){
			float startTime=block.trials.get(i).start.insideTime;
			for(int j=0;j<block.trials.get(i).intervals.size();j++){
				Interval interval=block.trials.get(i).intervals.get(j);
				
				String cameraPoints="";
				for(int k=0;k<interval.cameraPoints.size();k++){
					cameraPoints.concat(interval.cameraPoints.get(k).x+"-"+interval.cameraPoints.get(k).y+"-"+interval.cameraPoints.get(k).z+"++");
				}
				
				lines[index]=block.id+"|"+
						i+"|"+
						j+"|"+
						interval.ini.x+"|"+
						interval.ini.y+"|"+
						interval.ini.z+"|"+
						interval.end.x+"|"+
						interval.end.y+"|"+
						interval.end.z+"|"+
						interval.insideInterval.x+"|"+
						interval.insideInterval.y+"|"+
						interval.insideInterval.z+"|"+
						interval.insideWorld.x+"|"+
						interval.insideWorld.y+"|"+
						interval.insideWorld.z+"|"+
						interval.D+"|"+
						interval.W+"|"+
						interval.ID+"|"+
						startTime+"|"+
						interval.insideTime+"|"+
						cameraPoints;
				startTime=interval.insideTime;
				PApplet.println("Points of camera: "+interval.cameraPoints.size());
				index++;
			}
		}
		p.saveStrings("data/"+identification+"_"+device+"_"+block.id+".txt",lines);
		PApplet.println("Saved: data/"+identification+"_"+device+"_"+block.id+".txt");
	}
	/**
	 * Load the experimental parameters
	 * */
	public void loadBlock(int nBlock){
		int blockF=0;
		Trial[] trials=new Trial[nTrials];
		for(int i=0;i<nTrials;i++){
			trials[i]=new Trial(p,sketch,i);
		}
		String[] lines;
		lines = p.loadStrings("data/block"+nBlock+".txt");
		if(lines!= null){
			for(int j=0;j<lines.length;j++){
				String[] row = PApplet.split(lines[j],'|');
				//Data read from file
				blockF=Integer.parseInt(row[0]);
				int trialF=Integer.parseInt(row[1]);
				PVector ini=new PVector(Float.parseFloat(row[3]),Float.parseFloat(row[4]),Float.parseFloat(row[5]));
				PVector end=new PVector(Float.parseFloat(row[6]),Float.parseFloat(row[7]),Float.parseFloat(row[8]));
				float D=Float.parseFloat(row[9]);
				float W=Float.parseFloat(row[10]);
				float ID=Float.parseFloat(row[11]);
				Interval interval=new Interval(p,sketch,ini,end,D,W,ID,intervalColor);
				trials[trialF].addInterval(interval);
			}
		}
		//Add the block to the list
		Block block=new Block(sketch,blockF);
		for(int i=0;i<nTrials;i++){
			block.addTrial(trials[i]);
		}
		blocks.add(block);
	}
	/**
	 * Print the experimental parameters
	 * */
	public void printBlocks(){
		for(int i=0;i<blocks.size();i++){
			for(int j=0;j<blocks.get(i).trials.size();j++){
				for(int k=0;k<blocks.get(i).trials.get(j).intervals.size();k++){
					Interval interval=blocks.get(i).trials.get(j).intervals.get(k);
					PApplet.println("Block "+i+">Trial "+j+"]>Interval "+k+">> int.D: "+interval.D+" > int.W: "+interval.W);
				}
			}
		}
	}
}

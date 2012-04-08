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
import remixlab.proscene.Scene;

public class Experiment {
	public enum State {BEFORE,RUNNING,AFTER}
	State state;		//State of the block
	
	PApplet parent;
	Scene scene;
	
	long identification;//Identification of the participant
	
	
	int nBlocks;		//Block per device
	int nTrials;		//Trials per Block
	int nIntervals;		//Intervals per trial
	
	
	int currentBlock;	//Block running
	
	ArrayList<Block> blocks = new ArrayList<Block>();
	
	//Style attributes
	Color intervalColor;
	
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	public Experiment(PApplet p,Scene sc,long identification){
		parent = p;
		scene = sc;
		intervalColor=new Color(0,0,255);
		
		//Set the starting state
		state=State.BEFORE;
		
		//Parameters
		nBlocks=4;
		nTrials=9;
		nIntervals=3;
		
		//Load the data blocks
		for(int i=0;i<nBlocks;i++){
			loadBlock(i);
		}
	}
	/////////////////////////////////////// GET AND SET ///////////////////////////////////////
	/**
	 * Return the state
	 * */
	public State state(){
		return state;
	}
	
	/////////////////////////////////////// METHODS ///////////////////////////////////////
	public void draw(){
		if(state==State.RUNNING){
			for(int i=0;i<blocks.size();i++){
				blocks.get(i).draw();
			}
		}
	}
	
	
	public void loadBlock(int nBlock){
		int blockF=0;
		Trial[] trials=new Trial[nTrials];
		for(int i=0;i<nTrials;i++){
			trials[i]=new Trial(i);
		}
		String[] lines;
		lines = parent.loadStrings("data/block"+nBlock+".txt");
		if(lines!= null){
			for(int j=0;j<lines.length;j++){
				String[] row = PApplet.split(lines[j],'|');
				//Data read from file
				blockF=Integer.parseInt(row[0]);
				int trialF=Integer.parseInt(row[1]);
				//int interF=Integer.parseInt(row[2]);
				PVector ini=new PVector(Float.parseFloat(row[3]),Float.parseFloat(row[4]),Float.parseFloat(row[5]));
				PVector end=new PVector(Float.parseFloat(row[6]),Float.parseFloat(row[7]),Float.parseFloat(row[8]));
				float D=Float.parseFloat(row[9]);
				float W=Float.parseFloat(row[10]);
				float ID=Float.parseFloat(row[11]);
				Interval interval=new Interval(parent,scene,ini,end,D,W,ID,intervalColor);
				trials[trialF].addInterval(interval);
			}
		}
		//Add the block to the list
		Block block=new Block(blockF);
		for(int i=0;i<nTrials;i++){
			block.addTrial(trials[i]);
		}
		blocks.add(block);
	}
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

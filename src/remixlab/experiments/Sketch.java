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
import processing.core.PApplet;
import processing.core.PVector;
import remixlab.devices.*;
import remixlab.experiments.Experiment.Device;
import remixlab.experiments.Experiment.State;
import remixlab.proscene.Scene;

public class Sketch extends Scene {
	//ATTRIBUTES
	PApplet p;
	String identification;	//User identification
	Experiment experiment;	//Experiment object
	Avatar avatar; 			// Sight of the camera. Avatar is used to control the camera
	PVector start;			//Start position of the avatar
	
	//PARAMETERS
	float size;			//Size of the space (x and y)
	float high;			//High of the space (z)
	int divisions;		//Number of divisions of the floor and ceiling
	Color spaceColor;	//Color of the space
	
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	public Sketch(PApplet p){
		super(p);
	}
	
	/////////////////////////////////////// GET AND SET ///////////////////////////////////////
	public void setIdentification(String identif){
		identification=identif;
		experiment.setIdentification(identification);
	}

	/////////////////////////////////////// METHODS ///////////////////////////////////////
	public void init() {
		p=super.parent;
		this.setGridIsDrawn(false);
		//Set the space of the experiments
		size=2000;
		high=300;
		divisions=60;
		spaceColor=new Color(180,252,254,255);
		//Configure the avatar
		start=new PVector(10,10,150);
		avatar=new Avatar(p,this,start);
		//Configure the experiment
		experiment=new Experiment(p,this);
		p.smooth();
	}
	//Draw replace
	public void proscenium() {
		p.background(0);
		//Draw the experiment space
		drawSpace(size,high,divisions,avatar.getRadius(),spaceColor);
		//Draw the experiment
		if(experiment.state==State.RUNNING){
			experiment.draw();
			if(experiment.getDevice()!=Device.KINECT){
				avatar.drawHands(false);
			}
			// Draw the graphics elements of the Avatar
			avatar.draw(experiment.trans,experiment.rotat,experiment.left,experiment.right);
		}else{
			PApplet.println("Experiment ended");
		}
	}
	/**
	 * Restart the Avatar to the original position
	 * */
	public void restartAvatar(){
		avatar.setPosition(new PVector(10,10,150));
	}
	/////////////////////////////////////// GRAPHIC METHODS ///////////////////////////////////////
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

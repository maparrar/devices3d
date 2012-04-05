/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Avatar Class, this class manage the camera in first person.
 * */
package remixlab.devices;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import remixlab.proscene.InteractiveAvatarFrame;
import remixlab.proscene.Quaternion;
import remixlab.proscene.Scene;

public class Avatar {
	PApplet parent;
	Scene scene;
	InteractiveAvatarFrame frame;	// Frame using to port the camera
	PVector position;				// Position vector of the Avatar
	Quaternion orientation;			// Orientation of the Avatar
	//Conditionals to draw elements of the Avatar
	boolean drawTranslation;
	boolean drawRotation;
	boolean drawHands;
	boolean drawSight;
	boolean drawDepthImage;
	boolean drawRGBImage;
	//Style variables
	float alpha;					//Alpha of the sight and hands points
	float width;					//Width of the screen
	float height;					//Height of the screen
	float halfWidth;
	float halfHeight;
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	public Avatar(PApplet p,Scene s,PVector pos,Quaternion orient){
		parent=p;
		scene=s;
		position=pos;
		orientation=orient;
		//Configure the avatar
		scene.camera().setPosition(position);
		scene.camera().setOrientation(orientation);
		//Configure the styles
		alpha=80;
		width=parent.width;
		height=parent.height;
		halfWidth=width/2;
		halfHeight=height/2;
		//Set visible components
		drawTranslation=true;
		drawRotation=true;
		drawHands=true;
		drawSight=true;
		drawDepthImage=true;
		drawRGBImage=true;
	}
	/////////////////////////////////////// METHODS ///////////////////////////////////////
	/**
	 * Draw the elements of the Avatar
	 * */
	public void draw(PVector trans,PVector rotat,PVector left,PVector right){
		scene.beginScreenDrawing();
			parent.pushStyle();
				//Draw the selected elements
				if(drawTranslation)
					drawTranslation(trans);
				if(drawRotation)
					drawRotation(rotat);
				if(drawHands)
					drawHands(left,right);
				if(drawSight)
					drawSight();
			parent.popStyle();
		scene.endScreenDrawing();
	}
	public void draw(PVector trans,PVector rotat,PVector left,PVector right,float withImg,float heightImg,PImage depthImage,PImage rgbImage){
		scene.beginScreenDrawing();
			parent.pushStyle();
				//Draw the selected elements
				if(drawTranslation)
					drawTranslation(trans);
				if(drawRotation)
					drawRotation(rotat);
				if(drawHands)
					drawHands(left,right);
				if(drawSight)
					drawSight();
				if(drawDepthImage)
					drawDepthImage(depthImage,withImg,heightImg);
				if(drawRGBImage)
					drawRGBImage(rgbImage,withImg,heightImg);
			parent.popStyle();
		scene.endScreenDrawing();
	}
	/**
	 * Set the translation bars as visible or invisible
	 * @param value: true to view, false to hide
	 * */
	public void drawTranslation(boolean value){
		drawTranslation=value;
	}
	/**
	 * Draw the translation bars
	 * @param trans, PVector with the translation values
	 * */
	private void drawTranslation(PVector trans){
		parent.noFill();
		parent.strokeWeight(4);
		parent.stroke(255, 0, 0);
		parent.line(halfWidth,height-2,halfWidth+trans.x,height-2);
		parent.stroke(0, 255, 0);
		parent.line(2,(halfHeight),2,halfHeight+trans.y);
		parent.stroke(0,0,255);
		parent.line(width-2,halfHeight,width-2,halfHeight-trans.z);
	}
	/**
	 * Set the rotation bars as visible or invisible
	 * @param value: true to view, false to hide
	 * */
	public void drawRotation(boolean value){
		drawRotation=value;
	}
	/**
	 * Draw the rotation bars
	 * @param rotat, PVector with the rotation values
	 * */
	private void drawRotation(PVector rotat){
		parent.noFill();
		parent.strokeWeight(4);
		parent.stroke(255, 70, 70);
		parent.line(halfWidth, height - 6,halfWidth+rotat.x,height-6);
		parent.stroke(70, 255, 70);
		parent.line(6,halfHeight,6,halfHeight+rotat.y);
		parent.stroke(70, 70, 255);
		parent.line(width-6,halfHeight,width-6,halfHeight+rotat.z);
	}
	/**
	 * Set the hand points as visible or invisible
	 * @param value: true to view, false to hide
	 * */
	public void drawHands(boolean value){
		drawHands=value;
	}
	/**
	 * Draw the hand points
	 * @param left, PVector with the left hand position
	 * @param right, PVector with the right hand position
	 * */
	private void drawHands(PVector left,PVector right){
		parent.stroke(100, 255, 0, alpha);
		parent.point(left.x, left.y);
		parent.point(right.x, right.y);
	}
	/**
	 * Set the sight as visible or invisible
	 * @param value: true to view, false to hide
	 * */
	public void drawSight(boolean value){
		drawSight=value;
	}
	/**
	 * Draw the sight
	 * */
	private void drawSight(){
		parent.noFill();
		parent.strokeWeight(2);
		parent.stroke(100, 255, 0, alpha);
		parent.ellipse(halfWidth,halfHeight,30,30);
		parent.line(halfWidth-25,halfHeight,halfWidth+25,halfHeight);
		parent.line(halfWidth,halfHeight-25,halfWidth,halfHeight+25);
	}
	/**
	 * Set the Depth Image as visible or invisible
	 * @param value: true to view, false to hide
	 * */
	public void drawDepthImage(boolean value){
		drawDepthImage=value;
	}
	/**
	 * Draw the Depth Image
	 * */
	private void drawDepthImage(PImage depthImage,float withImg,float heightImg){
		parent.image(depthImage,10,0,withImg/3,heightImg/3);
	}
	/**
	 * Set the RGB Image as visible or invisible
	 * @param value: true to view, false to hide
	 * */
	public void drawRGBImage(boolean value){
		drawRGBImage=value;
	}
	/**
	 * Draw the RGB Image
	 * */
	private void drawRGBImage(PImage rgbImage,float withImg,float heightImg){
		parent.image(rgbImage,10,0,withImg/3,heightImg/3);
	}
}

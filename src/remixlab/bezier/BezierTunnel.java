package remixlab.bezier;

import java.awt.Color;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PVector;
import remixlab.proscene.*;

/**
 * A BezierTunnel is a tunnel build with one or more BezierCurves
 * */
public class BezierTunnel {
	PApplet parent;
	Scene scene;
	PVector ini;	
	PVector fin;
	int nParts;
	int detail;
	float radius;
	ArrayList<BezierCurve> curves = new ArrayList<BezierCurve>();
	
	/**
	 * BezierTunnel Constructor
	 * @param p: PApplet
	 * @param sc: Scene
	 * */
	public BezierTunnel(PApplet p, Scene sc,int cantParts,int det,float radi) {
		parent = p;
		scene = sc;
		radius = 10;
		ini = new PVector(radius,radius,radius);
		fin = new PVector(-(2*radius),-(2*radius),-(2*radius));
		nParts=cantParts;
		detail=det;
		radius=radi;
	}
	/**
	 * BezierTunnel Constructor
	 * @param p: PApplet
	 * @param sc: Scene
	 * @param tRadius: radius of tunnel
	 * */
	public BezierTunnel(PApplet p, Scene sc, float tRadius,int cantParts,int det,float radi) {
		parent = p;
		scene = sc;
		radius = tRadius;
		ini = new PVector(radius,radius,radius);
		fin = new PVector(-(2*radius),-(2*radius),-(2*radius));
		nParts=cantParts;
		detail=det;
		radius=radi;
	}
	/**
	 * BezierTunnel Constructor
	 * @param p: PApplet
	 * @param sc: Scene
	 * @param vIni: Normal vector to first plane of tunnel
	 * @param tRadius: radius of tunnel
	 * */
	public BezierTunnel(PApplet p, Scene sc, PVector vIni, float tRadius,int cantParts,int det,float radi) {
		parent = p;
		scene = sc;
		ini = vIni;
		radius = tRadius;
		fin = new PVector(-(2*radius),-(2*radius),-(2*radius));
		nParts=cantParts;
		detail=det;
		radius=radi;
	}
	/**
	 * BezierTunnel Constructor
	 * @param p: PApplet
	 * @param sc: Scene
	 * @param vIni: Normal vector to first plane of tunnel
	 * @param vFin: Normal vector to last plane of tunnel
	 * @param tRadius: radius of tunnel
	 * */
	public BezierTunnel(PApplet p, Scene sc, PVector vIni, PVector vFin, float tRadius,int cantParts,int det,float radi) {
		parent = p;
		scene = sc;
		ini = vIni;
		fin = vFin;
		radius = tRadius;
		nParts=cantParts;
		detail=det;
		radius=radi;
	}

	/**
	 * Adds a BezierCurve to the tunnel
	 * @param curve: BezierCurve to add to the tunnel
	 * */
	public void addCurve(PVector anchor,PVector control,Color color){
		BezierCurve curve=new BezierCurve(parent,scene,anchor,control,color,nParts,detail,radius);
		//Connect to the tunnel
		connect(curve);
	}
	
	/**
	 * Connect the curve with the previous curve and add to the tunnel
	 * @param curve to connect to the previous curve
	 * */
	private void connect(BezierCurve curve){
		if(curves.size()==0){
			curve.ini=ini;
		}else{
			BezierCurve prevCurve=curves.get(curves.size()-1);
			//Connect this curve with the previous
			curve.connect(prevCurve);
		}
		//Add the curve to the tunnel
		curves.add(curve);
		//Recalculate curves
		for(int i=0;i<curves.size();i++){
			curves.get(i).calculate();
		}
	}
	
	/**
	 * Draws the tunnel around the Bezier Path, defined by BezierCurve array
	 * */
	public void draw() {
//		drawBezierPoints();
//		drawBezierPath();
		drawBezierTunnel();
	}
	
	/**
	 * Draws the Tunnel around each part of each BezierCurve
	 * */
	public void drawBezierTunnel() {
		for(int i=0;i<curves.size();i++){
			BezierCurve curve=curves.get(i);
			for(int j=0;j<curve.parts.size();j++){
				BezierPart part=curve.parts.get(j);
				//Variables to calculate normals between this and nextPart
				BezierPart nextPart;
				if(j==curve.parts.size()-1){
					if(i==curves.size()-1){
						nextPart=part;
					}else{
						nextPart=curves.get(i+1).parts.get(0);
					}
				}else{
					nextPart=curve.parts.get(j+1);
				}
				//Pass the next part to calculate the normals between them
				part.draw(nextPart);
			}
		}
	}
	
	/**
	 * Draws a path with the BezierCurves in 'curves' array
	 * */
	public void drawBezierPath() {
		parent.noFill();
		// Draw the curves.length-1 Bezier Lines
		for (int i = 0; i < curves.size(); i++) {
			drawCurve(curves.get(i));
		}
	}
	
	/**
	 * Draws points of each curve, getting of Bezier formula
	 * */
	public void drawBezierPoints() {
		for (int i = 0; i < curves.size(); i++) {
			BezierCurve curve=curves.get(i);
			float t = (1 / (float) curve.nParts);
			for (int j = 0; j < curve.nParts; j++) {
				PVector v = curve.getPoint(j * t);
				parent.strokeWeight(3);
				parent.stroke(curve.color.getRGB());
				parent.point(v.x, v.y, v.z);
				parent.strokeWeight(1);
			}
		}
	}
	
	/**
	 * Draws a Bezier curve
	 * @param BezierCurve b
	 * */
	private void drawCurve(BezierCurve curve) {
		parent.stroke(curve.color.getRGB());
		parent.bezier(
				curve.ini.x, curve.ini.y, curve.ini.z, 
				curve.ctrl1.x, curve.ctrl1.y, curve.ctrl1.z,
				curve.ctrl2.x, curve.ctrl2.y, curve.ctrl2.z,
				curve.fin.x, curve.fin.y, curve.fin.z);
	}
}

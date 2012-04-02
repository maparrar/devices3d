/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Modification of the Hands example of the library SimpleOpenNI, see (http://code.google.com/p/simple-openni/)
 * -------------
 * --------------------------------------------------------------------------
 * SimpleOpenNI NITE Hands
 * --------------------------------------------------------------------------
 * Processing Wrapper for the OpenNI/Kinect library
 * http://code.google.com/p/simple-openni
 * --------------------------------------------------------------------------
 * prog:  Max Rheiner / Interaction Design / zhdk / http://iad.zhdk.ch/
 * date:  03/19/2011 (m/d/y)
 * ----------------------------------------------------------------------------
 * This example works with multiple hands, to enable mutliple hand change
 * the ini file in /usr/etc/primesense/XnVHandGenerator/Nite.ini:
 *  [HandTrackerManager]
 *  AllowMultipleHands=1
 *  TrackAdditionalHands=1
 * on Windows you can find the file at:
 *  C:\Program Files (x86)\Prime Sense\NITE\Hands\Data\Nite.ini
 * ----------------------------------------------------------------------------
 * */

package remixlab.devices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;
import SimpleOpenNI.XnVHandPointContext;
import SimpleOpenNI.XnVPointControl;

public class Kinect extends XnVPointControl {
	PApplet parent;
	HashMap _pointLists;
	int _maxPoints;
	// color[] _colorList = {
	// color(255,0,0),color(0,255,0),color(0,0,255),color(255,255,0)};

	PVector auxiliar; // Vector temporal para almacenar el punto anterior de una
						// mano (solo para pruebas)

	SimpleOpenNI context;

	/**
	 * Kinect Constructor
	 * 
	 * @param p
	 *            : PApplet parent
	 * */
	public Kinect(PApplet p, SimpleOpenNI cont) {
		parent = p;
		context = cont;

		_maxPoints = 30;
		_pointLists = new HashMap();

		auxiliar = new PVector();

	}

	public void OnPointCreate(XnVHandPointContext cxt) {
		// create a new list
		addPoint(cxt.getNID(), new PVector(cxt.getPtPosition().getX(), cxt
				.getPtPosition().getY(), cxt.getPtPosition().getZ()));

		PApplet.println("OnPointCreate, handId: " + cxt.getNID());
	}

	public void OnPointUpdate(XnVHandPointContext cxt) {
		
		
		addPoint(cxt.getNID(), new PVector(cxt.getPtPosition().getX(), cxt
				.getPtPosition().getY(), cxt.getPtPosition().getZ()));

		PVector vector = new PVector(cxt.getPtPosition().getX(), cxt
				.getPtPosition().getY(), cxt.getPtPosition().getZ());
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
		if (Math.abs(auxiliar.z - vector.z) > 50) {
			PApplet.println("hand: " + cxt.getNID() + " -> click");
		}
		// Mover a la derecha
		if ((vector.x - auxiliar.x) > 50) {
			PApplet.println("hand: " + cxt.getNID() + " -> derecha");
		}
		// Mover hacia arriba
		if ((vector.y - auxiliar.y) > 50) {
			PApplet.println("hand: " + cxt.getNID() + " -> arriba");
		}

		auxiliar = vector;

	}

	public void OnPointDestroy(long nID) {
		PApplet.println("OnPointDestroy, handId: " + nID);

		// remove list
		if (_pointLists.containsKey(nID))
			_pointLists.remove(nID);
	}

	public ArrayList getPointList(long handId) {
		ArrayList curList;
		if (_pointLists.containsKey(handId))
			curList = (ArrayList) _pointLists.get(handId);
		else {
			curList = new ArrayList(_maxPoints);
			_pointLists.put(handId, curList);
		}
		return curList;
	}

	public void addPoint(long handId, PVector handPoint) {
		ArrayList curList = getPointList(handId);

		curList.add(0, handPoint);
		if (curList.size() > _maxPoints)
			curList.remove(curList.size() - 1);
	}

	/**
	 * Draw lines between the list of the n last points of each hand
	 * */
	public void draw() {
		if (_pointLists.size() <= 0) {
			return;
		}
		parent.pushStyle();
		parent.noFill();
		PVector vec;
		PVector firstVec;
		PVector screenPos = new PVector();
		// draw the hand lists of points
		Iterator<Map.Entry> itrList = _pointLists.entrySet().iterator();
		while (itrList.hasNext()) {
			parent.strokeWeight(2);
			parent.stroke(200, 100, 0);
			ArrayList curList = (ArrayList) itrList.next().getValue();
			// draw line
			firstVec = null;
			Iterator<PVector> itr = curList.iterator();
			parent.beginShape();
			while (itr.hasNext()) {
				vec = itr.next();
				if (firstVec == null) {
					firstVec = vec;
				}
				// calc the screen pos
				context.convertRealWorldToProjective(vec, screenPos);
				parent.vertex(screenPos.x, screenPos.y);
			}
			parent.endShape();
			// draw current pos of the hand
			if (firstVec != null) {
				parent.strokeWeight(8);
				context.convertRealWorldToProjective(firstVec, screenPos);
				parent.point(screenPos.x, screenPos.y);
			}
		}
		parent.popStyle();
	}
}

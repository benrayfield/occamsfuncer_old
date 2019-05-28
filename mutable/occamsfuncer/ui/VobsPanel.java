package mutable.occamsfuncer.ui;
import static immutable.occamsfuncer.ImportStatic.*;
import immutable.occamsfuncer.Funcer;
import mutable.util.Rand;
import mutable.util.ScreenUtil;
import mutable.util.Var;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

import javax.swing.JFrame;
import javax.swing.JPanel;

/** vob as in immutable.occamsfuncer.Vob, an occamsfuncer Visual OBject.
This is occamsfuncer's default kind of graphics.
The Var<Funcer> has value of list of vobs. List is Funcer. Vob is Funcer.
See the Vob enum for that datastruct.
*/
public class VobsPanel extends JPanel{
	
	/** when this changes, displays it */
	public final Var<Funcer> varDisplay;
	
	protected final Consumer<Var> listener;
	
	public VobsPanel(Var<Funcer> varDisplay){
		this.varDisplay = varDisplay;
		listener = (Var v)->{ repaint(); };
		varDisplay.startListening(listener);
	}
	
	public void paint(Graphics g){
		paintVobs((Graphics2D)g, varDisplay.get());
	}
	
	protected void finalize() throws Throwable{
		varDisplay.stopListening(listener);
	}
	
	/** TODO cache BufferedImage per int[] but somehow reusable with different aftrans.
	Overwrites the AffineTransform by Graphics2D.setTransform.
	*/
	public static void paintVobs(Graphics2D g, Funcer listOfVob){
		for(long i=0; i<listOfVob.maplistSize(); i++){
			paintVob(g, listOfVob.get(i));
		}
	}
	
	/** Paints 1 vob. Overwrites the AffineTransform by Graphics2D.setTransform. */
	public static void paintVob(Graphics2D g, Funcer vob){
		Funcer type = vob.get("type");
		//TODO cache these calls of wr in static vars, not just cache in the map as usual.
		if(type.equals(wr("rectVob2d"))){
			int colorARGB = vob.get("colorARGB").i();
			AffineTransform aftrans = funcerToAffineTransform(vob.get("aftrans3x3"));
			g.setTransform(aftrans);
			g.setColor(new Color(colorARGB,true));
			g.drawRect(0, 0, 1, 1);
		}else if(type.equals(wr("linesVob2d"))){
			throw new Error("TODO");
		}
		//if(type.equals(wr("linesVob2d")){
		//	TODO
		//}
		//FIXME else other types
	}
	
	/** Example: a(3 4 5 6 7 8 0 0 0) or todo which 3 indexs are supposed to be
	the 0s... to java.awt.geom.AffineTransform.AffineTransform.
	TODO If it doesnt fit, returns identity aftrans.
	*/
	public static AffineTransform funcerToAffineTransform(Funcer aftrans){
		throw new Error("Can it convert 9 nonzero nums to 6 nums 1way?");
	}
	
	/** test the graphics in a window that pops up */
	public static void main(String... args){
		int[] voxels = new int[10];
		for(int i=0; i<voxels.length; i++){
			voxels[i] = Rand.strongRand.nextInt();
		}
		for(int i=1; i<voxels.length-2; i+=2){
			voxels[i] = voxels[i+1]; //chain of lines
		}
		Funcer vob = emptyMap
			.put("bitsPerColorDim", 4)
			.put("visColorDims", 3)
			.put("bitsAlpha", 0)
			.put("bitsPerYOrX", 10)
			.put("voxels", wr(voxels));
		Funcer vobs = emptyList.push(vob);
		VobsPanel p = new VobsPanel(new Var(vobs));
		ScreenUtil.testDisplayWithSysExit(p);
	}

}

/** Ben F Rayfield offers this software opensource MIT license */
package immutable.abstracttensordb;
import static immutable.abstracttensordb.Tensor.wrap;
import static mutable.util.Lg.*;

import java.util.function.DoubleSupplier;

import immutable.util.MathUtil;
import mutable.util.Rand;

public class Test{
	
	public static Object array(DoubleSupplier d, int... dims){
		if(dims.length == 0){
			return d.getAsDouble();
		}else if(dims.length == 1){
			int sizeA = dims[0];
			double[] ret = new double[sizeA];
			for(int i=0; i<sizeA; i++){
				ret[i] = d.getAsDouble();
			}
			return ret;
		}else if(dims.length == 2){
			int sizeA = dims[0], sizeB = dims[1];
			double[][] ret = new double[sizeA][sizeB];
			for(int i=0; i<sizeA; i++){
				//better than calling array(d,1d) cuz new double[][] is normally solid block in memory
				for(int j=0; j<sizeB; j++){
					ret[i][j] = d.getAsDouble();
				}
			}
			return ret;
		}else if(dims.length == 3){
			int sizeA = dims[0], sizeB = dims[1], sizeC = dims[2];
			double[][][] ret = new double[sizeA][sizeB][sizeC];
			for(int i=0; i<sizeA; i++){
				//better than calling array(d,2d) cuz new double[][][] is normally solid block in memory
				for(int j=0; j<sizeB; j++){
					for(int k=0; k<sizeC; k++){
						ret[i][j][k] = d.getAsDouble();
					}
				}
			}
			return ret;
		}
		throw new Error("TODO");
	}
	
	/** TODO write a combo of Tensor ops that generates this opencl code and the code to interface with java,
	this code being from OpenclProgs.java in HumanAiNet.
	public static final String matmulCode1dAs2dThenSigmoidThenWeightedCoinFlip =
		"kernel void "+OpenclUtil.newKernelName()+"(global const float* bias, int const bSize,
		int const cSize, int const dSize, global const float* bc, global const float* cd, global float* bdOut){\r\n"+
		"	int bd = get_global_id(0);\r\n"+
		"	const int b = bd/dSize;\r\n"+ //TODO optimize allow get_global_id(more dims)?//
		"	const int d = bd%dSize;\r\n"+ //TODO optimize allow get_global_id(more dims)?
		"	float sum = bias[bd];\r\n"+
		"	for(int c=0; c<cSize; c++){\r\n"+
		"		sum += bc[b*cSize+c]*cd[c*dSize+d];\r\n"+ //TODO optimize allow get_global_id(more dims)?
		"	}\r\n"+
		"	float chance = 1/(1+exp(-sum));\r\n"+
		"	float randFraction = fmod(fabs(sum)*49999,1);\r\n"+
		"	float weightedCoinFlip = fmax(0,ceil(chance-randFraction));\r\n"+
		"	bdOut[bd] = weightedCoinFlip;\r\n"+
		"}";
	*/
	public static void testMatmulCode1dAs2dThenSigmoidThenWeightedCoinFlip(){
		int sizeX = 50, sizeY = 30, sizeZ = 70;
		DoubleSupplier randGauss = ()->Rand.strongRand.nextGaussian();
		double[][] _XY = (double[][])array(randGauss, sizeX, sizeY);
		double[][] _YZ = (double[][])array(randGauss, sizeY, sizeZ);
		double[][] _XZ = new double[sizeX][sizeZ]; //correct multiply of XY by YZ
		for(int x=0; x<sizeX; x++){
			for(int z=0; z<sizeZ; z++){ 
				double sum = 0;
				for(int y=0; y<sizeY; y++){
					sum += _XY[x][y]*_YZ[y][z];
				}
				_XZ[x][z] = sum;
			}
		}
		//Lowercase means its expected not to exist in memory, just as an abstraction.
		Tensor XY = wrap(_XY);
		Tensor YZ = wrap(_YZ);
		
		
		Tensor XYz = XY.pushDim(sizeZ);
		Tensor YZx = YZ.pushDim(sizeX);
		Tensor xYZ = YZx.permuDims(2,0,1);
		Tensor XyZMult = XYz.bin("*",xYZ);
		Tensor XZyMult = XyZMult.permuDims(0,2,1);
		Tensor XZ = XZyMult.popDim("+");
		
		
		double[][] aXZ = (double[][]) XZ.array();
		for(int x=0; x<sizeX; x++){
			for(int z=0; z<sizeZ; z++){
				double diff = Math.abs(_XZ[x][z]-aXZ[x][z]);
				if(diff > .000001) throw new Error("Failed matrix multiply part of testMatmulCode1dAs2dThenSigmoidThenWeightedCoinFlip test, diff="+diff);
			}
		}
		//float chance = 1/(1+exp(-sum));
		Tensor XZchance = XZ.una("-").una("exp").bin("+",XZ.fill(1.)).una("/");
		//float randFraction = fmod(fabs(sum)*49999,1);
		Tensor randFraction = XZ.una("abs").bin("*",XZ.fill(49999.)).bin("%",XZ.fill(1.));
		//float weightedCoinFlip = fmax(0,ceil(chance-randFraction));
		Tensor weightedCoinFlip = XZchance.bin("+",randFraction.una("-")).bin("max",XZ.fill(0.));
		double[][] _weightedCoinFlip = (double[][]) weightedCoinFlip.array();
		for(int x=0; x<sizeX; x++){
			for(int z=0; z<sizeZ; z++){
				double correct_sum = _XY[x][z];
				double correct_chance = 1/(1+Math.exp(-correct_sum));
				double correct_randFraction = (Math.abs(correct_sum)*49999)%1;
				double correct_weightedCoinFlip = Math.max(0,Math.ceil(correct_chance-correct_randFraction));
				double diff =  Math.abs(correct_weightedCoinFlip-_weightedCoinFlip[x][z]);
				if(diff > .000001) throw new Error("Failed later part of testMatmulCode1dAs2dThenSigmoidThenWeightedCoinFlip test, diff="+diff);
			}
		}
		lg("testMatmulCode1dAs2dThenSigmoidThenWeightedCoinFlip test passes");
	}
	
	/** TODO i want a Tensor.convolve op for doing cellularAutomata
	and maybe more complex kinds of convolution that have more than 1 output index,
	other than the tensor which may be at each index.
	This op enforces bounds such as a size30 dim in a size50 dim convolves a size 21 dim.
	I want it to work with any small number of dims, hopefully convolving 1 at a time (abstractly).
	For example, write conwaysGameOfLife in this thing as a 3x3 convolution to sum
	then subtract just the center cell to get the few rules to update it.
	Conwaylife isnt much optimized by GPU vs cpu cuz its convolution window
	is so small (plus theres hashlife that makes CPU much faster for it)
	but its a good testcase for Tensor.convolve op.
	*/
	public static void testConvolveConwaylife(){
		int sizeX = 50, sizeY = 30;
		double[][] _XY = (double[][])array( //input to conwaylife
			()->MathUtil.weightedCoinFlip(.3,Rand.strongRand)?1:0,
			sizeX,
			sizeY);
		double[][] _correctNextCellOn = new double[sizeX][sizeY];
		for(int x=1; x<sizeX-1; x++){ //cells at edges are always off regardless of normal conway rules
			for(int y=0; y<sizeY-1; y++){
				double adjacents =
					_XY[x-1][y-1]+_XY[x][y-1]+_XY[x+1][y-1]+
					_XY[x-1][y  ]+            _XY[x+1][y  ]+
					_XY[x-1][y+1]+_XY[x][y+1]+_XY[x+1][y+1];
				_correctNextCellOn[x][y] = (adjacents==3 || (adjacents==2 && _XY[x][y]==1))?1:0; 
			}
		}
		Tensor XY = wrap(_XY);
		Tensor scalarZero = wrap(0.);
		//Tensor zeros3x3 = scalarZero.pushDim(3).pushDim(3);
		//XY.convolve(0,zeros3x3) FIXME what does this mean? Which dims?
		Tensor convolve4d = XY.conv(0,3).conv(1,3); //[x0][y0][x1][y1]==XY[x0+x1][y0+y1]
		
		//Simpler example:
		//Tensor X = ...;
		//Tensor convolve2d = X.conv(0,3); //[x0][x1]==X[x0+x1]. x0 in (0..sizeX-3). x1 in (0..2).
		//Tensor sumThreeAdjacents = convolve2d.popDim("+"); //index i sums values at indexs (i..i+2)
		
		Tensor sumNineAdjacents = convolve4d.popDim("+").popDim("+");
		Tensor XYBut1SmallerOnEach2dEdge = XY.suf(0,1).pre(0,sizeX-2).suf(1,1).pre(1,sizeY-2);
		Tensor sumEightAdjacents = sumNineAdjacents.bin("+",XYBut1SmallerOnEach2dEdge.una("-"));
		
		
		//Next cell is on if 3@sumEightAdjacents or if [2@sumEightAdjacents AND XY].
		//Use sumEightAdjacents and XYBut1SmallerOnEach2dEdge, which are all 1 smaller at each 2d edge.
		
		Tensor theres3Adjacents = sumEightAdjacents.bin("==",sumEightAdjacents.fill(3)); //vals are 0 or 1
		Tensor theres2Adjacents = sumEightAdjacents.bin("==",sumEightAdjacents.fill(2)); //vals are 0 or 1
		Tensor theres2AdjacentsAndCenterWasOn = theres2Adjacents.bin("*",XYBut1SmallerOnEach2dEdge); //0 or 1
		Tensor nextCellOn_but1SmallerOnEach2dEdge = theres3Adjacents.bin("*",theres2AdjacentsAndCenterWasOn);
				
		//same size as XY
		Tensor xxxx = wrap(0.).pushDim(sizeX-2);
		Tensor yyyyyy = wrap(0.).pushDim(sizeY);
		//FIXME which dim indexs?
		Tensor nextCellOn =
			yyyyyy
			.cat(1,xxxx.cat(0,nextCellOn_but1SmallerOnEach2dEdge).cat(0,xxxx))
			.cat(1,yyyyyy);
		double[][] _nextCellOn = (double[][])nextCellOn.array();
		//compare nextCellOn to _correctNextCellOn
		for(int x=0; x<sizeX; x++){
			for(int y=0; y<sizeY; y++){
				double diff = Math.abs(_nextCellOn[x][y]-_correctNextCellOn[x][y]);
				if(diff > .000001){
					throw new Error("Failed testConvolveConwaylife test, diff="+diff);
				}
			}
		}
		lg("testConvolveConwaylife test passes");
	}
	
	/*wavenetliketopology? How about a weaker form like acyclicFlow in a 1d array
	where lower index affect higher index and include a 2d matrix op that reads from
	a dense range of lower index and writes (once idempotently) into a dense range of higher indexs
	so wavenettopology could be done using 3 times the array size
	(cuz calls this op twice then sums into a third place).
			
	sparsedoppler?
	
	2dGridElectricSimMusicTool?*/

}

package hageldave.ezfftw.dp;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.BTest.lambdatest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;

import static hageldave.ezfftw.JunitUtils.doubleTolerance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import hageldave.ezfftw.FFTW_Initializer;

/* --- DOUBLE PRECISION VERSION --- */
public class FFTTest {

	@Test
	public void test_r2c2r() {
		double[] realIn = new double[64];
		// generate sine wave with +1 y offset
		double to2pi = ((double)(Math.PI*2/realIn.length));
		for(int i = 0; i < realIn.length; i++){
			realIn[i] = (double)Math.sin(i*to2pi)+1;
		}

		double[] realOut = new double[realIn.length];
		double[] imagOut = new double[realIn.length];
		double[] realInToCheck = new double[realIn.length];

		{// array arguments
			FFT.fft(realIn, realOut, imagOut, realIn.length);

			// sum of sinus values has to be zero, but due to y offset by 1 is 1*length. (DC check)
			assertEquals(0, imagOut[0], 0);
			assertEquals(realIn.length, realOut[0], doubleTolerance);
			// for sinus the imaginary part has to show frequency != 0 for 1hz
			// also real part has to zero except for 0hz
			for(int i = 0; i < realIn.length; i++){
				if( i == 1 || i == realIn.length-1 ){
					assertNotEquals(0, imagOut[i], 1);
				} else {
					assertEquals(0, imagOut[i], doubleTolerance);
				}
				if(i != 0){
					assertEquals(0, realOut[i], doubleTolerance);
				}
			}

			// back transform
			FFT.ifft(realOut, imagOut, realInToCheck, realIn.length);

			// should restore original but scaled by number of elements
			for(int i = 0; i < realIn.length; i++){
				assertEquals(realIn[i]*realIn.length, realInToCheck[i], doubleTolerance);
			}
		}
		
		Arrays.fill(realInToCheck, 0);
		Arrays.fill(realOut, 0);
		Arrays.fill(imagOut, 0);
		
		{// sampler/writer arguments
			
			FFT.fft(	new RowMajorArrayAccessor(realIn, realIn.length), 
						new RowMajorArrayAccessor(realOut, realIn.length)
						.combineToComplexWriter( 
						new RowMajorArrayAccessor(imagOut, realIn.length)), 
						realIn.length);

			// sum of sinus values has to be zero, but due to y offset by 1 is 1*length. (DC check)
			assertEquals(0, imagOut[0], 0);
			assertEquals(realIn.length, realOut[0], doubleTolerance);
			// for sinus the imaginary part has to show frequency != 0 for 1hz
			// also real part has to zero except for 0hz
			for(int i = 0; i < realIn.length; i++){
				if( i == 1 || i == realIn.length-1 ){
					assertNotEquals(0, imagOut[i], 1);
				} else {
					assertEquals(0, imagOut[i], doubleTolerance);
				}
				if(i != 0){
					assertEquals(0, realOut[i], doubleTolerance);
				}
			}

			// back transform
			FFT.ifft(	new RowMajorArrayAccessor(realOut, realIn.length)
						.combineToComplexSampler(
						new RowMajorArrayAccessor(imagOut, realIn.length)), 
						new RowMajorArrayAccessor(realInToCheck, realIn.length), 
						realIn.length);

			// should restore original but scaled by number of elements
			for(int i = 0; i < realIn.length; i++){
				assertEquals(realIn[i]*realIn.length, realInToCheck[i], doubleTolerance);
			}
		}

	}

	
	@Test
	public void test_c2c() {
		double[] realIn = new double[64];
		double[] imagIn = new double[64];
		// generate sine wave with +1 y offset
		double to2pi = ((double)(Math.PI*2/realIn.length));
		for(int i = 0; i < realIn.length; i++){
			realIn[i] = (double)Math.sin(i*to2pi)+1;
		}

		double[] realOut = new double[realIn.length];
		double[] imagOut = new double[realIn.length];
		double[] realInToCheck = new double[realIn.length];
		double[] imagInToCheck = new double[imagIn.length];

		{// array arguments
			FFT.fft(realIn, imagIn, realOut, imagOut, realIn.length);

			// sum of sinus values has to be zero, but due to y offset by 1 is 1*length. (DC check)
			assertEquals(0, imagOut[0], 0);
			assertEquals(realIn.length, realOut[0], doubleTolerance);
			// for sinus the imaginary part has to show frequency != 0 for 1hz
			// also real part has to zero except for 0hz
			for(int i = 0; i < realIn.length; i++){
				if( i == 1 || i == realIn.length-1 ){
					assertNotEquals(0, imagOut[i], 1);
				} else {
					assertEquals(0, imagOut[i], doubleTolerance);
				}
				if(i != 0){
					assertEquals(0, realOut[i], doubleTolerance);
				}
			}

			// back transform
			FFT.ifft(realOut, imagOut, realInToCheck, imagInToCheck, realIn.length);

			// should restore original but scaled by number of elements
			for(int i = 0; i < realIn.length; i++){
				assertEquals(realIn[i]*realIn.length, realInToCheck[i], doubleTolerance);
				assertEquals(imagIn[i]*realIn.length, imagInToCheck[i], doubleTolerance);
			}
		}
		
		Arrays.fill(realInToCheck, 0);
		Arrays.fill(imagInToCheck, 0);
		Arrays.fill(realOut, 0);
		Arrays.fill(imagOut, 0);
		
		{// sampler/writer arguments
			
			FFT.fft(	new RowMajorArrayAccessor(realIn, realIn.length)
						.combineToComplexSampler(
						new RowMajorArrayAccessor(imagIn, realIn.length)),
						
						new RowMajorArrayAccessor(realOut, realIn.length)
						.combineToComplexWriter( 
						new RowMajorArrayAccessor(imagOut, realIn.length)),
						
						realIn.length);

			// sum of sinus values has to be zero, but due to y offset by 1 is 1*length. (DC check)
			assertEquals(0, imagOut[0], 0);
			assertEquals(realIn.length, realOut[0], doubleTolerance);
			// for sinus the imaginary part has to show frequency != 0 for 1hz
			// also real part has to zero except for 0hz
			for(int i = 0; i < realIn.length; i++){
				if( i == 1 || i == realIn.length-1 ){
					assertNotEquals(0, imagOut[i], 1);
				} else {
					assertEquals(0, imagOut[i], doubleTolerance);
				}
				if(i != 0){
					assertEquals(0, realOut[i], doubleTolerance);
				}
			}

			// back transform
			FFT.ifft(	new RowMajorArrayAccessor(realOut, realIn.length)
						.combineToComplexSampler(
						new RowMajorArrayAccessor(imagOut, realIn.length)),
						
						new RowMajorArrayAccessor(realInToCheck, realIn.length)
						.combineToComplexWriter(
						new RowMajorArrayAccessor(imagInToCheck, realIn.length)), 
						
						realIn.length);

			// should restore original but scaled by number of elements
			for(int i = 0; i < realIn.length; i++){
				assertEquals(realIn[i]*realIn.length, realInToCheck[i], doubleTolerance);
				assertEquals(imagIn[i]*realIn.length, imagInToCheck[i], doubleTolerance);
			}
		}

	}
	
	@Test
	public void concurrentInvocations() {
		FFTW_Initializer.initFFTW();
		final int numThreads = 32;
		for(int j = 0; j < 500; j++){
		final CountDownLatch latch = new CountDownLatch(numThreads);
		LinkedList<Thread> threads = new LinkedList<>();
		for(int i=0; i<numThreads; i++){
			// BLOCKTEST EVAL: https://github.com/hageldave/ezFFTW/blob/20d97041dfde6731bbfc02980cf2b581c62161ec/src/test/java/hageldave/ezfftw/dp/FFTTest.java#L208-L221
			lambdatest().given(latch, new CountDownLatch(1));

			Thread t = new Thread(()->{
				try {
					blocktest().given(latch, new CountDownLatch(1)).end(FIRST_BLOCK).checkEq("arrays[2][1027]", 0, 0.001).checkEq("arrays.length", 3).checkEq("arrays[0].length", 1028);
					int size = 1028;
					double[][] arrays = new double[3][size];
					latch.countDown();
					latch.await();
					FFT.fft(arrays[0], arrays[1], arrays[2], size);
					FFT.ifft(arrays[1], arrays[2], arrays[0], size);
					FFT.fft(arrays[0], arrays[1], arrays[2], size);
					FFT.ifft(arrays[1], arrays[2], arrays[0], size);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			threads.add(t);
			t.start();
		}
		for(Thread t: threads){
			try {
				t.join(8000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		}
	}
	
}

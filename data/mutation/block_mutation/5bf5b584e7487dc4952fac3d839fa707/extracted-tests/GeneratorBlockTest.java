package scengen.application;

import java.util.HashMap;
import java.util.Map;
import scengen.distribution.MultivariateDistribution;
import scengen.distribution.MultivariateLognormal;
import scengen.distribution.MultivariateNormal;
import scengen.distribution.MultivariateStudent;
import scengen.distribution.MultivariateUniform;
import scengen.methods.METHOD;
import scengen.methods.MomentMatching;
import scengen.methods.MonteCarlo;
import scengen.methods.QuantizationGrids;
import scengen.methods.QuantizationLearning;
import scengen.methods.QuasiMonteCarlo;
import scengen.methods.ReductionMethod;
import scengen.methods.Scenred2;
import scengen.methods.VoronoiCellSampling;
import scengen.tools.Xorshift;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static scengen.application.Generator.*;

public class GeneratorBlockTest {

    @Test(expected = IllegalArgumentException.class)
    public void testA() throws Exception {
        int dim = 3;
        String[] scorrel = new String[] { "1", "2", "1", "1", "1", "1", "1", "1", "1" };
        double[][] correl = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                correl[i][j] = Double.parseDouble(scorrel[j + i * dim]);
                if (correl[i][j] > 1 || correl[i][j] < -1)
                    throw new IllegalArgumentException("Correlation matrix for uniform distribution invalid.");
            }
            if (correl[i][i] != 1)
                throw new IllegalArgumentException("");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testB() throws Exception {
        int dim = 3;
        String[] scorrel = new String[] { "1", "1", "2", "1", "1", "1", "1", "1", "1" };
        double[][] correl = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                correl[i][j] = Double.parseDouble(scorrel[j + i * dim]);
                if (correl[i][j] > 1 || correl[i][j] < -1)
                    throw new IllegalArgumentException("Correlation matrix for uniform distribution invalid.");
            }
            if (correl[i][i] != 1)
                throw new IllegalArgumentException("");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testC() throws Exception {
        int dim = 3;
        String[] scorrel = new String[] { "1", "1", "1", "-2", "1", "1", "1", "1", "1" };
        double[][] correl = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                correl[i][j] = Double.parseDouble(scorrel[j + i * dim]);
                if (correl[i][j] > 1 || correl[i][j] < -1)
                    throw new IllegalArgumentException("Correlation matrix for uniform distribution invalid.");
            }
            if (correl[i][i] != 1)
                throw new IllegalArgumentException("");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testD() throws Exception {
        int dim = 3;
        String[] scorrel = new String[] { "-1", "1", "1", "1", "1", "1", "1", "1", "1" };
        double[][] correl = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                correl[i][j] = Double.parseDouble(scorrel[j + i * dim]);
                if (correl[i][j] > 1 || correl[i][j] < -1)
                    throw new IllegalArgumentException("Correlation matrix for uniform distribution invalid.");
            }
            if (correl[i][i] != 1)
                throw new IllegalArgumentException("");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testE() throws Exception {
        int dim = 3;
        String[] scorrel = new String[] { "1", "1", "1", "1", "-1", "1", "1", "1", "1" };
        double[][] correl = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                correl[i][j] = Double.parseDouble(scorrel[j + i * dim]);
                if (correl[i][j] > 1 || correl[i][j] < -1)
                    throw new IllegalArgumentException("Correlation matrix for uniform distribution invalid.");
            }
            if (correl[i][i] != 1)
                throw new IllegalArgumentException("");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testF() throws Exception {
        int dim = 3;
        String[] scorrel = new String[] { "1", "1", "1", "1", "1", "1", "1", "1", "-1" };
        double[][] correl = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                correl[i][j] = Double.parseDouble(scorrel[j + i * dim]);
                if (correl[i][j] > 1 || correl[i][j] < -1)
                    throw new IllegalArgumentException("Correlation matrix for uniform distribution invalid.");
            }
            if (correl[i][i] != 1)
                throw new IllegalArgumentException("");
        }
    }

    @Test
    public void testG() throws Exception {
        int dim = 3;
        String[] scorrel = new String[] { "1", "1", "1", "1", "1", "1", "1", "-1", "1" };
        double[][] correl = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                correl[i][j] = Double.parseDouble(scorrel[j + i * dim]);
                if (correl[i][j] > 1 || correl[i][j] < -1)
                    throw new IllegalArgumentException("Correlation matrix for uniform distribution invalid.");
            }
            if (correl[i][i] != 1)
                throw new IllegalArgumentException("");
        }
        assertEquals(-1, correl[2][1], 0.1);
    }

    @Test
    public void testH() throws Exception {
        int dim = 3;
        String[] scorrel = new String[] { "1", "0.5", "-0.2", "-1", "1", "0.99", "-0.3", "1", "1" };
        double[][] correl = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                correl[i][j] = Double.parseDouble(scorrel[j + i * dim]);
                if (correl[i][j] > 1 || correl[i][j] < -1)
                    throw new IllegalArgumentException("Correlation matrix for uniform distribution invalid.");
            }
            if (correl[i][i] != 1)
                throw new IllegalArgumentException("");
        }
        assertEquals(1, correl[1][1], 0.1);
    }
}

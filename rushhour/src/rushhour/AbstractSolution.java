// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist

package rushhour;
import java.util.ArrayList;
import search.SearchFailureException;
import rushhour.model.BoardState;
import rushhour.model.Boards;
import rushhour.model.Move;
import rushhour.BreadthFirstFinder;

/**
 *  Wrapper for the heuristic homework solution.
 */
public abstract class AbstractSolution {
  private final MovesFinder[] moves;

  /**
   *  The constructor takes the implementations of A* with various
   *  heuristics as arguments.
   */
  public AbstractSolution(MovesFinder... moves) {
    this.moves = moves;
  }

  /**
   *  A sample runner for the solution: runs all of the solvers (plus
   *  {@linkplain BreadthFirstFinder BFS}) on all of the sample
   *  boards, and shows the results as a table.  You might find it
   *  useful to extend your version of this method to help you with
   *  the effective branching factor calculations.
   */
  public void run() {
    final int
        samples = Boards.BOARDS.size(),
        solvers = moves.length;

    // Pull the solvers names
    final String[] solverName = new String[solvers];
    for(int i=0; i<solvers; i++) {
      solverName[i] = moves[i].toString();
    }

    // Where we'll capture the data from running the samples
    final BreadthFirstFinder bfs = new BreadthFirstFinder();
    final long[][]
        addedToFrontier = new long[samples][solvers],
        expandedFromFrontier = new long[samples][solvers],
        solverDepth = new long[samples][solvers];
    final double[][] ebf = new double[samples][solvers];
    final long[]
        bfsAdded = new long[samples],
        bfsExpanded = new long[samples],
        bfsDepth = new long[samples];
    final double[] bfsEbf = new double[samples];
    final String[] sampleNames = new String[samples];
    long maxAdded=0, maxExpanded=0, maxDepth=0;
    double maxEBF=0;
    int maxSampleNameLen=3, nextSample=0;

    // Run all the samples
    SAMPLES:
    for(final String sampleKey : Boards.BOARDS.keySet()) {
      final BoardState state = Boards.BOARDS.get(sampleKey);
      sampleNames[nextSample] = sampleKey;
      if (maxSampleNameLen < sampleKey.length()) {
        maxSampleNameLen = sampleKey.length();
      }

      // Run under BFS, set the baselines
      final ArrayList<Move> bfsResult;
      try {
        bfsResult = bfs.search(state).fillPath(new ArrayList<Move>());
      } catch (SearchFailureException sfe) {
        System.err.println
            ("Warning: BFS failed for " + sampleKey + ", skipping board");
        bfsAdded[nextSample] = -1;
        bfsExpanded[nextSample] = -1;
        bfsDepth[nextSample] = -1;
        bfsEbf[nextSample] = Double.NaN;
        for(int i=0; i<solvers; i++) {
          addedToFrontier[nextSample][i] = -1;
          expandedFromFrontier[nextSample][i] = -1;
          solverDepth[nextSample][i] = -1;
          ebf[nextSample][i] = Double.NaN;
        }
        continue SAMPLES;
      }
      final long
          addedBfs = bfs.getLastAddedToFrontier(),
          expandedBfs = bfs.getLastExpandedFromFrontier(),
          depthBfs = bfsResult.size();
      final double bEBF = calculateEBF(1, expandedBfs, depthBfs, expandedBfs);
      bfsAdded[nextSample] = addedBfs;
      bfsExpanded[nextSample] = expandedBfs;
      bfsDepth[nextSample] = depthBfs;
      bfsEbf[nextSample] = bEBF;
      if (maxAdded < addedBfs) { maxAdded = addedBfs; }
      if (maxExpanded < expandedBfs) { maxExpanded = expandedBfs; }
      if (maxDepth < depthBfs) { maxDepth = depthBfs; }
      if (maxEBF < bEBF) { maxEBF = bEBF; }

      // Run under each of the solvers in the moves array, capture
      // those results.
      SOLVERS:
      for(int i=0; i<solvers; i++) {
        final MovesFinder solver = moves[i];
        final ArrayList<Move> solverResult;
        try {
          solverResult = solver.search(state).fillPath(new ArrayList<Move>());
        } catch (SearchFailureException sfe) {
          System.err.println("Warning: " + solver + " failed for "
                             + sampleKey + ", skipping");
          addedToFrontier[nextSample][i] = -1;
          expandedFromFrontier[nextSample][i] = -1;
          solverDepth[nextSample][i] = -1;
          ebf[nextSample][i] = Double.NaN;
          continue SOLVERS;
        }

        final long
            addedSolver = solver.getLastAddedToFrontier(),
            expandedSolver = solver.getLastExpandedFromFrontier(),
            depthSolver = solverResult.size();
        addedToFrontier[nextSample][i] = addedSolver;
        expandedFromFrontier[nextSample][i] = expandedSolver;
        solverDepth[nextSample][i] = depthSolver;
        final double solverEBF
            = calculateEBF(1, expandedSolver, depthBfs, expandedSolver);
        ebf[nextSample][i] = solverEBF;
        if (maxAdded < addedSolver) { maxAdded = addedSolver; }
        if (maxExpanded < expandedSolver) { maxExpanded = expandedSolver; }
        if (maxDepth < depthSolver) { maxDepth = depthSolver; }
        if (maxEBF < solverEBF) { maxEBF = solverEBF; }
      }

      // Next sample number
      nextSample += 1;
    }

    // Prep table elements
    final long
        addedWidth = Math.max(5, // "added"
                              1+Math.round(Math.floor(Math.log10(maxAdded)))),
        expandedWidth = Math.max(3, // "exp"
                                 1+Math.round(Math.floor(Math.log10(maxExpanded)))),
        depthWidth = Math.max(3, // "len"
                              1+Math.round(Math.floor(Math.log10(maxDepth)))),
        ebfWidth = Math.max(4, // "ebf"
                            4+Math.round(Math.floor(Math.log10(maxEBF))));
    final String
        sampleTitle = String.format("%%%ds", maxSampleNameLen),
        solverHeaderFmt = String.format(" | %%%ds %%%ds %%%ds %%%ds",
                                        depthWidth, addedWidth, expandedWidth,
                                        ebfWidth),
        solverHeader = String.format(solverHeaderFmt,
                                     "len", "added", "exp", "ebf"),
        solverTitle = String.format(" | %%%ds",
                                    3+depthWidth+addedWidth+expandedWidth+ebfWidth),
        dataFormat = String.format(" | %%%dd %%%dd %%s%%%dd%%s %%%d.2f",
                                   depthWidth, addedWidth, expandedWidth,
                                   ebfWidth);

    // And the table. Title rows first
    System.out.printf(sampleTitle, "");
    System.out.printf(solverTitle, "BFS");
    for(int col=0; col<solvers; col++) {
      System.out.printf(solverTitle, "[" + (1+col) + "]");
    }
    System.out.println(" |");

    System.out.printf(sampleTitle, "Board config");
    System.out.print(solverHeader);
    for(int col=0; col<solvers; col++) {
      System.out.print(solverHeader);
    }
    System.out.println(" |");

    // Table rows

    for(int row=0; row<samples; row++) {
      final long bfsExp = bfsExpanded[row];
      System.out.printf(sampleTitle, sampleNames[row]);
      System.out.printf(dataFormat,
                        bfsDepth[row], bfsAdded[row], "", bfsExp, "",
                        bfsEbf[row]);
      for(int col=0; col<solvers; col++) {
        final long thisExp = expandedFromFrontier[row][col];
        final String
            expPrefix = thisExp>bfsExp ? ANSI_RED_BACKGROUND : "",
            expSuffix = thisExp>bfsExp ? ANSI_RESET : "";
        System.out.printf(dataFormat, solverDepth[row][col],
                          addedToFrontier[row][col],
                          expPrefix, thisExp, expSuffix,
                          ebf[row][col]);
      }
      System.out.println(" |");
    }

    // Stats on EBFs for each solver
    System.out.println();
    System.out.println("Effective branching factors by heuristic");
    final double[]
        meanEBF = new double[solvers],
        devEBF = new double[solvers];
    for(int k=0; k<solvers; k++) {
      meanEBF[k] = 0;
      for(int i=0; i<samples; i++) {
        meanEBF[k] += ebf[i][k];
      }
      meanEBF[k] /= samples;

      double devSum = 0;
      for(int i=0; i<samples; i++) {
        final double diff = ebf[i][k] - meanEBF[k];
        devSum += diff*diff;
      }
      devEBF[k] = Math.sqrt(devSum / (samples-1));

      System.out.printf(" [%d] mean=%.3f, SD=%.3f\n",
                        (1+k), meanEBF[k], devEBF[k]);
    }

    // Solver titles as footnotes
    System.out.println();
    for(int col=0; col<solvers; col++) {
      System.out.println("[" + (1+col) + "] " + solverName[col]);
    }
  }

  private static final double DELTA = 0.001;
  double calculateEBF(double below, double above,
                      long trueDepth, long nodesExpanded) {
    final double midpoint = (above+below)/2.0;
    if (above-below < DELTA) { return midpoint; }

    final double midNodes = nodesFrom(trueDepth, midpoint);
    if (midNodes < nodesExpanded) {
      return calculateEBF(midpoint, above, trueDepth, nodesExpanded);
    } else if (midNodes > nodesExpanded) {
      return calculateEBF(below, midpoint, trueDepth, nodesExpanded);
    } else {
      return midpoint;
    }
  }

  public double nodesFrom(final long trueDepth, final double ebf) {
    double result=0, prod=1;
    for(int i=1; i<=trueDepth; i++) {
      prod = prod*ebf;
      result += prod;
    }
    return result;
  }

  public static final String ANSI_RESET = "\033[0m";
  public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
  public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
  public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
  public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
  public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
  public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
  public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
}



// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist

package rushhour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;
import search.SearchFailureException;
import search.BreadthFirstSearcher;
import search.KnowsOwnCost;
import search.SearchTreeNode;
import search.GoalCheckers;
import search.ExploredSets;
import rushhour.model.BoardState;
import rushhour.model.Move;
import rushhour.model.PlacedCar;

/**
 *  Model solution finder for RushHour boards using breadth-first
 *  search.
 */
public class BreadthFirstFinder
    extends BreadthFirstSearcher<BoardState,BoardNode>
    implements Runners {

  public BreadthFirstFinder() {
    super(() -> GoalCheckers.firstGoal((BoardNode cn) -> cn.hasGoalState()),
          ExploredSets.trackGeneratedByArtifactHashSet((n) -> n.getState().boardString()),
          (BoardState board) -> new BoardNode(board));
    // setDebug(true);
  }

  /**
   *  Tests the given sample boards using BFS.
   * @see Runners#runSampleBoards()
   */
  public static void main(String[] argv) {
    new BreadthFirstFinder().runSampleBoards();
  }

  @Override public void debugFrontierRemoval(BoardNode node) {
    super.debugFrontierRemoval(node);
    System.out.print(" - Available moves: ");
    String sep = "";
    String fin = "none";
    for(final Move move : node.getState().getValidMoves()) {
      System.out.print(sep);
      System.out.print(move.toString());
      sep = ", ";
      fin = "";
    }
    System.out.println(fin);
  }
}

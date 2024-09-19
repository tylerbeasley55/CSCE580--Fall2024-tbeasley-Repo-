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
import search.AStarSearcher;
import search.KnowsOwnCost;
import search.SearchTreeNode;
import search.ExploredSets;
import search.Frontiers;
import rushhour.model.BoardState;
import rushhour.model.Move;
import rushhour.model.PlacedCar;

/**
 *  Superclass for the various A* approaches to be tested in this
 *  homework.  The constructor takes the heuristic function, and plugs
 *  it into the general A* framework.
 *
 *  Note that if we override {@link Object#toString toString} in the
 *  concrete subclasses with nice and distinct names, it will help the
 *  output of {@link AbstractSolution#run} to be a bit more readable.
 */
public class MovesFinder extends AStarSearcher<BoardState,BoardNode>
    implements Runners {

  public MovesFinder(Function<BoardState,Double> heuristic) {
    super((BoardNode node) -> node.hasGoalState(),
          (BoardNode node) -> heuristic.apply(node.getState()),
          ExploredSets.trackGeneratedByArtifactHashSet
              ((node) -> node.getState().boardString()),
          (BoardState board) -> new BoardNode(board));
    // setDebug(true);
  }
}

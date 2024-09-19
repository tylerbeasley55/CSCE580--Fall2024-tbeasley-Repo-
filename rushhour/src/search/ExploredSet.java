// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist

package search;

/**
 *  Methods required to track the nodes which we have already either
 *  added to the frontier, or removed from the frontier for
 *  exploration.
 *
 *  The {@link ExploredSets} class contains some standard
 *  implementations and utilities of this interface.
 *
 * @param <Node> The type of tree nodes stored in the explored set.
 *
 * @see GraphSearcher#search
 */
public interface ExploredSet<Node> {

  /**
   * Called by the {@link GraphSearcher#search search} method with the
   * initial tree node.
   *
   * @param n The tree node to be noted
   */
  public void noteInitial(Node n);

  /**
   * Called by the {@link GraphSearcher#search search} method with a
   * node when it is removed from the frontier for exploration.
   *
   * @param n The tree node to be noted
   */
  public void noteExplored(Node n);

  /**
   * Called by the {@link GraphSearcher#search search} method to
   * determine whether a node should be added to the frontier.
   *
   * @param n The tree node to be tested
   */
  public boolean shouldAddToFrontier(Node n);
}



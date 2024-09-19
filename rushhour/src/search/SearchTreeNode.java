// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist

package search;

/**
 * Methods required of a search tree node.
 *
 * @param <Self> The implementation type of these nodes.  So a class
 * implementing this interface would start something like
 * <pre>{@code
  public MyNode implements SearchTreeNode<MyNode, MyState> {
    ...
  } }</pre>
 * @param <State> The type of the search state underlying these nodes.
 */
public interface SearchTreeNode<Self extends SearchTreeNode<Self,State>,
                                State> {
  /**
   * Returns the search state underlying this node.
   */
  public State getState();

  /**
   * Expands the search node into successor nodes.
   * @return An {@link Iterable} instance of the successor nodes.
   */
  public Iterable<Self> expand();
}


// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist

package search;
import java.util.ArrayList;

/**
 * Type of search tree nodes which form a path of nodes, each expanded
 * from its parent which is next in the path.  The essential method of
 * this interface is {@link #getParent getParent}, pointing to the
 * node from which this node was expanded.  The other additional
 * methods have default implementations in terms of {@link #getParent
 * getParent}.
 *
 * @param <This> The implementation type of these nodes.  So a class
 * implementing this interface would start something like
 * <pre>{@code
  public MyNode implements SearchTreePathNode<MyNode, MyState> {
    ...
  } }</pre>
 * @param <S> The type of the search state underlying these nodes.
 */
public interface SearchTreePathNode<This extends SearchTreePathNode<This,S>,S>
    extends SearchTreeNode<This,S> {

  /**
   * Returns the node from which this node was expanded.
   * @return {@code null} if this is the initial node.
   */
  public This getParent();

  /**
   * Returns the sequence of nodes, starting with the initial state's
   * node at index 0, leading to this node.
   */
  public default ArrayList<S> statePath() {
    return statePath(new ArrayList<S>());
  }

  /**
   * Write the sequence of nodes starting with the initial state's
   * node and leading to this node into the given {@link
   * java.util.ArrayList ArrayList}.
   */
  public default ArrayList<S> statePath(ArrayList<S> states) {
    final This parent = getParent();
    if (parent != null) {
      parent.statePath(states);
    }
    states.add(getState());
    return states;
  }

  public default String pathToString() {
    final This parent = getParent();
    if (parent == null) {
      return getState().toString();
    } else {
      return parent.pathToString() + " >> " + getState().toString();
    }
  }
}


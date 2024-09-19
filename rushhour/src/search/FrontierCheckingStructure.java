// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist

package search;

/**
 *  Subclass of frontier representations which support checking
 *  membership of a node in the frontier.
 *
 * @param <Node> The type of tree nodes stored in the frontier.
 */
public interface FrontierCheckingStructure<Node>
    extends FrontierStructure<Node> {

  public boolean contains(Node n);
}

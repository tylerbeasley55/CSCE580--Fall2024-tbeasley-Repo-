// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist

package search;

/**
 *  Additional interface implemented by search tree nodes which are
 *  aware of their own cost.
 */
public interface KnowsOwnCost {

  /** @return The cost of reaching the state in this node. */
  public double getCost();

}

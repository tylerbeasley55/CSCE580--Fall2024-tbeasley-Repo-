// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist

package samplesoln;
import rushhour.MovesFinder;
import rushhour.model.BoardState;
import rushhour.model.PlacedCar;

/**
 *  Simple heuristic looking at the distance the family car must move.
 *  Might not be consistent if it needs to move back first.
 */
public class FamilyCarDist extends MovesFinder {
  public FamilyCarDist() {
    super((BoardState b) -> {
        final PlacedCar us = b.placement(0);
        return (double)(b.getBoardSize()-us.getLength()-us.getCol());
      });
  }

  @Override public String toString() {
    return "Family car distance only";
  }
}

// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist


package rushhour.model;

@SuppressWarnings("serial")
public class IllegalMoveException extends MoveException {

  public IllegalMoveException(Move.Direction dir, PlacedCar car) {
    super(dir, car);
  }
}


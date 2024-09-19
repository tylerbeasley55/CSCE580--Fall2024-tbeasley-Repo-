// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist


package rushhour.model;

@SuppressWarnings("serial")
public class MoveException extends RuntimeException {

  private final Move.Direction dir;
  private final PlacedCar car;

  public MoveException(Move.Direction dir, PlacedCar car) {
    this.dir = dir;
    this.car = car;
  }

  public Move.Direction getDirection() { return dir; }
  public PlacedCar getPlacedCar() { return car; }
}


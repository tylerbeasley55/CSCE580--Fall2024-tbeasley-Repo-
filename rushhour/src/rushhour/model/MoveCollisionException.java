// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist


package rushhour.model;

@SuppressWarnings("serial")
public class MoveCollisionException extends MoveException {

  private final BoardState board;

  public MoveCollisionException(Move.Direction dir, PlacedCar car,
                                BoardState board) {
    super(dir, car);
    this.board = board;
  }

  public BoardState getBoard() { return board; }

  @Override public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("collision moving ");
    sb.append(getPlacedCar().getName());
    sb.append(" ");
    sb.append(getDirection());
    sb.append(" in\n");
    board.toString(sb);
    return sb.toString();
  }
}


package samplesoln;
import rushhour.MovesFinder;
import rushhour.model.BoardState;
import rushhour.model.PlacedCar;

public class BlockingCars extends MovesFinder {
    public BlockingCars() {
        super((BoardState b) -> {
            final PlacedCar us = b.placement(0);
            double blocking = 0;
            // get familt car positions
            int famCarRow = us.getRow();
            int famCarFrontCol = us.getCol() + us.getLength(); 
            for(int col = famCarFrontCol; col < b.getBoardSize(); col ++) {
                // check for number of cars in way
                // note - one car cannot occupy multiple cols directly in front of the family car (blocking car cant be horizontal on same row as family car)
                if (b.filledAt(famCarRow, col)) {
                    blocking++;
                }
            }
            return blocking;
        });
    }
    @Override public String toString() {
        return "Blocking Car only";
      }
}

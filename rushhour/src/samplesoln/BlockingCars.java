package samplesoln;
import rushhour.MovesFinder;
import rushhour.model.BoardState;
import rushhour.model.PlacedCar;
// adds # of blocking cars to total distance from goak
public class BlockingCars extends MovesFinder {
    public BlockingCars() {
        super((BoardState b) -> {
            final PlacedCar us = b.placement(0);
            double blocking = 0;
            double distanceToGoal = b.getBoardSize()-us.getLength()-us.getCol();
            // get family car positions
            int usRow = us.getRow();
            int usBackCol = us.getCol() + us.getLength() - 1; // start with back column to make sure length of car is accounted for
            for(int col = usBackCol + 1; col < b.getBoardSize(); col ++) {
                // check for number of cars in way
                // note - one car cannot occupy multiple cols directly in front of the family car (blocking car cant be horizontal on same row as family car)
                if (b.filledAt(usRow, col)) {
                    blocking++;
                }
            }

            return distanceToGoal + blocking;
        });
    }
    @Override public String toString() {
        return "Distance + Blocking Car";
    }
}

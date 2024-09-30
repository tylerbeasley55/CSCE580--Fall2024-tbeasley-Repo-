// Written by Tyler Beasley for CSCE580 Project1
package samplesoln;
import rushhour.MovesFinder;
import rushhour.model.BoardState;
import rushhour.model.PlacedCar;

// This heuristica adds the cost to move the blocking cars to the blocking cars heuristic function
public class WeightedBlockingCars extends MovesFinder {
    public WeightedBlockingCars() {
        super((BoardState b) -> {
            final PlacedCar us = b.placement(0);
            double weight = 0;
            double blockingCar = 0;
            double distanceToGoal = b.getBoardSize()-us.getLength()-us.getCol();
            // get family car positions
            int usRow = us.getRow();
            int usFrontCol = us.getCol() + us.getLength(); // front column for distance
            for(int col = usFrontCol; col < b.getBoardSize(); col ++) {
                // check for number of cars in way and decrease penalty if they are farther away
                if (b.filledAt(usRow, col)) {
                    double distance = col - (usFrontCol);
                    // decrease weight for distance
                    weight = 1.0/(1+distance);

                }
            }
            return distanceToGoal + (blockingCar*weight);
        });
    }
    @Override public String toString() {
        return "Distance + Weighted Blocking Car";
      }
}


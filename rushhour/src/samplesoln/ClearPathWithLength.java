package samplesoln;
import rushhour.MovesFinder;
import rushhour.model.BoardState;
import rushhour.model.PlacedCar;

// This heuristica adds the cost to move the blocking cars to the blocking cars heuristic function

public class ClearPathWithLength extends MovesFinder {
    public ClearPathWithLength() {
        super((BoardState b) -> {
            final PlacedCar us = b.placement(0);
            double movesCost = 0;
            double distanceToGoal = b.getBoardSize()-us.getLength()-us.getCol();
            // get family car positions
            int usRow = us.getRow();
            int usBackCol = us.getCol() + us.getLength() - 1; 
            for(int col = usBackCol + 1; col < b.getBoardSize(); col ++) {
                // check for number of cars in way
                // note - one car cannot occupy multiple cols directly in front of the family car (blocking car cant be horizontal on same row as family car)
                if (b.filledAt(usRow, col)) {
                    PlacedCar BlockingCar = getCar(b, usRow, col);
                    // check if null
                    if (BlockingCar != null) {
                        movesCost += getMovesPerBlockedCar(b, BlockingCar);
                    }
                }
            }
            return distanceToGoal + movesCost;
        });
    }
    @Override public String toString() {
        return "Distance + possibility to move all blocking cars, considering length";
    }

    private static PlacedCar getCar(BoardState b, int row, int col) {
        for(int i = 0; i < b.placed(); i++) {
            PlacedCar car = b.placement(i);
            for(int l = 0; l < car.getLength(); l++) { // check for whole length of car
                if(car.isVertical()) { // check orientation
                    if(car.getCol() == col && car.getRow() + l == row) {
                        return car;
                    }
                } else {
                    if (car.getRow() == row && car.getCol() + l == col) {
                        return car;
                    }
                }
            }
        }
        return null; // no car in positon
    }

    private static double getMovesPerBlockedCar(BoardState b, PlacedCar blockingCar) {
        // cost of move is length of car - 1
        double cost = blockingCar.getLength() - 1;
        if(!validMoves(blockingCar, b)) {
            cost++;
        } else {
            for (int col = blockingCar.getCol() + blockingCar.getLength(); col < b.getBoardSize(); col++) {
                if (b.filledAt(blockingCar.getRow(), col)) {
                    PlacedCar nextBlockingCar = getCar(b, blockingCar.getRow(), col);
                    if (nextBlockingCar.isVertical()) {
                        cost += getMovesPerBlockedCar(b, nextBlockingCar);
                    }
                }
            }
        }
        return cost;
    }
    private static boolean validMoves(PlacedCar blockingCar, BoardState b) {
        int row = blockingCar.getRow();
        int col = blockingCar.getCol();
        if (blockingCar.isVertical()) {

            // Check for upward move
            if (row > 0 && !b.filledAt(row - 1, col)) {
                return true; // Valid move up
            }

            // Check for downward move
            if (row + blockingCar.getLength() < b.getBoardSize() && !b.filledAt(row + blockingCar.getLength(), col)) {
                return true; // Valid move down
            }
        } else {
            // Check for left move
            if (col > 0 && !b.filledAt(row, col - 1)) {
                return true; // Valid move left
            }

            // Check for right move
            if (col + blockingCar.getLength() < b.getBoardSize() && !b.filledAt(row, col + blockingCar.getLength())) {
                return true; // Valid move right
            }
        }
        return false; // No valid moves found
    }
}

package samplesoln;
import rushhour.MovesFinder;
import rushhour.model.BoardState;
import rushhour.model.PlacedCar;

public class BlockingCarsWithMoveCost extends MovesFinder {
    public BlockingCarsWithMoveCost() {
        super((BoardState b) -> {
            final PlacedCar us = b.placement(0);
            double movesCost = 0;
            // get familt car positions
            int usRow = us.getRow();
            int usBackCol = us.getCol() + us.getLength() - 1; 
            for(int col = usBackCol + 1; col < b.getBoardSize(); col ++) {
                // check for number of cars in way
                // note - one car cannot occupy multiple cols directly in front of the family car (blocking car cant be horizontal on same row as family car)
                if (b.filledAt(usRow, col)) {
                    PlacedCar BlockingCar = getCar(b, usRow, col);
                }
            }
            return movesCost;
        });
    }
    @Override public String toString() {
        return "Blocking Car only";
    }

    private PlacedCar getCar(BoardState b, int row, int col) {
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
}

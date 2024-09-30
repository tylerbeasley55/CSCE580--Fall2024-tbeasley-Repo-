// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist
// Edited by Tyler Beasley for CSCE580 Project 1
package samplesoln;

public class Solution extends rushhour.AbstractSolution {
  public Solution() {
    super(new ZeroHeuristic(),
          new FamilyCarDist(),
          new BlockingCars(),
          new WeightedBlockingCars(),
          new ClearPath(),
          new EnhancedClearPath(),
          new ClearPathWithLength());
  }

  // Use *exactly* this main method
  public static void main(String[] args) { new Solution().run(); }
}

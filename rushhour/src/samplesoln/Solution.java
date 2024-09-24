// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist

package samplesoln;

public class Solution extends rushhour.AbstractSolution {
  public Solution() {
    super(new ZeroHeuristic(),
          new FamilyCarDist(),
          new BlockingCars());
  }

  // Use *exactly* this main method
  public static void main(String[] args) { new Solution().run(); }
}

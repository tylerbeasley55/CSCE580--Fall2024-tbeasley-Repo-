// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist

/**
 * Framework for solving a Rush Hour puzzle via graph search.
 *
 * <p>Some starting points for exploring this code:</p>
 *
 * <ol>
 * <li>
 *   Package {@link rushhour.model} is an implementation of
 *   the game mechanics --- the Rushhour board, cars, possible moves,
 *   etc.
 *   <ul>
 *   <li>
 *     Class {@link rushhour.model.Boards} has several sample
 *     initial board configurations.
 *   </li>
 *   </ul>
 * </li>
 * <li>
 *   Package {@link search} is a generic
 *   implementation of several of the graph search algorithms we have
 *   discussed.
 *   <ul>
 *   <li>
 *     The heart of this implementation is class
 *     {@link search.GraphSearcher GraphSearcher},
 *     which implements the <b>Graph-Search</b> algorithm of Russell
 *     and Norvig in its general form.  The specific behaviors of the
 *     frontier, the explored set, checking for goal nodes, etc. are
 *     provided through the generic type arguments, and through the
 *     behaviors passed as constructor arguments.
 *   </li>
 *   <li>
 *     You will make particular (if indirect) use of class
 *     {@link search.AStarSearcher AStarSearcher},
 *     which specializes
 *     {@link search.GraphSearcher GraphSearcher}
 *     with the priority queue details of A* search.
 *   </li>
 *   </ul>
 * </li>
 * <li>
 *   Package {@link rushhour} links the generic search
 *   implementations with the Rushhour model.
 *   <ul>
 *   <li>
 *     Class {@link rushhour.BreadthFirstFinder} solves
 *     Rushhour puzzles using breadth-first search.  This class is your
 *     frenemy: On the one hand, this class gives you a working example
 *     of how we specialize the general search algorithms to a
 *     particular problem.  But on the other hand this class is your
 *     rival, since the entire point of designing good heuristics with
 *     A* is to \emph{beat blind search algorithms like BFS}.
 *   </li>
 *   <li>
 *     For each heuristic function you implement, you will write
 *     one class extending {@link rushhour.MovesFinder}.  Note
 *     the series of {@link rushhour.MovesFinder MovesFinder}'s
 *     superclasses: they include the A* implementation of
 *     {@link search.AStarSearcher AStarSearcher},
 *     so extensions of {@link rushhour.MovesFinder MovesFinder}
 *     all use A* search.  Note also that the constructor for
 *     {@link rushhour.MovesFinder MovesFinder} takes only one
 *     argument --- the heuristic function.  So
 *     {@link rushhour.MovesFinder MovesFinder}
 *     applies the heuristic given in its constructor to A*.  Your
 *     subclasses should provide that constructor argument, and nothing
 *     more: do not otherwise override any methods inherited from
 *     {@link rushhour.MovesFinder MovesFinder} in your final
 *     submission.  (Overriding the various debugging functions as you
 *     develop and debug is fine, but silence this output for your
 *     actual submission.)
 *
 *     For example, see the <a
 *     href="src/samplesoln/ZeroHeuristic.java">ZeroHeuristic</a>
 *     class in the sample solution.
 *
 *   </li>
 *   <li>
 *     Finally, you will extend class
 *     {@link rushhour.AbstractSolution} to wrap up all of your
 *     work on one bundle.
 *
 *     The {@link rushhour.AbstractSolution#run run} method of
 *     this class is suitable for calling
 *     from the <tt>main</tt> method of your concrete
 *     <tt>rushhour.Solution</tt> class, such as with
 *     <pre>  new Solution().run();</pre>
 *     The given version will apply all of your solvers, plus BFS, to
 *     all of the sample boards, and print the results as a table.  Of
 *     course you are free to override or edit this method locally to
 *     print additional calculations useful for your analysis of the
 *     effective branching factor.
 *   </li>
 *   </ul>
 * </li>
 * </ol>
 */
package rushhour;

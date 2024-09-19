// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist

/**
 * A generic implementation of several of the graph search algorithms
 * discussed in Russell and Norvig.
 *
 * The heart of this implementation is class {@link GraphSearcher},
 * which implements the {@code GraphSearch} algorithm of Russell and
 * Norvig in its general form. The specific behaviors of the frontier,
 * the explored set, checking for goal nodes, etc. are provided
 * through the generic type arguments, and through the behaviors
 * passed as constructor arguments.
 *
 * However the {@link GraphSearcher} class is a bit abstract, and so
 * there are specialized versions of it for common algorithms and
 * purposes.
 *
 * <ul>
 *
 * <li><p>The {@link AStarSearcher} class fixes the operation of the
 * search frontier for the A* algorithm.</p><p>This class has two
 * further specializations: {@link AStarSearcher.SimpleNodes} with
 * minimal search tree nodes containing only the underlying state and
 * its cost, and {@link AStarSearcher.PathNodes} with nodes also each
 * containing a backlink to its parent.</p></li>
 *
 * <li><p>The {@link BreadthFirstSearcher} class fixes the operation
 * of the search frontier for the breadth-first search
 * algorithm.</p></li>
 *
 *
 * </ul>
 */
package search;

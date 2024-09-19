// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist

package search;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *  Sample implementations of checkers that a tree node corresponds to
 *  a search goal.
 */
public class GoalCheckers {

  /**
   * Implementation that checks whether a tree node corresponds to a
   * search goal, built on a predicate on search tree nodes.
   */
  public static <Node> GoalChecker<Node>
      firstGoal(final Predicate<Node> checker) {
    return new GoalChecker<Node>() {
      public Node get() throws SearchFailureException {
        throw new SearchFailureException();
      }
      public boolean test(Node n) {
        return checker.test(n);
      }
    };
  }

  /**
   * Implementation that checks whether a tree node corresponds to a
   * search goal, built on a predicate on search states.
   */
  public static <S, N extends SearchTreeNode<N,S>>
      Supplier<GoalChecker<N>> goalCheckerFactory(final Predicate<S> pred) {
    return () -> new GoalChecker<N>() {
        public boolean test(N n) { return pred.test(n.getState()); }
        public N get() throws SearchFailureException {
          throw new SearchFailureException();
        }
      };
  }

  /**
   *  Converts a predicate on search states into a predicate on search
   *  tree nodes.
   */
  public static <S, N extends SearchTreeNode<N,S>> Predicate<N>
      liftPredicate(final Predicate<S> pred) {
    return (n) -> pred.test(n.getState());
  }
}


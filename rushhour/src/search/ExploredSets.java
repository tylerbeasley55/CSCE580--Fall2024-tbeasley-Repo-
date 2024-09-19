// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist

package search;
import java.util.HashSet;
import java.util.function.Function;

/**
 * Three sample implementations of {@linkplain search.ExploredSet ways
 * to track nodes} which we have already either added to the frontier,
 * or removed from the frontier for exploration.
 */
public class ExploredSets {

  /**
   * Implementation which does not actually track anything.
   *
   * @param <F> The type of the frontier.
   * @param <N> The type of search nodes.
   */
  public static <F,N> Function<F,ExploredSet<N>> doNotTrack() {
    return new Function<F,ExploredSet<N>>() {
      public ExploredSet<N> apply(F frontier) {
        return new ExploredSet<N>() {
          @Override public void noteExplored(N n) { }
          @Override public void noteInitial(N n) { }
          @Override public boolean shouldAddToFrontier(N n) { return true; }
        };
      }
    };
  }

  /**
   * Implementation which tracks nodes using a hash table of the
   * underlying node states.
   *
   * @param <F> The type of the frontier.
   * @param <S> The type of the state underlying each search node.
   * @param <N> The type of search nodes.
   */
  public static <F,S,N extends SearchTreeNode<N,S>>
      Function<F,ExploredSet<N>> trackStateByHashSet() {
    return trackGeneratedByArtifactHashSet((x) -> x.getState());
  }

  /**
   * Implementation which tracks nodes using a hash table of some
   * artifact constructed from the nodes.
   *
   * @param artifactBuilder Function taking a search node, and
   * returning the hashable artifact.
   *
   * @param <F> The type of the frontier.
   * @param <N> The type of search nodes.
   * @param <A> The type of the artifact to be hashed by the frontier
   * tracker.
   */
  public static <F,N,A> Function<F,ExploredSet<N>>
      trackGeneratedByArtifactHashSet(final Function<N,A> artifactBuilder) {
    return (F frontier) -> new ExploredSet<N>() {
        private final HashSet<A> tracker = new HashSet<A>();
        @Override public void noteExplored(N n) { }
        @Override public void noteInitial(N n) {
          final A artifact = artifactBuilder.apply(n);
          tracker.add(artifact);
        }
        @Override public boolean shouldAddToFrontier(N n) {
          final A artifact = artifactBuilder.apply(n);
          final boolean result = !tracker.contains(artifact);
          if (result) { tracker.add(artifact); }
          return result;
        }
      };
  }
}


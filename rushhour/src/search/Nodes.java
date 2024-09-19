// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist

package search;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.Predicate;

/**
 * Sample implementations of {@linkplain SearchTreeNode search tree nodes}.
 */
public final class Nodes {

  /**
   *  Helper class for {@linkplain SearchTreeNode search tree node}
   *  expansion bundling a node and its associated cost.
   */
  public static class CostAndStep<S> {
    private final double cost;
    private final S state;
    /**
     * @param cost Returns the cost associated with moving from the
     * local origin state to <tt>state</tt>.  Other plumbing adds this
     * cost to the overall origin, and deals with the estimate of
     * moving from <tt>state</tt> to the overall goal.
     */
    public CostAndStep(double cost, S state) {
      this.cost = cost;
      this.state = state;
    }
    public double getCost() { return cost; }
    public S getState() { return state; }
  }

  public static <S, N extends SearchTreeNode<N,S>> Function<N,Double>
      liftHeuristic(Function<S,Double> hs) {
    return (n) -> hs.apply(n.getState());
  }

  // /////////////////////////////////////////////////////////////////
  // Just a simple wrapper for the state.  No cost, no history

  /**
   *  Abstract implementations of {@linkplain SearchTreeNode search
   *  tree nodes} which only wraps the underlying state, and leaves
   *  the expansion of the node to be defined by a function argument
   *  passed to the constructor.
   *
   * @param <Self> The implementation type of these nodes.
   *
   * @param <Exp> Intermediate type returned in the process of
   * expanding nodes.
   *
   * @param <S> The type of the search state underlying these nodes.
   */
  static abstract class
      SimpleCoreTreeNode<Self extends SimpleCoreTreeNode<Self, Exp, S>, Exp, S>
      implements SearchTreeNode<Self, S> {
    final Function<S,Iterable<Exp>> expander;
    private final S state;

    /**
     * Sole constructor for this class.
     *
     * @param expander Function from the underlying state to an
     * iterable collection of information leading to expanded nodes.
     *
     * @param state The state underlying this node.
     */
    SimpleCoreTreeNode(Function<S,Iterable<Exp>> expander, S state) {
      this.expander = expander;
      this.state = state;
    }

    /** {@inheritDoc} */
    public S getState() { return state; }

    /** Format this node as a string, typically for debugging. */
    @Override public String toString() {
      return "[" + getState().toString() + "]";
    }
  };

  /**
   *  Simple implementation of {@linkplain SearchTreeNode search tree
   *  nodes}, expecting that the {@code Exp} type argument of the
   *  {@link SimpleCoreTreeNode} will be just the search state type.
   *
   * @param <S> The type of the search state underlying these nodes.
   */
  public static class SimpleTreeNode<S>
      extends SimpleCoreTreeNode<SimpleTreeNode<S>,S,S> {
    /**
     * Sole constructor for this class.
     *
     * @param expander Function from the underlying state to an
     * iterable collection of information leading to expanded nodes.
     *
     * @param state The state underlying this node.
     */
    public SimpleTreeNode(Function<S,Iterable<S>> expander, S state) {
      super(expander,state);
    }

    /** {@inheritDoc} */
    public Iterable<SimpleTreeNode<S>> expand() {
      return () -> new Iterator<SimpleTreeNode<S>>() {
          final SimpleTreeNode<S> node = SimpleTreeNode.this;
          final Iterator<S>
              dests = node.expander.apply(node.getState()).iterator();
          @Override public boolean hasNext() { return dests.hasNext(); }
          @Override public SimpleTreeNode<S> next() {
            final S dest = dests.next();
            return new SimpleTreeNode<S>(expander,dest);
          }
        };
    }

    /**
     *  Returns a factory for tree nodes given an expander from a
     *  state to the states underlying expanded nodes.
     */
    public static final <S> Function<S,SimpleTreeNode<S>>
        initializer(final Function<S,Iterable<S>> expander) {
      return (final S state) -> new SimpleTreeNode<S>(expander, state);
    }
  }

  // /////////////////////////////////////////////////////////////////
  // The state and the cost.

  /**
   *  Abstract implementation of {@linkplain SearchTreeNode search
   *  tree nodes} with a notion of cost.
   *
   * @param <Self> The implementation type of these nodes.
   *
   * @param <Exp> Intermediate type returned in the process of
   * expanding nodes, bundling (at least) successor node states and
   * their cost.
   *
   * @param <S> The type of the search state underlying these nodes.
   */
  static abstract class
      SimpleCoreTreeCostNode<Self extends
                                  SimpleCoreTreeCostNode<Self, Exp, S>,
                             Exp extends CostAndStep<S>, S>
      extends SimpleCoreTreeNode<Self, Exp, S>
      implements KnowsOwnCost {
    private final double cost;

    /**
     * Sole constructor for this class.
     *
     * @param expander Function from the underlying state to an
     * iterable collection of information for the construction of
     * expanded nodes.
     *
     * @param cost Cost associated with this node.
     *
     * @param state The state underlying this node.
     */
    SimpleCoreTreeCostNode(Function<S,Iterable<Exp>> expander,
                           double cost, S state) {
      super(expander, state);
      this.cost = cost;
    }

    /** {@inheritDoc} */
    public double getCost() { return cost; }

    /** {@inheritDoc} */
    @Override public String toString() {
      return "[" + getState().toString() + "@" + getCost() + "]";
    }
  };

  /**
   *  Implementation of {@linkplain SearchTreeNode search tree nodes}
   *  with a notion of cost.
   *
   * @param <S> The type of the search state underlying these nodes.
   */
  public static class SimpleTreeCostNode<S>
      extends SimpleCoreTreeCostNode<SimpleTreeCostNode<S>,CostAndStep<S>,S> {

    /**
     * Sole constructor for this class.
     *
     * @param expander Function from a state to (cost,state)-bundles
     * of information for the construction of expanded nodes.
     *
     * @param cost Cost associated with this node.
     *
     * @param state The state underlying this node.
     */
    public SimpleTreeCostNode(Function<S,Iterable<CostAndStep<S>>> expander,
                              double cost, S state) {
      super(expander,cost,state);
    }

    /** {@inheritDoc} */
    public Iterable<SimpleTreeCostNode<S>> expand() {
      return () -> new Iterator<SimpleTreeCostNode<S>>() {
          final SimpleTreeCostNode<S> node = SimpleTreeCostNode.this;
          final Iterator<CostAndStep<S>>
              dests = node.expander.apply(node.getState()).iterator();
          @Override public boolean hasNext() { return dests.hasNext(); }
          @Override public SimpleTreeCostNode<S> next() {
            final CostAndStep<S> cs = dests.next();
            final S dest = cs.getState();
            final double cost = cs.getCost();
            return new SimpleTreeCostNode<S>
                (expander, SimpleTreeCostNode.this.getCost()+cost, dest);
          }
        };
    }

    /**
     *  Returns a factory for tree nodes with costs, given an expander
     *  from a state to the states underlying its expanded nodes.
     */
    public static final <S> Function<S, SimpleTreeCostNode<S>>
        initializer(final Function<S,Iterable<CostAndStep<S>>> expander) {
      return (final S state)
          -> new SimpleTreeCostNode<S>(expander, 0.0, state);
    }
  }

  // /////////////////////////////////////////////////////////////////
  // The state plus a pointer to the parent node.

  /**
   *  Abstract implementation of {@linkplain SearchTreeNode search
   *  tree nodes} which contain links to their parent node.
   *
   * @param <Self> The implementation type of these nodes.
   *
   * @param <Exp> Intermediate type returned in the process of
   * expanding nodes.
   *
   * @param <S> The type of the search state underlying these nodes.
   */
  static abstract class
      SimpleCoreTreePathNode<Self extends
                                    SimpleCoreTreePathNode<Self,Exp,S>, Exp, S>
      extends SimpleCoreTreeNode<Self, Exp, S>
      implements SearchTreePathNode<Self, S> {
    private final Self parent;

    /**
     * @param expander Function from the underlying state to an
     * iterable collection of information used to create expanded
     * nodes.
     *
     * @param state The state underlying this node.
     */
    SimpleCoreTreePathNode(Function<S,Iterable<Exp>> expander, S state) {
      this(expander, null, state);
    }
    /**
     * @param parent The parent of this node.
     *
     * @param state The state underlying this node.
     */
    SimpleCoreTreePathNode(Self parent, S state) {
      this(parent.expander, parent, state);
    }

    /**
     * @param expander Function from the underlying state to an
     * iterable collection of information used to create expanded
     * nodes.
     *
     * @param parent The parent of this node.
     *
     * @param state The state underlying this node.
     */
    SimpleCoreTreePathNode(Function<S,Iterable<Exp>> expander,
                           Self parent, S state) {
      super(expander,state);
      this.parent = parent;
    }

    /** {@inheritDoc} */
    public Self getParent() { return parent; }

    /** {@inheritDoc} */
    @Override public String toString() {
      return "[" + pathToString() + "]";
    }

  };

  /**
   *  Implementation of {@linkplain SearchTreeNode search tree nodes}
   *  which retain a link to their parent.
   *
   * @param <S> The type of the search state underlying these nodes.
   */
  public static class SimpleTreePathNode<S>
      extends SimpleCoreTreePathNode<SimpleTreePathNode<S>,S, S> {

    /**
     * @param expander Function from a state to (cost,state)-bundles
     * of information for the construction of expanded nodes.
     *
     * @param state The state underlying this node.
     */
    public SimpleTreePathNode(Function<S,Iterable<S>> expander, S state) {
      super(expander,null,state);
    }

    /**
     * @param parent The parent of this node.
     *
     * @param state The state underlying this node.
     */
    public SimpleTreePathNode(SimpleTreePathNode<S> parent, S state) {
      super(parent.expander,parent,state);
    }

    /** {@inheritDoc} */
    public Iterable<SimpleTreePathNode<S>> expand() {
      return () -> new Iterator<SimpleTreePathNode<S>>() {
          final SimpleTreePathNode<S> node = SimpleTreePathNode.this;
          final Iterator<S>
              dests = node.expander.apply(node.getState()).iterator();
          @Override public boolean hasNext() { return dests.hasNext(); }
          @Override public SimpleTreePathNode<S> next() {
            final S dest = dests.next();
            return new SimpleTreePathNode<S>(node,dest);
          }
        };
    }

    /**
     *  Returns a factory for parent-tracking tree nodes, given an
     *  expander from a state to the states underlying expanded nodes.
     */
    public static final <S> Function<S, SimpleTreePathNode<S>>
        initializer(final Function<S,Iterable<S>> expander) {
      return (final S state) -> new SimpleTreePathNode<S>(expander, state);
    }
  }

  // /////////////////////////////////////////////////////////////////
  // The state, the cost, and a pointer to the parent node.

  /**
   *  Abstract implementation of {@linkplain SearchTreeNode search
   *  tree nodes} which have a notion of cost, and which contain links
   *  to their parent node.
   *
   * @param <Self> The implementation type of these nodes.
   *
   * @param <Exp> Intermediate type returned in the process of
   * expanding nodes, bundling (at least) successor node states and
   * their cost.
   *
   * @param <S> The type of the search state underlying these nodes.
   */
  static abstract class
      SimpleCoreTreePathCostNode<Self extends
                                      SimpleCoreTreePathCostNode<Self,Exp,S>,
                                 Exp extends CostAndStep<S>, S>
      extends SimpleCoreTreePathNode<Self, Exp, S>
      implements KnowsOwnCost {
    private final double cost;

    /**
     * @param expander Function from the underlying state to an
     * iterable collection of information for the construction of
     * expanded nodes.
     *
     * @param cost Cost associated with this node.
     *
     * @param state The state underlying this node.
     */
    SimpleCoreTreePathCostNode(Function<S,Iterable<Exp>> expander,
                               double cost, S state) {
      this(expander, null, cost, state);
    }

    /**
     * @param parent Parent of this node.
     *
     * @param cost Cost associated with this node.
     *
     * @param state The state underlying this node.
     */
    SimpleCoreTreePathCostNode(Self parent, double cost, S state) {
      this(parent.expander, parent, cost, state);
    }

    /**
     * @param expander Function from the underlying state to an
     * iterable collection of information for the construction of
     * expanded nodes.
     *
     * @param parent Parent of this node.
     *
     * @param cost Cost associated with this node.
     *
     * @param state The state underlying this node.
     */
    SimpleCoreTreePathCostNode(Function<S,Iterable<Exp>> expander,
                               Self parent, double cost, S state) {
      super(expander,parent,state);
      this.cost = cost;
    }

    /** {@inheritDoc} */
    public double getCost() { return cost; }

    /** {@inheritDoc} */
    @Override public String toString() {
      return "[" + pathToString() + "@" + getCost() + "]";
    }
  };

  /**
   *  Implementation of {@linkplain SearchTreeNode search tree nodes}
   *  which have a notion of cost, and which contain links to their
   *  parent node.
   *
   * @param <S> The type of the search state underlying these nodes.
   */
  public static class SimpleTreePathCostNode<S>
      extends SimpleCoreTreePathCostNode<SimpleTreePathCostNode<S>,
                                         CostAndStep<S>,
                                         S> {

    /**
     * @param expander Function from the underlying state to an
     * iterable collection of information for the construction of
     * expanded nodes.
     *
     * @param cost Cost associated with this node.
     *
     * @param state The state underlying this node.
     */
    public SimpleTreePathCostNode
        (Function<S,Iterable<CostAndStep<S>>> expander, double cost, S state) {
      super(expander,null,cost,state);
    }

    /**
     * @param parent Parent of this node.
     *
     * @param cost Cost associated with this node.
     *
     * @param state The state underlying this node.
     */
    public SimpleTreePathCostNode(SimpleTreePathCostNode<S> parent,
                                  double cost, S state) {
      super(parent.expander,parent,cost,state);
    }

    /** {@inheritDoc} */
    public Iterable<SimpleTreePathCostNode<S>> expand() {
      return () -> new Iterator<SimpleTreePathCostNode<S>>() {
          final SimpleTreePathCostNode<S> node = SimpleTreePathCostNode.this;
          final Iterator<CostAndStep<S>>
              dests = node.expander.apply(node.getState()).iterator();
          @Override public boolean hasNext() { return dests.hasNext(); }
          @Override public SimpleTreePathCostNode<S> next() {
            final CostAndStep<S> cs = dests.next();
            final S dest = cs.getState();
            final double cost = cs.getCost();
            return new SimpleTreePathCostNode<S>
                (node, SimpleTreePathCostNode.this.getCost()+cost, dest);
          }
        };
    }

    /**
     *  Returns a factory for tree nodes with track both their parent
     *  and their own cost, given an expander from a state to the
     *  states underlying expanded nodes.
     */
    public static final <S> Function<S, SimpleTreePathCostNode<S>>
        initializer(final Function<S,Iterable<CostAndStep<S>>> expander) {
      return (final S state)
          -> new SimpleTreePathCostNode<S>(expander, 0.0, state);
    }
  }
}

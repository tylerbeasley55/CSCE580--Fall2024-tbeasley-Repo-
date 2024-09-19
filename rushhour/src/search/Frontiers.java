// Rushhour AI assignment
//
// Description: Sample Rushhour boards
// Date: December 2020
// Author: John Maraist

package search;

import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 *  Standard implementations of structures representing a {@linkplain
 *  search.FrontierStructure frontier}.
 */
public class Frontiers {

  /**
   *  Abstract class adding debugging hooks to the basic {@linkplain
   *  search.FrontierStructure frontier}.
   */
  public static abstract class DebuggingFrontier<Node>
      implements FrontierStructure<Node> {
    @Override public void debugDisplayFrontier() { }
  }

  /**
   *  A frontier as a priority queue, for e.g. A*.
   */
  public static class PriorityQueue<Node> extends DebuggingFrontier<Node> {

    protected final java.util.PriorityQueue<Node> queue;

    public PriorityQueue(Comparator<Node> prioritizer) {
      this.queue = new java.util.PriorityQueue<Node>(prioritizer);
    }

    @Override public void add(Node n) {
      queue.add(n);
    }

    @Override public boolean isEmpty() {
      return queue.isEmpty();
    }

    @Override public Node pop() {
      final Node result = queue.poll();
      if (result == null) {
        throw new FrontierEmptyException();
      }
      return result;
    }

    @Override public int countOpen() {
      return queue.size();
    }

    @Override public void debugDisplayFrontier() {
      super.debugDisplayFrontier();
    }
  }

  /**
   *  Factory for {@linkplain search.Frontiers.PriorityQueue priority
   *  queue-based} frontier implementations.
   */
  public static <Node> Supplier<PriorityQueue<Node>>
      priorityQueueFactory(final Comparator<Node> prioritizer) {
    return new Supplier<PriorityQueue<Node>>() {
      public PriorityQueue<Node> get() {
        return new PriorityQueue<Node>(prioritizer);
      }
    };
  }

  // = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =

  public static class StateKeyedPriorityQueue<S, N extends SearchTreeNode<N,S>>
      extends PriorityQueue<N> {

    private final HashMap<S,N> bestPath = new HashMap<>();
    private final Comparator<N> prioritizer;

    public StateKeyedPriorityQueue(Comparator<N> prioritizer) {
      super(prioritizer);
      this.prioritizer = prioritizer;
    }

    @Override public void add(N node) {
      final S state = node.getState();
      if (bestPath.containsKey(state)) {
        final N previousBest = bestPath.get(state);
        if (prioritizer.compare(previousBest,node)>0) {
          queue.remove(previousBest);
          bestPath.put(state, node);
          super.add(node);
        }
      } else {
        bestPath.put(state, node);
        super.add(node);
      }
    }
  }

  public static <State, Node extends SearchTreeNode<Node,State>>
      Supplier<StateKeyedPriorityQueue<State,Node>>
      stateKeyedPriorityQueueFactory(final Comparator<Node> prioritizer) {
    return new Supplier<StateKeyedPriorityQueue<State,Node>>() {
      public StateKeyedPriorityQueue<State,Node> get() {
        return new StateKeyedPriorityQueue<State,Node>(prioritizer);
      }
    };
  }

  // = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =

  public static class DebuggablePriorityQueue<S, N extends SearchTreeNode<N,S>>
      extends DebuggingFrontier<N> {

    protected final java.util.PriorityQueue<N> queue;
    protected final HashMap<S,N> bestPath = new HashMap<>();
    protected final Comparator<N> prioritizer;

    public DebuggablePriorityQueue(Comparator<N> prioritizer) {
      this.prioritizer = prioritizer;
      this.queue = new java.util.PriorityQueue<N>(prioritizer);
    }

    @Override public void add(N node) {
      final S state = node.getState();
      if (bestPath.containsKey(state)) {
        final N previousBest = bestPath.get(state);
        if (prioritizer.compare(previousBest,node)>0) {
          queue.remove(previousBest);
          bestPath.put(state, node);
          queue.add(node);
        }
      } else {
        bestPath.put(state, node);
        queue.add(node);
      }
    }

    @Override public boolean isEmpty() {
      return queue.isEmpty();
    }

    @Override public N pop() {
      final N result = queue.poll();
      if (result == null) {
        throw new FrontierEmptyException();
      }
      return result;
    }

    @Override public int countOpen() {
      return queue.size();
    }

    @Override public void debugDisplayFrontier() {
      super.debugDisplayFrontier();
    }
  }

  public static <State, Node extends SearchTreeNode<Node,State>>
      Supplier<DebuggablePriorityQueue<State,Node>>
      debuggablePriorityQueueFactory(final Comparator<Node> prioritizer) {
    return new Supplier<DebuggablePriorityQueue<State,Node>>() {
      public DebuggablePriorityQueue<State,Node> get() {
        return new DebuggablePriorityQueue<State,Node>(prioritizer);
      }
    };
  }

  // =================================================================

  /**
   *  A queue as a priority queue, for e.g. BFS.
   */
  public static class Queue<Node> implements FrontierCheckingStructure<Node> {

    protected final java.util.LinkedList<Node>
        queue = new java.util.LinkedList<Node>();

    @Override public void add(Node n) {
      queue.offer(n);
    }

    @Override public boolean isEmpty() {
      return queue.isEmpty();
    }

    @Override public Node pop() {
      try {
        return queue.remove();
      } catch (NoSuchElementException cause) {
        throw new FrontierEmptyException(cause);
      }
    }

    @Override public int countOpen() {
      return queue.size();
    }

    @Override public boolean contains(Node n) {
      return queue.contains(n);
    }
  }

  public static <Node> Supplier<Queue<Node>> queueFactory() {
    return new Supplier<Queue<Node>>() {
      public Queue<Node> get() {
        return new Queue<Node>();
      }
    };

  }

}

package algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptSolVertexCover {
  private int[][] adj;
  private int[] deg; // current degree (ignoring removed vertices)
  private boolean[] removed;
  private int n, edgesLeft, best;
  private long deadline, calls;
  private boolean timedOut;

  /** @return size of the optimal cover, or -1 if it timed out */
  public static int solve(int n, List<int[]> edges, long timeoutMs) {
    return new OptSolVertexCover().run(n, edges, timeoutMs);
  }

  private int run(int n, List<int[]> edges, long timeoutMs) {
    this.n = n;

    // build adjacency lists, ignoring duplicate edges
    List<List<Integer>> tmp = new ArrayList<>();
    for (int i = 0; i < n; i++)
      tmp.add(new ArrayList<>());
    Set<Long> seen = new HashSet<>();
    for (int[] e : edges) {
      int a = Math.min(e[0], e[1]), b = Math.max(e[0], e[1]);
      if (a == b || !seen.add((long) a * n + b))
        continue;
      tmp.get(a).add(b);
      tmp.get(b).add(a);
    }
    adj = new int[n][];
    deg = new int[n];
    for (int i = 0; i < n; i++) {
      adj[i] = tmp.get(i).stream().mapToInt(Integer::intValue).toArray();
      deg[i] = adj[i].length;
      edgesLeft += deg[i];
    }
    edgesLeft /= 2;

    removed = new boolean[n];
    best = n; // all vertices is always a valid cover
    deadline = System.nanoTime() + timeoutMs * 1_000_000L;

    search(0);
    return timedOut ? -1 : best;
  }

  private void search(int size) {
    if (timedOut)
      return;
    if ((++calls & 1023) == 0 && System.nanoTime() > deadline) {
      timedOut = true;
      return;
    }
    if (edgesLeft == 0) {
      best = Math.min(best, size);
      return;
    }

    // pick the vertex with max degree
    int v = -1, maxDeg = 0;
    for (int i = 0; i < n; i++) {
      if (!removed[i] && deg[i] > maxDeg) {
        maxDeg = deg[i];
        v = i;
      }
    }

    // lower bound: each vertex covers at most maxDeg edges
    if (size + (edgesLeft + maxDeg - 1) / maxDeg >= best)
      return;

    // Branch 1: v is in the cover
    remove(v);
    search(size + 1);
    restore(v);

    // Branch 2: v is NOT in the cover -> all its neighbours are
    List<Integer> nb = new ArrayList<>();
    for (int u : adj[v])
      if (!removed[u])
        nb.add(u);
    if (size + nb.size() < best) {
      for (int u : nb)
        remove(u);
      search(size + nb.size());
      for (int i = nb.size() - 1; i >= 0; i--)
        restore(nb.get(i));
    }
  }

  private void remove(int v) {
    removed[v] = true;
    edgesLeft -= deg[v];
    for (int u : adj[v])
      if (!removed[u])
        deg[u]--;
  }

  private void restore(int v) { // must be called in reverse order of remove()
    for (int u : adj[v])
      if (!removed[u])
        deg[u]++;
    edgesLeft += deg[v];
    removed[v] = false;
  }
}

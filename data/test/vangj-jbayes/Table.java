package com.github.vangj.jbayes.inf.exact.sampling;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;

import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.Variable;
import com.github.vangj.jbayes.inf.exact.graph.util.NodeUtil;
import java.util.ArrayList;
import java.util.Arrays;import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Table {

  private final Node node;
  private final List<Node> parents;
  private final List<String> parentIds;
  private Map<String, List<Double>> probs;

  public Table(Node node, List<Node> parents) {
    this.node = node;
    parents.sort((n1, n2) -> n1.getId().compareTo(n2.getId()));
    this.parents = parents;
    this.parentIds = this.parents.stream()
        .map(Variable::getId)
        .collect(Collectors.toList());

    this.probs = new HashMap<>();
    if (0 == this.parents.size()) {
      List<Double> cumsums = getCumsums(node.probs());
      this.probs.put("default", cumsums);
    } else {
      List<List<String>> lists = parents.stream()
          .map(Variable::getValues)
          .map(s -> (List<String>) new ArrayList(s))
          .collect(Collectors.toList());

      List<List<String>> cartesian = NodeUtil.product(lists);
      List<String> keys = cartesian.stream()
          .map(values -> {
              // BLOCKTEST EVAL: https://github.com/vangj/jbayes/blob/4f3a542a4cab356b2b4a3d5d735ae0346429ef2d/src/main/java/com/github/vangj/jbayes/inf/exact/sampling/Table.java#L39-L52
            blocktest().given(this.parents, Arrays.asList(Node.builder().id("foo").build(), Node.builder().id("bar").build(), Node.builder().id("baz").build()))
                .given(values, new ArrayList<>(Arrays.asList("val1", "val2", "val3")))
                .checkReturnEq("foo=val1,bar=val2,baz=val3");
            blocktest().given(this.parents, new ArrayList<>())
                .given(values, new ArrayList<>(Arrays.asList("val1", "val2", "val3")))
                .checkReturnEq("");
            final int paSize = this.parents.size();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < paSize; i++) {
              Node n = this.parents.get(i);
              String v = values.get(i);
              String s = n.getId() + "=" + v;
              sb.append(s);
              if (i < paSize - 1) {
                sb.append(",");
              }
            }
            return sb.toString();
          })
          .collect(Collectors.toList());

      final int n = node.getValues().size();
      List<Double> nodeProbs = node.probs();
      List<List<Double>> probs = NodeUtil.groupList(n, nodeProbs).stream()
          .map(Table::getCumsums)
          .collect(Collectors.toList());

      this.probs = new HashMap<>();
      final int numPairs = keys.size();
      for (int i = 0; i < numPairs; i++) {
        String k = keys.get(i);
        List<Double> v = probs.get(i);
        this.probs.put(k, v);
      }
    }
  }

  private static List<Double> getCumsums(List<Double> probs) {
    final int size = probs.size();
    List<Double> cumsums = new ArrayList<>();
    cumsums.add(probs.get(0));
    for (int i = 1; i < size; i++) {
      double sum = cumsums.get(i - 1) + probs.get(i);
      cumsums.add(sum);
    }
    return cumsums;
  }

  public String getValue(final double prob, Map<String, String> sample) {
    if (!this.hasParents()) {
      List<Double> probs = this.probs.get("default");
      int index = NodeUtil.bisectRight(probs, prob);
      return node.getValueList().get(index);
    } else {
      String k = this.parentIds.stream()
          .map(id -> id + "=" + sample.get(id))
          .collect(Collectors.joining(","));
      List<Double> probs = this.probs.get(k);
      int index = NodeUtil.bisectRight(probs, prob);
      return node.getValueList().get(index);
    }
  }

  public boolean hasParents() {
    return parents.size() > 0;
  }

  public Map<String, List<Double>> getProbs() {
    return probs;
  }

}

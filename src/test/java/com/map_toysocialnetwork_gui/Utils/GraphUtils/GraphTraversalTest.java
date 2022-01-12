package com.map_toysocialnetwork_gui.Utils.GraphUtils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GraphTraversalTest {

    private Map<Integer, List<Integer>> generateAdjLists() {
        Map<Integer, List<Integer>> adjLists = new HashMap<>();
        adjLists.put(1, List.of(2,3,4,5));
        adjLists.put(2, List.of(1,4));
        adjLists.put(3, List.of(1,5));
        adjLists.put(4, List.of(1,2));
        adjLists.put(5, List.of(1,3));
        adjLists.put(6, List.of(7));
        adjLists.put(7, List.of(6));
        return adjLists;
    }

    private Map<Integer, List<Integer>> generateAdjLists2() {
        Map<Integer, List<Integer>> adjLists = new HashMap<>();
        adjLists.put(1, List.of(2));
        adjLists.put(2, List.of(1,3,4));
        adjLists.put(3, List.of(2,6));
        adjLists.put(4, List.of(2,5));
        adjLists.put(5, List.of(4,6));
        adjLists.put(6, List.of(3,5,7));
        adjLists.put(7, List.of(6));
        return adjLists;
    }

    @Test
    void allNodesDFS() {
        GraphTraversal<Integer> graphTraversal = new GraphTraversal<>(generateAdjLists());
        var parents = graphTraversal.AllNodesDFS();
        int components = 0;
        for(var parent:parents.values()) {
            if (parent==null) {
                components++;
            }
        }
        assertEquals(components,2);
    }

    @Test
    void longestRoad() {
        GraphTraversal<Integer> graphTraversal = new GraphTraversal<>(generateAdjLists2());
        var longestRoad = graphTraversal.computeLongestRoad();
        assertEquals(longestRoad.size(),6);
    }
}
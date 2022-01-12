package com.map_toysocialnetwork_gui.Utils.GraphUtils;

import com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException;

import java.util.*;

/**
 * Collection of graph traversal algorithms.
 * @param <E> type of the nodes in the graph.
 */
public class GraphTraversal<E> {
    private Map<E,E> parents;
    private Map<E, Boolean> visited;
    private Map<E, List<E>> adjLists;
    private List<E> currentPath;
    private List<E> longestPath;
    private List<E> domain;

    /**
     * Creates a new graph traversal object.
     * @param adjLists the adjacency list of a graph.
     */
    public GraphTraversal(Map<E, List<E>> adjLists) {
        this.adjLists = adjLists;
    }

    /**
     * Performs a depth-first search on all nodes. Updates the parents and distance attributes accordingly.
     * @return parents updated after the DFS.
     */
    public Map<E, E> AllNodesDFS() {
        parents = new HashMap<>();
        visited = new HashMap<>();
        adjLists.forEach((K,V)->{
            parents.put(K,null);
            visited.put(K,false);
        });
        for(E node:adjLists.keySet()) {
            if (!visited.get(node)) {
                DFSVisit(node);
            }
        }
        return parents;
    }

    /**
     * Performs a depth-first search from a given source.
     * @param source E type object representing the source of the DFS.
     * @return parents after the DFS.
     */
    public Map<E, E> SingleSourceDFS(E source) {
        if (adjLists.get(source)==null) {
            throw new ServiceException("Invalid source!");
        }
        parents = new HashMap<>();
        visited = new HashMap<>();
        adjLists.forEach((K,V)->{
            parents.put(K,null);
            visited.put(K,false);
        });
        DFSVisit(source);
        return parents;
    }

    /**
     * Recursive method used in a DFS.
     * @param node E type object representing the source of the local search.
     */
    private void DFSVisit(E node) {
        visited.put(node, true);
        for(E neighbour:adjLists.get(node)) {
            if(!visited.get(neighbour)) {
                parents.put(neighbour, node);
                DFSVisit(neighbour);
            }
        }
    }

    /**
     * Computes the longest road in a given graph.
     * @return a list representing the list of nodes that form the longest road.
     */
    public List<E> computeLongestRoad() {
        currentPath = new ArrayList<>();
        longestPath = new ArrayList<>();
        domain = adjLists.keySet().stream().toList();
        backtrack(domain.iterator());
        return longestPath;
    }

    private void backtrack(Iterator<E> iterator) {
        E el = iterator.next();
        currentPath.add(el);
        while(el!=null) {
            currentPath.set(currentPath.size()-1, el);
            if (consistent()) {
                outputSolution();
                backtrack(domain.iterator());
            }
            if (iterator.hasNext()) {
                el = iterator.next();
            }
            else {
                el = null;
            }
        }
        currentPath.remove(currentPath.size()-1);
    }

    private void outputSolution() {
        if (currentPath.size()>longestPath.size()) {
            longestPath = new ArrayList<>(currentPath);
        }
    }

    private boolean consistent() {
        var size = currentPath.size();
        if(size==1) {
            return true;
        }
        for(int i=0; i<size-1; i++) {
            if(currentPath.get(i)==currentPath.get(size-1)) {
                return false;
            }
        }
        return adjLists.get(currentPath.get(size - 2)).contains(currentPath.get(size-1));
    }
}

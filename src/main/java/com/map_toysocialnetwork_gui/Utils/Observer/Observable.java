package com.map_toysocialnetwork_gui.Utils.Observer;

public interface Observable {
    void addObserver(Observer observer);
    void notifyObservers();
}

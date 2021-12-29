package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Service.Service;

public abstract class Controller {
    protected Service service;

    public void setService(Service service) {
        this.service = service;
    }
}

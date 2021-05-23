package com.example.application.views.play;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class GameOverEvent extends ComponentEvent<Component> {
    public GameOverEvent(Component source,
                       boolean fromClient) {
        super(source, fromClient);
    }
}

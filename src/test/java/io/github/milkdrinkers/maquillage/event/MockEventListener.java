package io.github.milkdrinkers.maquillage.event;

@FunctionalInterface
public interface MockEventListener {
    void onEvent(MockEvent event);
}
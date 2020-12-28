package com.timecat.data.system.model.eventbus.idea;

public final class SlideChangedEvent {
    private final int status;

    public SlideChangedEvent(int i) {
        this.status = i;
    }

    public final int getStatus() {
        return this.status;
    }
}

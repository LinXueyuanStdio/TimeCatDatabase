package com.timecat.data.system.model.eventbus;

import com.timecat.data.system.model.api.BiliBiliVideo;

public class BiliBiliEvent {
    public BiliBiliVideo url;

    public BiliBiliEvent(BiliBiliVideo url) {
        this.url = url;
    }
}

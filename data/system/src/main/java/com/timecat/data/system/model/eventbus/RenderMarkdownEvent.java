package com.timecat.data.system.model.eventbus;

import com.jess.arms.utils.LogUtils;
import com.timecat.data.system.model.StringUtils;

public class RenderMarkdownEvent {

  public String content;
  public String title;

  public RenderMarkdownEvent(String content) {
    LogUtils.debugInfo("RenderMarkdownEvent");
    if (content == null) {
      this.content = "";
      title = "";
      return;
    }
    this.content = content;
    this.title = StringUtils.getShortText(content);
  }

  public RenderMarkdownEvent(String title, String content) {
    this.title = title;
    this.content = content;
  }
}

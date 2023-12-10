package com.hwamok.integration.slack;

public interface SlackApi {
    void chatPostMessage(String botName, String channel, String message);
}

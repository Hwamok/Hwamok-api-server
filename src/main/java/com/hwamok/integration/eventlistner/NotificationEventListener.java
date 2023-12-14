package com.hwamok.integration.eventlistner;

import com.hwamok.event.SlackEvent;
import com.hwamok.integration.slack.SlackApi;
import com.hwamok.integration.slack.SlackProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final SlackProperties slackProperties;
    private final SlackApi slackApi;

    @EventListener(SlackEvent.class)
    public void onSlackEvent(SlackEvent event) {
        slackApi.chatPostMessage(slackProperties.getBotName(), slackProperties.getLogChannel(), event.getMessage());
    }
}

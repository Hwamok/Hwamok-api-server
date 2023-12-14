package com.hwamok.integration.slack;

import com.hwamok.core.annotation.LocalProfile;
import org.springframework.stereotype.Component;

@Component
@LocalProfile
public class LocalSlackApi implements SlackApi {
    @Override
    public void chatPostMessage(String botName, String channel, String message) {

    }
}

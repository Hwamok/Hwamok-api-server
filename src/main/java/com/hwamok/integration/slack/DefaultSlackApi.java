package com.hwamok.integration.slack;

import com.hwamok.core.annotation.AppProfile;
import com.slack.api.methods.MethodsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@AppProfile
@Component
@RequiredArgsConstructor
public class DefaultSlackApi implements SlackApi {

    private final MethodsClient slackClient;
    private final Environment environment;

    @Override
    public void chatPostMessage(String botName, String channel, String message) {
        try {
            slackClient.chatPostMessage(builder -> builder
                    .username(botName)
                    .channel(channel)
                    .text("```" + message + " - " + Arrays.stream(environment.getActiveProfiles()).findFirst().orElse("check profile") + "```")
            );
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}

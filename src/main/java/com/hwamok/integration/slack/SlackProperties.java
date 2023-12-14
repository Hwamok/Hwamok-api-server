package com.hwamok.integration.slack;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlackProperties {
    private String botToken;
    private String logChannel;
    private String botName = "Hwamok-Bot";
}

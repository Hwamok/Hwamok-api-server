package com.hwamok.event;


import com.hwamok.core.exception.Exceptions;

public class UnhandledExceptionEvent extends SlackEvent {

    public UnhandledExceptionEvent(Exception ex) {
        super("```<!channel> {message} ```"
                .replace("{message}", Exceptions.simplifyMessage(ex))
        );
    }
}

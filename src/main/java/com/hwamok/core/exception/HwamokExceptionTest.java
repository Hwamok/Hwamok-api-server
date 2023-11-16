package com.hwamok.core.exception;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableTypeAssert;

import static org.assertj.core.api.ThrowableAssert.*;

public class HwamokExceptionTest {
    private final ExceptionCode exceptionCode;
    private final ThrowableTypeAssert<HwamokException> throwableTypeAssert;

    public HwamokExceptionTest(ExceptionCode exceptionCode, ThrowableTypeAssert<HwamokException> throwableTypeAssert) {
        this.exceptionCode = exceptionCode;
        this.throwableTypeAssert = throwableTypeAssert;
    }

    public static HwamokExceptionTest assertThatHwamokException(ExceptionCode exceptionCode) {
        return new HwamokExceptionTest(exceptionCode, Assertions.assertThatExceptionOfType(HwamokException.class));
    }

    public void isThrownBy(ThrowingCallable throwingCallable) {
        this.throwableTypeAssert.isThrownBy(throwingCallable).withMessage(exceptionCode.name());
    }

}

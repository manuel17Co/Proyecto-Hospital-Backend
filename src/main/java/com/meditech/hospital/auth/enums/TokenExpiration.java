package com.meditech.hospital.auth.enums;

public abstract class TokenExpiration {
    private static final long ACCESS = 60 * 60 * 2L;
    private static final long REFRESH = 60 * 60 * 24 * 7L;
    private static final long PASSWORD_RESET = 60 * 10L;
    private static final long VERIFICATION = 60 * 10L;

    public static long get(TokenType tokenType) {
        return switch (tokenType) {
            case ACCESS -> ACCESS;
            case REFRESH -> REFRESH;
            case PASSWORD_RESET -> PASSWORD_RESET;
            case VERIFICATION -> VERIFICATION;
        };
    }
}
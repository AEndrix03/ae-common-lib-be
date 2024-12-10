package it.aredegalli.commons.enums.auth;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    USERNAME("USERNAME"),
    EMAIL("EMAIL");

    private final String symbol;

    public static LoginTypeEnum fromSymbol(String symbol) {
        return Arrays.stream(LoginTypeEnum.values())
                .filter(type -> type.symbol.equalsIgnoreCase(symbol))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid symbol: " + symbol));
    }
    
    @Component
    static class LoginTypeEnumConverter implements Converter<String, LoginTypeEnum> {
        @Override
        public LoginTypeEnum convert(@NotNull String source) {
            for (LoginTypeEnum value : LoginTypeEnum.values()) {
                if (value.getSymbol().equalsIgnoreCase(source)) {
                    return value;
                }
            }
            throw new IllegalArgumentException("Invalid LoginTypeEnum: " + source);
        }
    }
}

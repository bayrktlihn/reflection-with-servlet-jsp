package io.bayrktlihn.reflectionwithservletjsp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;


@Getter
@Setter
@NoArgsConstructor
public class RequestHandlerCommand {

    private Object controller;
    private Method method;
    private String path;

}

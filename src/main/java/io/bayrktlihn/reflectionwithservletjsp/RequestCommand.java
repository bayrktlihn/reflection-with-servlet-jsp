package io.bayrktlihn.reflectionwithservletjsp;

import io.bayrktlihn.reflectionwithservletjsp.enums.RequestMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;


@Getter
@Setter
@NoArgsConstructor
public class RequestCommand {

    private Object controller;
    private Method toBeInvokedMethod;
    private RequestMethod requestMethod;
    private String path;

}

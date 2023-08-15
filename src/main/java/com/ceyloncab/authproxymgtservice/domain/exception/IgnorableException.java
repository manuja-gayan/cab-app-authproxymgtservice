package com.ceyloncab.authproxymgtservice.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IgnorableException extends RuntimeException{
    private String msg;
}

package com.libertex.qa.challenge.api.model.response;

import com.libertex.qa.core.api.model.BaseModel;
import lombok.Getter;

@Getter
public class HelloData extends BaseModel {
    private String resultCode;
    private String message;
}

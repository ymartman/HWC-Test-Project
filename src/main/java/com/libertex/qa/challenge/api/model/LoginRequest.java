package com.libertex.qa.challenge.api.model;

import com.libertex.qa.core.api.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class LoginRequest extends BaseModel {
    private String username;
}

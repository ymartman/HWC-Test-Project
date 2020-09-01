package com.libertex.qa.challenge.api.model.response;

import com.libertex.qa.core.api.model.BaseModel;
import lombok.Getter;

import java.util.List;

@Getter
public class ClientsData extends BaseModel {
    private String resultCode;
    //TODO поидее должна возвращаться ClientData {fullName username}
    private List<String> clients;
}

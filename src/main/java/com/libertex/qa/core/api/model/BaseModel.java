package com.libertex.qa.core.api.model;

import com.google.gson.GsonBuilder;

public abstract class BaseModel {

    @Override
    public String toString(){
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}

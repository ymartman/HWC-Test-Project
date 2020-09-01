package com.libertex.qa.challenge.config;

import lombok.Getter;
import org.aeonbits.owner.ConfigFactory;

public class GlobalConfig {

    /** singleton instance to escape multiple reading of file sustem*/
    private final static GlobalConfig  instance = new GlobalConfig();

    /**
     * Information about challenge api
     */
    @Getter
    private final ServiceConfig challenge;

    /**
     * @return instance with set of configurations
     */
    public static GlobalConfig getInstance(){
        return instance;
    }

    private GlobalConfig(){
        challenge = ConfigFactory.create(ServiceConfig.class);
    }
}

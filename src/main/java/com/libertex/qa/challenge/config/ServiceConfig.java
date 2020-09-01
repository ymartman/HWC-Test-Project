package com.libertex.qa.challenge.config;

import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.*;

@LoadPolicy(LoadType.MERGE)
@Sources({
        "system:properties",
        "system:env",
        "classpath:challenge.properties",
         })
public interface ServiceConfig extends Config {

    @Key("challenge.host.name")
    String hostName();

    @Key("challenge.session.bearer")
    String bearerName();
}

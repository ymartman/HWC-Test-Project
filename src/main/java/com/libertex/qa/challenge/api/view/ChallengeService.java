package com.libertex.qa.challenge.api.view;

import com.libertex.qa.challenge.api.controller.BaseService;

public class ChallengeService extends BaseService<IChallenge> {

    @Override
    protected Class<IChallenge> getInterfaceClass() {
        return IChallenge.class;
    }

    public ChallengeService setXSessionId(String id){
        addHeader("X-Session-Id", id);
        return this;
    }
}

package com.zerox.base.demo;

import com.zerox.base.annotation.Action;
import com.zerox.base.annotation.Inject;

@Action
public class PersonController {
    @Inject()
    private PersonService personService;

    public void say(){
        personService.say();
    }

}

package com.zerox.base.demo;

import com.zerox.base.annotation.Action;
import com.zerox.base.annotation.Inject;
import com.zerox.base.annotation.RequestMapping;

@Action
@RequestMapping(value = "/")
public class PersonController {
    @Inject()
    private PersonService personService;

    @RequestMapping(value = "/index")
    public void say(){
        personService.say();
    }

}

package com.zerox.base.demo;

import com.zerox.base.annotation.Action;
import com.zerox.base.annotation.Inject;
import com.zerox.base.annotation.RequestMapping;
import com.zerox.base.annotation.Service;

@Action
@RequestMapping(value = "/")
@Service
public class PersonControllererer {
    @Inject()
    private PersonService personService;

    @RequestMapping(value = "/index")
    public void say(){
        personService.say();
    }

}

package com.zerox.base.demo;

import com.zerox.base.annotation.Service;

@Service(name = "personService")
public class PersonService {

    public void say(){
        System.out.println("person service say...");
    }
}

package com.zerox.base.server;

import com.zerox.base.annotation.Service;

@Service(name = "personService")
public class PersonService {

    public String say(){
        String x = "person service say...";
        System.out.println(x);
        return x;
    }
}

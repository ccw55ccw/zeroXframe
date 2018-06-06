package com.zerox.base.server;

import com.zerox.base.annotation.Action;
import com.zerox.base.annotation.Inject;
import com.zerox.base.annotation.RequestMapping;
import com.zerox.base.annotation.Service;

import java.util.HashMap;
import java.util.Map;

@Action
@RequestMapping(value = "/")
@Service
public class PersonController {
    @Inject()
    private PersonService personService;

    @RequestMapping(value = "/index")
    public Map<String, Object> index(String jsonParams){
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("info", personService.say());
        }};
        return map;
    }

}

package com.zerox.base.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Bean {

    private String name;

    private String className;

    private String scope;

    private List<BeanProperty> properties = new ArrayList<BeanProperty>();
}

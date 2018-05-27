package com.zerox.base.web;

import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Data
public class Request {
    private String requestMethod;
    private String requestPath;

    @Override
    public int hashCode(){
       return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
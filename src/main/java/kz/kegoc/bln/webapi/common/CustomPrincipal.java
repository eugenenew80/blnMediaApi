package kz.kegoc.bln.webapi.common;

import kz.kegoc.bln.entity.adm.User;

import java.security.Principal;

public class CustomPrincipal implements Principal {

    public CustomPrincipal(String name, User user) {
        this.user = user;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    private String name;
    private User user;
}

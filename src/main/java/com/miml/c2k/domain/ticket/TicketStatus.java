package com.miml.c2k.domain.ticket;

import lombok.Getter;

@Getter
public enum TicketStatus {
    ACTIVE("ticket-active"),
    INACTIVE("ticket-inactive"),
    BEFORE_PAYMENT("ticket-before-payment"),
    CANCELED("ticket-canceled");

    private final String thymeleafClass;

    TicketStatus(String thymeleafClass) {
        this.thymeleafClass = thymeleafClass;
    }

}

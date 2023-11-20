package com.example.Phone.Pay.management.usage_classes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelfTransfer {
    String accountFrom;
    String accountTo;
    Long amount;
    String pin;
}

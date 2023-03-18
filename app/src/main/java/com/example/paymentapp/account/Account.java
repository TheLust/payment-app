package com.example.paymentapp.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class Account implements Serializable {

    private String uuid;
    private String password;
    private Float amount;

}

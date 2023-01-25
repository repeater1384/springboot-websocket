package com.example.demo.test;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Customer {
    String id;
    String name;
    String wido;
    String gyungdo;
}

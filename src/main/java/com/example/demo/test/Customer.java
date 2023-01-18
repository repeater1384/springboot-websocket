package com.example.demo.test;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class Customer {
    String id;
    String name;
    String wido;
    String gyungdo;
}

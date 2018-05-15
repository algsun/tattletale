package com.microwise.tattletale.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * WebSocket Demo entity
 *
 * @author li.jianfei
 * @since 2017/12/11
 */
@Data
@AllArgsConstructor
public class Greeting {
    private String content;
}

package com.microwise.tattletale.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 基类
 *
 * @author sun.cong
 * @create 2017-11-20 11:31
 **/
@Data
public class Base implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 名称
     */
    private String name;
}

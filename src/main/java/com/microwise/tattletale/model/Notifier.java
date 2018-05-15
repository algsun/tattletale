package com.microwise.tattletale.model;

import java.io.Serializable;

/**
 * 通知人
 *
 * @author sun.cong
 * @create 2017-12-04 16:55
 **/
public class Notifier implements Serializable {
    /**
     * 通知人唯一标识
     */
    private String id;
    /**
     * 通知人姓名
     */
    private String name;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 电话
     */
    private String mobile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notifier)) return false;

        Notifier notifier = (Notifier) o;

        if (!getId().equals(notifier.getId())) return false;
        if (!getName().equals(notifier.getName())) return false;
        if (!getEmail().equals(notifier.getEmail())) return false;
        return getMobile().equals(notifier.getMobile());

    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + getMobile().hashCode();
        return result;
    }
}

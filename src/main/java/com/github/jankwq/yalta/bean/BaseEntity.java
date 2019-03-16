package com.github.jankwq.yalta.bean;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
public abstract class BaseEntity implements BaseBean {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Date createTime = new Date();

    private Date updateTime = createTime;

}

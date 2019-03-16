package com.github.jankwq.yalta.bean.updater;

import lombok.extern.slf4j.Slf4j;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

/**
 * @author yinjianfeng
 * @date 2018/12/18
 */
@Slf4j
public class AttributesUpdater extends BaseUpdater {


    private final Attributes attributes;

    public AttributesUpdater(Attributes attributes){
        this.attributes = attributes;
    }

    @Override
    Object getValue(String name) {
        try {
            Attribute attribute = attributes.get(name);
            if (attribute != null){
                return attribute.get();
            }
        } catch (NamingException e){
            //
        }
        return null;
    }

}

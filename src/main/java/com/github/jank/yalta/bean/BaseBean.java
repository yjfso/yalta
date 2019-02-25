package com.github.jank.yalta.bean;

import com.github.jank.yalta.bean.updater.BaseUpdater;

import java.io.Serializable;

public interface BaseBean extends Serializable {

    default void update(BaseUpdater updater){
        updater.run(this);
    }

}

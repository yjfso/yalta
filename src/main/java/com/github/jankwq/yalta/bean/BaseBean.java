package com.github.jankwq.yalta.bean;

import com.github.jankwq.yalta.bean.updater.BaseUpdater;

import java.io.Serializable;

public interface BaseBean extends Serializable {

    default void update(BaseUpdater updater){
        updater.run(this);
    }

}

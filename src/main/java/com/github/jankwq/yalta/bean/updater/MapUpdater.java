package com.github.jankwq.yalta.bean.updater;

import java.util.Map;

/**
 * @author yinjianfeng
 * @date 2018/12/18
 */
public class MapUpdater extends BaseUpdater {

    private final Map map;

    public MapUpdater(Map map){
        this.map = map;
    }

    @Override
    Object getValue(String name) {
        return map.get(name);
    }
}

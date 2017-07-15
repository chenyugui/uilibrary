package com.taichuan.uilibrary.icon;

import com.joanzapata.iconify.Icon;

/**
 * Created by gui on 2017/7/7.
 */

public enum MyIcons implements Icon {
    icon_back('\ue69b'),// 复制下载的icon的Unicode值，去掉; 和 &#x
    ;

    private char character;

    MyIcons(char character) {
        this.character = character;
    }

    @Override
    public String key() {
        return name().replace('_', '-');
    }

    @Override
    public char character() {
        return character;
    }
}

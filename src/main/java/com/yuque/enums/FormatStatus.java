package com.yuque.enums;

import com.yuque.enums.core.IEnum;
import lombok.Getter;

/**
 * @author 11029
 * @description 语雀Format枚举类
 * @since 2023/8/2 7:51:59
 */
public enum FormatStatus implements IEnum {
    MARKDOWN("markdown", "一种优化的富文本格式，适合编写笔记"),
    LAKE("lake", "语雀专有格式"),
    HTML("html", "网页格式");

    FormatStatus(String format, String desc) {
        this.format = format;
        this.desc = desc;
    }

    @Getter
    private final String format;
    @Getter
    private final String desc;

    @Override
    public String getValue() {
        return format;
    }
}

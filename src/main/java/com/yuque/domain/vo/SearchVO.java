package com.yuque.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 11029
 * @description
 * @since 2023/8/2 12:21:18
 */
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
public class SearchVO {
    private String id;
    private String type;
    private String title;
    private String summary;
    private String url;
    private String info;
    private SearchTargetVO target;
}

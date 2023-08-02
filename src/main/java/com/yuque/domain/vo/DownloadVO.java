package com.yuque.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author 11029
 * @description
 * @since 2023/8/2 8:41:55
 */
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
public class DownloadVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String body;
    private String title;
    private String slug;
}

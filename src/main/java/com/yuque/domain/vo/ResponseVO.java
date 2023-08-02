package com.yuque.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 11029
 * @description
 * @since 2023/8/1 19:50:53
 */
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ResponseVO<T>{
    private T data;
}

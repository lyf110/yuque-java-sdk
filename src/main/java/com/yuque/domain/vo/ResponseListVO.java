package com.yuque.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 11029
 * @description
 * @since 2023/8/1 20:09:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseListVO <T>{
    List<T> data;
}

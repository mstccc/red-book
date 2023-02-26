package com.achong.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FansVo {
    private String fanId;
    private String nickName;
    private String face;
    private boolean isFried = false;
}

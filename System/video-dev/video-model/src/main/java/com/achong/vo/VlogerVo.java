package com.achong.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VlogerVo {
    private String vlogerId;
    private String nickName;
    private String face;
    private boolean isFollowed = true;
}

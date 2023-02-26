package com.achong.mapper;

import com.achong.my.mapper.MyMapper;
import com.achong.pojo.Fans;
import com.achong.vo.FansVo;
import com.achong.vo.VlogerVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FansMapperCustom extends MyMapper<Fans> {
    public List<VlogerVo> queryMyFollows(@Param("paramMap")Map<String, Object> map);

    public List<FansVo> queryMyFans(@Param("paramMap")Map<String, Object> map);
}
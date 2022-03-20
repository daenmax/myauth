package cn.daenx.myauth.base.vo;

import lombok.Data;

@Data
public class DataRanking {
    private String type;
    private String content;
    private Integer num;
    private Integer fromSoftId;
    private Integer pageIndex;
    private Integer pageSize;
}

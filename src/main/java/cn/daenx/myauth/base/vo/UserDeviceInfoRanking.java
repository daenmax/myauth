package cn.daenx.myauth.base.vo;

import lombok.Data;

@Data
public class UserDeviceInfoRanking {
    private String deviceInfo;
    private Integer num;
    private Integer fromSoftId;
    private Integer pageIndex;
    private Integer pageSize;
}

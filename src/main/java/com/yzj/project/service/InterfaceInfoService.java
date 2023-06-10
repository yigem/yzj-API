package com.yzj.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzj.yzjcommon.model.entity.InterfaceInfo;

/**
* @author bx
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-04-01 16:59:46
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}

package com.example.demo.service;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.service </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/12/9 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.example.demo.dao.FunDescDao;
import com.example.demo.entity.FunDescEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FunDescService {
    @Resource
    private FunDescDao funDescDao;

    public List<FunDescEntity> selectByProcedureId(Integer procedureId){
        return funDescDao.selectByProcedureId(procedureId);
    }

    public List<FunDescEntity> selectByProjectIdAndVersion(Integer prject, String version){
        return funDescDao.selectByProjectIdAndVersion(prject,version);
    }

    public List<FunDescEntity> selectByFullName(String fullName){
        List<FunDescEntity> resList = new ArrayList<>();
        List<FunDescEntity> list = funDescDao.selectByFullName(fullName);
        for(FunDescEntity fe: list){
            if(!"".equals(fe.getComment())){
                resList.add(fe);
                break;
            }
        }
        return resList;
    }

    public void add(FunDescEntity entity){
        List<FunDescEntity> old = funDescDao.selectByProcedureId(entity.getProcedureId());
        if(old != null && old.size()>0){
            entity.setId(old.get(0).getId());
            funDescDao.updateByPrimaryKey(entity);
        }else{
            funDescDao.insert(entity);
        }
    }
}

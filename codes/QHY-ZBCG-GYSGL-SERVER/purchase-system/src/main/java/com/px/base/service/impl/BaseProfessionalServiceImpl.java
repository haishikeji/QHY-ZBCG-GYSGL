package com.px.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.base.domain.BaseProfessional;
import com.px.base.domain.vo.BaseProfessionalVo;
import com.px.base.mapper.BaseProfessionalMapper;
import com.px.base.service.BaseProfessionalService;
import com.px.common.core.domain.AjaxResult;
import com.px.common.exception.ServiceException;
import com.px.common.utils.StringUtils;
import com.px.common.utils.bean.BeanUtils;
import com.px.common.utils.bean.BeanValidators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class BaseProfessionalServiceImpl extends ServiceImpl<BaseProfessionalMapper, BaseProfessional> implements BaseProfessionalService {

    @Autowired
    private BaseProfessionalMapper baseProfessionalMapper;
    @Autowired
    protected Validator validator;

    @Override
    public void deleteIds(List<Integer> deleteIds) {
        for (Integer deleteId : deleteIds) {
            BaseProfessional baseProfessional = baseProfessionalMapper.selectById(deleteId);
            List<BaseProfessional> baseProfessionalList = baseProfessionalMapper.selectByParentCode(baseProfessional.getProfessionalCode());
//            int ii = baseProfessionalMapper.countByParentCode(baseProfessional.getProfessionalCode());
            if (baseProfessionalList.size() > 0) {
                throw new ServiceException(String.format("%1$s已有子级,不能删除", baseProfessional.getProfessionalName()));
            }
            baseProfessionalMapper.deleteById(deleteId);
        }
    }

    @Override
    public String importBaseProfessional(List<BaseProfessional> baseProfessionalList, boolean updateSupport) {
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (BaseProfessional baseProfessional : baseProfessionalList) {
            try {
                // 判断编码是否存在
                List<BaseProfessional> professionalCode = baseProfessionalMapper.selectByProfessionalCode(baseProfessional);
                List<BaseProfessional> professionalName = baseProfessionalMapper.selectByProfessionalName(baseProfessional);
                //List<BaseProfessional> parentCode = baseProfessionalMapper.selectByParentCodes(baseProfessional);
                if (professionalCode.size() > 0 || professionalName.size() > 0) {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、编码或专业名称" + baseProfessional.getProfessionalCode() + "、" + baseProfessional.getProfessionalName() + " 已存在");
                } else {
                    // bean对象属性认证
                    BeanValidators.validateWithException(validator, baseProfessional);
                    baseProfessionalMapper.insert(baseProfessional);
                    successNum++;
                    // successMsg.append("<br/>" + successNum + "、专业名称 " + baseProfessional.getProfessionalName() + " 导入成功");
                }
            } catch (Exception e) {
                String msg = "<br/>" + failureNum + "、专业名称 " + baseProfessional.getProfessionalName() + " 导入失败：";
                failureMsg.append(msg);
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条。<br/>");
        }
        return successMsg.toString();
    }

    @Override
    public AjaxResult downloadPathFile(String downloadPath, HttpServletRequest request, HttpServletResponse response) {
        //设置文件路径
        File file = new File(downloadPath);
        //获取文件名称
        String fileName = file.getName();
        //判断文件是否存在
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                file.delete();
                return AjaxResult.success("下载成功");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return AjaxResult.success("下载失败");
    }

    /**
     * 修改--  判断当前修改数据是否存在子级数据，存在不能修改
     *
     * @param baseProfessional
     * @return
     */
    @Override
    public int updateProfessional(BaseProfessional baseProfessional) {
        BaseProfessional baseProfessional1 = baseProfessionalMapper.selectById(baseProfessional.getId());
        if (!StringUtils.isNull(baseProfessional.getProfessionalCode())) {
            List<BaseProfessional> baseProfessionalList = baseProfessionalMapper.selectByParentCode(baseProfessional1.getProfessionalCode());
            if (baseProfessionalList.size() > 0) {
                throw new ServiceException(String.format("当前数据编码已有子级,不能修改编码！", baseProfessional.getProfessionalName()));
            }
        }
        return baseProfessionalMapper.updateById(baseProfessional);
    }

    @Override
    public int deleteId(Integer id) {
        BaseProfessional baseProfessional = baseProfessionalMapper.selectById(id);
        List<BaseProfessional> baseProfessionalList = baseProfessionalMapper.selectByParentCode(baseProfessional.getProfessionalCode());
        if (baseProfessionalList.size() > 0) {
            throw new ServiceException(String.format("当前数据编码已有子级,不能删除！", baseProfessional.getProfessionalName()));
        }
        return baseProfessionalMapper.deleteById(baseProfessional);
    }

    @Override
    public List<BaseProfessionalVo> selectBaseProfessionalVo(BaseProfessionalVo baseProfessionalVo) {
        List<BaseProfessional> allList = new ArrayList<>();
        List<BaseProfessional> list = baseProfessionalMapper.selectBaseProfessional(baseProfessionalVo);
        allList.addAll(list);
        // 传惨"品目名称professionalName" 需查询是否有上级数据
        if (StringUtils.isNotNull(baseProfessionalVo.getProfessionalName())) {
            for (BaseProfessional baseProfessional : list) {
                // 遍历数据父级code 在列表中是否存在；如果不存在查询code等于当前数据父级code的数据，放入列表
                List<BaseProfessional> listStream = new ArrayList<>();
                listStream = listStream(list, listStream, baseProfessional.getParentCode());
                allList.addAll(listStream);
            }
        }
        // 将实体类转换为Vo类
        List<BaseProfessionalVo> allListVo = BeanUtils.entityListToVOList(allList, BaseProfessionalVo.class);
        List<BaseProfessionalVo> treeList = buildTree(allListVo);
        return treeList;
    }

    @Override
    public List<BaseProfessional> selectBaseProfessional(BaseProfessionalVo baseProfessionalVo) {
        List<BaseProfessional> allList = baseProfessionalMapper.selectBaseProfessional(baseProfessionalVo);
        return allList;
    }

    /**
     * 遍历list 是否存在当前数据code
     */
    public List<BaseProfessional> listStream(List<BaseProfessional> list, List<BaseProfessional> listStream, String parentCode) {
        // 遍历数据父级code 在列表中是否存在；如果不存在查询code等于当前数据父级code的数据，放入列表
        boolean check = list.stream().anyMatch(voList -> voList.getProfessionalCode().equals(parentCode));
        if (!check) {
            BaseProfessional baseProfessional = baseProfessionalMapper.selectByCode(parentCode);
            if (StringUtils.isNotNull(baseProfessional)) {
                listStream.add(baseProfessional);
                if (!"0".equals(baseProfessional.getParentCode())) {
                    listStream(list, listStream, baseProfessional.getParentCode());
                }
            }
        }
        return listStream;
    }

    /**
     * 构建树形结构
     *
     * @return
     */
    public static List<BaseProfessionalVo> buildTree(List<BaseProfessionalVo> allList) {
        //创建集合封装最终数据
        List<BaseProfessionalVo> trees = new ArrayList<>();
        //遍历所有菜单集合
        for (BaseProfessionalVo baseProfessional : allList) {
            //找到递归入口，parentId=0
            if (baseProfessional.getParentCode().equals("0")) {
                trees.add(findChildren(baseProfessional, allList));
            }
        }
        return trees;
    }

    /**
     * 从根节点进行递归查询，查询子节点
     * 判断 编码 =  父级编码是否相同，如果相同是子节点，进行数据封装
     */
    private static BaseProfessionalVo findChildren(BaseProfessionalVo baseProfessional, List<BaseProfessionalVo> allList) {
        //数据初始化
        baseProfessional.setBaseProfessionalVoList(new ArrayList<BaseProfessionalVo>());
        //遍历递归查找
        for (BaseProfessionalVo it : allList) {
            if (baseProfessional.getProfessionalCode().equals(it.getParentCode())) {
                if (baseProfessional.getBaseProfessionalVoList() == null) {
                    baseProfessional.setBaseProfessionalVoList(new ArrayList<>());
                }
                baseProfessional.getBaseProfessionalVoList().add(findChildren(it, allList));
            }
        }
        return baseProfessional;
    }

}

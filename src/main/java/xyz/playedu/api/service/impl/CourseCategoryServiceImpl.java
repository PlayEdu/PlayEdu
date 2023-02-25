package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import xyz.playedu.api.domain.CourseCategory;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.service.CourseCategoryService;
import xyz.playedu.api.mapper.CourseCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【course_categories】的数据库操作Service实现
 * @createDate 2023-02-24 13:55:19
 */
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {

    @Override
    public List<CourseCategory> listByParentId(Integer id) {
        return list(query().getWrapper().eq("parent_id", id).orderByAsc("sort"));
    }

    @Override
    public List<CourseCategory> all() {
        return list(query().getWrapper().orderByAsc("sort"));
    }

    @Override
    public CourseCategory findOrFail(Integer id) throws NotFoundException {
        CourseCategory category = getById(id);
        if (category == null) {
            throw new NotFoundException("分类不存在");
        }
        return category;
    }

    @Override
    @Transactional
    public void deleteById(Integer id) throws NotFoundException {
        CourseCategory category = findOrFail(id);
        //更新parent_chain
        updateParentChain(category.getParentChain(), childrenParentChain(category));
        //删除记录
        removeById(category.getId());
    }

    @Override
    @Transactional
    public void update(CourseCategory category, String name, Integer parentId, Integer sort) throws NotFoundException {
        String childrenChainPrefix = childrenParentChain(category);

        CourseCategory data = new CourseCategory();
        data.setId(category.getId());

        if (!category.getName().equals(name)) {
            data.setName(name);
        }

        if (!category.getParentId().equals(parentId)) {
            data.setParentId(parentId);
            if (parentId.equals(0)) {
                data.setParentChain("");
            } else {
                CourseCategory parentCourseCategory = findOrFail(parentId);
                data.setParentChain(childrenParentChain(parentCourseCategory));
            }
        }
        if (!category.getSort().equals(sort)) {
            data.setSort(sort);
        }

        //提交更换
        updateById(data);

        category = getById(category.getId());
        updateParentChain(childrenParentChain(category), childrenChainPrefix);
    }

    private void updateParentChain(String newChildrenPC, String oldChildrenPC) {
        List<CourseCategory> children = list(query().getWrapper().like("parent_chain", oldChildrenPC + "%"));
        if (children.size() == 0) {
            return;
        }

        ArrayList<CourseCategory> updateRows = new ArrayList<>();
        for (CourseCategory tmpCourseCategory : children) {
            CourseCategory tmpUpdateCourseCategory = new CourseCategory();
            tmpUpdateCourseCategory.setId(tmpCourseCategory.getId());

            // parentChain计算
            String pc = newChildrenPC;
            if (!tmpCourseCategory.getParentChain().equals(oldChildrenPC)) {
                pc = tmpCourseCategory.getParentChain().replaceFirst(oldChildrenPC + ",", newChildrenPC.length() == 0 ? newChildrenPC : newChildrenPC + ',');
            }
            tmpUpdateCourseCategory.setParentChain(pc);

            // parentId计算
            int parentId = 0;
            if (pc != null && pc.length() > 0) {
                String[] parentIds = pc.split(",");
                parentId = Integer.parseInt(parentIds[parentIds.length - 1]);
            }
            tmpUpdateCourseCategory.setParentId(parentId);

            updateRows.add(tmpUpdateCourseCategory);
        }
        updateBatchById(updateRows);
    }


    @Override
    public void create(String name, Integer parentId, Integer sort) throws NotFoundException {
        String parentChain = "";
        if (parentId != 0) {
            parentChain = compParentChain(parentId);
        }

        CourseCategory category = new CourseCategory();
        category.setName(name);
        category.setParentId(parentId);
        category.setParentChain(parentChain);
        category.setSort(sort);
        category.setCreatedAt(new Date());
        category.setUpdatedAt(new Date());

        save(category);
    }

    @Override
    public String childrenParentChain(CourseCategory category) {
        String prefix = category.getId() + "";
        if (category.getParentChain() != null && category.getParentChain().length() > 0) {
            prefix = category.getParentChain() + "," + prefix;
        }
        return prefix;
    }

    @Override
    public String compParentChain(Integer parentId) throws NotFoundException {
        String parentChain = "";
        if (parentId != 0) {
            CourseCategory parentCourseCategory = getById(parentId);
            if (parentCourseCategory == null) {
                throw new NotFoundException("父级分类不存在");
            }
            String pc = parentCourseCategory.getParentChain();
            parentChain = pc == null || pc.length() == 0 ? parentId + "" : pc + "," + parentId;
        }
        return parentChain;
    }
}





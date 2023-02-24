package xyz.playedu.api.bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.api.domain.CourseCategory;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.service.CourseCategoryService;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/24 13:57
 */
@Component
public class CourseCategoryBus {

    @Autowired
    private CourseCategoryService categoryService;

    public String compParentChain(Integer parentId) throws NotFoundException {
        String parentChain = "";
        if (parentId != 0) {
            CourseCategory parentCourseCategory = categoryService.getById(parentId);
            if (parentCourseCategory == null) {
                throw new NotFoundException("父级分类不存在");
            }
            String pc = parentCourseCategory.getParentChain();
            parentChain = pc == null || pc.length() == 0 ? parentId + "" : pc + "," + parentId;
        }
        return parentChain;
    }

    public static String childrenParentChain(CourseCategory CourseCategory) {
        String prefix = CourseCategory.getId() + "";
        if (CourseCategory.getParentChain() != null && CourseCategory.getParentChain().length() > 0) {
            prefix = CourseCategory.getParentChain() + "," + prefix;
        }
        return prefix;
    }

}

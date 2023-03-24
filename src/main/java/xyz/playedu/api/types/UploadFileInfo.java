package xyz.playedu.api.types;

import lombok.Data;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/24 14:35
 */
@Data
public class UploadFileInfo {
    private String originalName;
    private String extension;
    private long size;
    private String saveName;
    private String resourceType;
    private String savePath;
    private String url;
}

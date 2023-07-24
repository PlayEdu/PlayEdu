/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BackendConstant {
    public static final String SUPER_ADMIN_ROLE = "super-role";

    public static final List<String> UN_AUTH_URI_WHITELIST =
            new ArrayList<>() {
                {
                    add("/backend/v1/system/image-captcha");
                    add("/backend/v1/auth/login");
                }
            };

    public static final String RESOURCE_TYPE_VIDEO = "VIDEO";
    public static final String RESOURCE_TYPE_IMAGE = "IMAGE";
    public static final String RESOURCE_TYPE_PDF = "PDF";
    public static final String RESOURCE_TYPE_WORD = "WORD";
    public static final String RESOURCE_TYPE_PPT = "PPT";
    public static final String RESOURCE_TYPE_EXCEL = "EXCEL";
    public static final String RESOURCE_TYPE_ZIP= "ZIP";
    public static final String RESOURCE_TYPE_RAR = "RAR";
    public static final String RESOURCE_TYPE_TXT = "TXT";

    public static final String RESOURCE_TYPE_ANNEX =
            RESOURCE_TYPE_PDF +","+ RESOURCE_TYPE_TXT +","+
            RESOURCE_TYPE_ZIP +","+ RESOURCE_TYPE_RAR +","+
            RESOURCE_TYPE_WORD +","+RESOURCE_TYPE_PPT +","+RESOURCE_TYPE_EXCEL;

    public static final HashMap<String, String> RESOURCE_EXT_2_CONTENT_TYPE =
            new HashMap<>() {
                {
                    put("png", "image/png");
                    put("jpg", "image/jpg");
                    put("jpeg", "image/jpeg");
                    put("gif", "image/gif");
                    put("pdf", "application/pdf");
                    put("mp4", "video/mp4");
                    put("doc", "application/msword");
                    put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                    put("ppt", "application/vnd.ms-powerpoint");
                    put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
                    put("xls", "application/vnd.ms-excel");
                    put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    put("txt", "text/plain");
                    put("zip", "application/zip");
                    put("rar", "application/x-rar");
                }
            };
    public static final HashMap<String, String> RESOURCE_EXT_2_TYPE =
            new HashMap<>() {
                {
                    put("png", RESOURCE_TYPE_IMAGE);
                    put("jpg", RESOURCE_TYPE_IMAGE);
                    put("jpeg", RESOURCE_TYPE_IMAGE);
                    put("gif", RESOURCE_TYPE_IMAGE);
                    put("pdf", RESOURCE_TYPE_PDF);
                    put("mp4", RESOURCE_TYPE_VIDEO);
                    put("doc", RESOURCE_TYPE_WORD);
                    put("docx", RESOURCE_TYPE_WORD);
                    put("ppt", RESOURCE_TYPE_PPT);
                    put("pptx", RESOURCE_TYPE_PPT);
                    put("xls", RESOURCE_TYPE_EXCEL);
                    put("xlsx", RESOURCE_TYPE_EXCEL);
                    put("txt", RESOURCE_TYPE_TXT);
                    put("zip", RESOURCE_TYPE_ZIP);
                    put("rar", RESOURCE_TYPE_RAR);
                }
            };
    public static final HashMap<String, String> RESOURCE_TYPE_2_DIR =
            new HashMap<>() {
                {
                    put(RESOURCE_TYPE_VIDEO, UPLOAD_VIDEO_DIR);
                    put(RESOURCE_TYPE_IMAGE, UPLOAD_IMAGE_DIR);
                    put(RESOURCE_TYPE_PDF, UPLOAD_PDF_DIR);
                    put(RESOURCE_TYPE_WORD, UPLOAD_WORD_DIR);
                    put(RESOURCE_TYPE_PPT, UPLOAD_PPT_DIR);
                    put(RESOURCE_TYPE_EXCEL, UPLOAD_EXCEL_DIR);
                    put(RESOURCE_TYPE_TXT, UPLOAD_TXT_DIR);
                    put(RESOURCE_TYPE_ZIP, UPLOAD_ZIP_DIR);
                    put(RESOURCE_TYPE_RAR, UPLOAD_RAR_DIR);
                }
            };

    public static final String STORAGE_DRIVER_MINIO = "minio";

    public static final String[] COURSE_HOUR_TYPE_WHITELIST = {"VIDEO"};
    public static final String[] COURSE_HOUR_TYPE_WHITELIST_TEXT = {"视频"};

    public static final String UPLOAD_IMAGE_DIR = "images/";
    public static final String UPLOAD_VIDEO_DIR = "videos/";
    public static final String UPLOAD_PDF_DIR = "pdf/";
    public static final String UPLOAD_WORD_DIR = "word/";
    public static final String UPLOAD_PPT_DIR = "word/";
    public static final String UPLOAD_EXCEL_DIR = "excel/";
    public static final String UPLOAD_TXT_DIR = "txt/";
    public static final String UPLOAD_ZIP_DIR = "zip/";
    public static final String UPLOAD_RAR_DIR = "rar/";

    public static final String PRIVACY_FIELD_TYPE_EMAIL = "email";
    public static final String PRIVACY_FIELD_TYPE_PHONE = "phone";
    public static final String PRIVACY_FIELD_TYPE_NAME = "name";
    public static final String PRIVACY_FIELD_TYPE_ID_CARD = "IDCard";

    public static final String APP_CONFIG_FIELD_TYPE_IMAGE = "image";
    public static final String APP_CONFIG_FIELD_TYPE_INPUT = "input";
    public static final String APP_CONFIG_FIELD_TYPE_TEXT = "text";
    public static final String APP_CONFIG_FIELD_TYPE_SELECT = "select";
    public static final String APP_CONFIG_FIELD_TYPE_SWITCH = "switch";
}

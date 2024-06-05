declare global {
  interface KeyNumberObject {
    [key: number]: number;
  }

  interface FileItem {
    id: string; //上传文件的唯一id
    file: File; //上传的文件资源
    // 上传实际执行者
    upload: {
      handler: UploadChunk;
      progress: number;
      status: number;
      remark: string;
    };
    // 视频文件信息
    video?: {
      duration: number; //时长
      poster: string; //视频帧
    };
  }

  interface UserModel {
    avatar: string;
    create_city?: string;
    create_ip?: string;
    created_at?: string;
    credit1?: number;
    email: string;
    id: number;
    id_card?: string;
    is_active?: number;
    is_lock?: number;
    is_set_password?: number;
    is_verify?: number;
    login_at?: string;
    name: string;
    updated_at?: string;
    verify_at?: string;
  }

  interface AdminUserDetailModel {
    created_at: string;
    email: string;
    id: number;
    is_ban_login: number;
    login_at: string;
    login_ip: string;
    login_times: number;
    name: string;
    updated_at: string;
  }

  interface CourseModel {
    charge: number;
    class_hour: number;
    created_at: string;
    id: number;
    is_required: number;
    is_show: number;
    short_desc: string;
    thumb: string;
    title: string;
  }

  interface CategoriesBoxModel {
    [key: number]: CategoriesItemModel[];
  }

  interface CategoriesItemModel {
    id: number;
    name: string;
    parent_chain: string;
    parent_id: number;
    sort: number;
  }

  interface CategoriesModel {
    [key: number]: string;
  }

  interface DepartmentsModel {
    [key: number]: string;
  }

  interface DepIdsModel {
    [key: number]: number[];
  }

  interface CategoryIdsModel {
    [key: number]: number[];
  }

  interface DepartmentsBoxModel {
    [key: number]: DepartmentsItemModel[];
  }

  interface DepartmentsItemModel {
    created_at: string;
    id: number;
    name: string;
    parent_chain: string;
    parent_id: number;
    sort: number;
    updated_at: string;
  }

  interface RolesModel {
    [key: number]: RoleModel[];
  }

  interface RoleModel {
    created_at: string;
    id: number;
    name: string;
    slug: string;
    updated_at: string;
  }

  interface RoleIdsModel {
    [key: number]: number[];
  }

  interface CourseChaptersModel {
    id?: number;
    hours: CourseHourModel[];
    name: string;
  }

  interface CourseHourModel {
    id?: number;
    duration: number;
    name: string;
    rid: number;
    type: string;
  }

  interface AttachmentDataModel {
    id?: number;
    name: string;
    rid: number;
    type: string;
  }
}

export {};

declare global {
  interface CourseModel {
    charge: number;
    class_hour: number;
    created_at: string;
    id: number;
    is_required: number;
    is_show: number;
    short_desc: string;
    thumb: number;
    title: string;
  }

  interface HourModel {
    chapter_id: number;
    course_id: number;
    duration: number;
    id: number;
    rid: number;
    sort: number;
    title: string;
    type: string;
  }

  interface HourRecordModel {
    course_id: number;
    created_at: string;
    finished_at?: string;
    finished_duration: number;
    hour_id: number;
    id: number;
    is_finished: number;
    real_duration: number;
    total_duration: number;
    updated_at: string;
    user_id: number;
  }

  interface CourseRecordModel {
    course_id: number;
    created_at: string;
    finished_at?: string;
    finished_count: number;
    hour_count: number;
    id: number;
    is_finished: number;
    progress: number;
    updated_at: string;
    user_id: number;
  }

  interface ResourceUrlModel {
    [key: number]: string;
  }
}

export {};

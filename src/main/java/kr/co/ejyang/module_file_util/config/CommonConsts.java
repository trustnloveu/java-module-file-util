package kr.co.ejyang.module_file_util.config;

public interface CommonConsts {

    // 파일 관련 설정값
    public static final long FILE_MAX_SIZE          = 50 * 1024 * 1024 + 100;   // 파일 최대 사이즈 ( 50MB + 100 byte )
    public static final long FILE_MIN_SIZE          = 1024 + 100;               // 파일 최소 사이즈 ( 1KB + 100 byte )
    public static final long FILE_MAX_LENGTH        = 80;                       // 파일명 최대 길이

    // 디렉토리 타입
    public static final String PUBLIC       = "public";
    public static final String PRIVATE      = "private";
    public static final String STATIC       = "static";
    public static final String[] DIR_TYPES  = { PUBLIC, PRIVATE, STATIC };

    // 허용 확장자 타입
    public static final String[] ALLOW_EXT_TYPES    = { "jpg", "jpeg", "png", "pdf", "xlsx", "xls", "hwp", "hwpx" };

}

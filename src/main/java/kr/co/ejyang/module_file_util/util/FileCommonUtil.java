package kr.co.ejyang.module_file_util.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static kr.co.ejyang.module_file_util.config.CommonConsts.*;

@Component("FileCommonUtil")
public class FileCommonUtil {

    // #########################################################################################
    //                                      [ PUBLIC ]
    // #########################################################################################

    /*******************************************************************************************
     * 파일 검증 ( Null, 파일명, 용량, 확장자 )
     *******************************************************************************************/
    public boolean isValidFile(MultipartFile file) {
        return !Objects.isNull(file) && isValidLength(file) && isValidSize(file) && isValidLength(file) && isValidExtType(file);
    }


    /*******************************************************************************************
     * 디렉토리 타입 검증 ( public, private, static )
     *******************************************************************************************/
    public boolean isValidDirType(String dirType) {
        return Arrays.asList(DIR_TYPES).contains(dirType);
    }


    /*******************************************************************************************
     * 파일명 변환 - 타임스탬프 + 랜덤 정수 ( convertFileName )
     *******************************************************************************************/
    public String convertFileName(MultipartFile file) {

        String originalName = file.getOriginalFilename();                           // 실제 파일명
        String originalNameExtension = FilenameUtils.getExtension(originalName);    // 실제 파일명 확장자

        // 파일명 변환 (타임스템프)
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmmssSSS");
        String formatDateTime = now.format(formatter);
        int ranInt = (int) (Math.random() * 10000); // 랜덤 정수

        // 최종 파일명
        return ranInt + formatDateTime + "." + Objects.requireNonNull(originalNameExtension).toLowerCase();
    }


    /*******************************************************************************************
     * 임시 파일명 URL 발급 ( UUID - 타임스템프 )
     *  - Redis Key 사용
     *******************************************************************************************/
    public String generateFileTempUrl() {
        UUID uuid = UUID.randomUUID();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmmssSSS");
        String formatDateTime = now.format(formatter);

        return uuid + "-" + formatDateTime;
    }

    /*******************************************************************************************
     * Storage Key 검증
     *  - 파일 경로 Key 일치 검증
     *  - 경로 형식 = /{StorageKey}/{FileType}/{DirPath}/{file}
     *  - Storage Key = 2번째 인덱스
     *******************************************************************************************/
    public boolean isValidKeyInPath(String path, String key) {
        String[] pathArray = path.split("/");
        List<String> pathList = Arrays.stream(pathArray).collect(Collectors.toList());

        if (pathList.size() < 2 ) throw new ArrayIndexOutOfBoundsException(String.format("잘못된 경로입니다. ( %s )", path));

        return key.equals(pathList.get(2));
    }

    /*******************************************************************************************
     * 파일 타입 검증
     *  - 파일 경로 File Type 일치 검증 ( public, private )
     *  - 경로 형식 = /{StorageKey}/{FileType}/{DirPath}/{file}
     *  - File Type = 3번째 인덱스
     *******************************************************************************************/
    public boolean isValidAccessByFileType(String path, String type) {
        String[] pathArray = path.split("/");
        List<String> pathList = Arrays.stream(pathArray).collect(Collectors.toList());

        if (pathList.size() < 3 ) throw new ArrayIndexOutOfBoundsException(String.format("잘못된 경로입니다. ( %s )", path));

        return type.equals(pathList.get(2));
    }


    // #########################################################################################
    //                                      [ PRIVATE ]
    // #########################################################################################


    /*******************************************************************************************
     * 파일명 길이 확인 ( isValidLength )
     *******************************************************************************************/
    private boolean isValidLength(MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename()).length() <= FILE_MAX_LENGTH;
    }


    /*******************************************************************************************
     * 파일 용량 확인 ( isValidLength ) - 최대/최소 사이즈 체크
     *******************************************************************************************/
    private boolean isValidSize(MultipartFile file) {
        return file.getSize() <= FILE_MAX_SIZE && file.getSize() >= FILE_MIN_SIZE;
    }


    /*******************************************************************************************
     * 파일 확장자 확인 ( isValidExtType )
     *******************************************************************************************/
    private boolean isValidExtType(MultipartFile file) {

        String originalName = file.getOriginalFilename();                           // 실제 파일명
        String originalNameExtension = FilenameUtils.getExtension(originalName);    // 실제 파일명 확장자

        // 허용 확장자 검증
        return Arrays.stream(ALLOW_EXT_TYPES).anyMatch(s -> s.equalsIgnoreCase(originalNameExtension));
    }

}

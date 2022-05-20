package kopo.poly.util;

import java.io.File;

public class FileUtil {

    /**
     * 현재 날짜를 기준으로 년 /월/일 폴더 생성하기
     *
     * @param uploadDir 저장되는 ROOT폴더
     * @return 파일이 저장되기 위해 생성된 전체 폴더 경로
     */
    public static String mkdirForDate(String uploadDir) {

        String path = uploadDir + DateUtil.getDateTime("/yyyy/MM/dd"); // 폴더 경로

        File Folder = new File(path);

        if (!Folder.exists()) {
            Folder.mkdirs(); // 폴더 생성합니다.

        }

        return path;
    }
}

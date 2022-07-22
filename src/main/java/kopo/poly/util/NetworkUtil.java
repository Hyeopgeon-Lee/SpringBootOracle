package kopo.poly.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Slf4j
public class NetworkUtil {

    /**
     * GET 방식으로 OpenAPI 호출하기(전송할 헤더값이 존재하지 않는 경우 사용)
     * 네이버 API 전송 소스 참고하여 구현
     *
     * @param apiUrl 호출할 OpenAPI URL 주소
     */
    public static String get(String apiUrl) {
        return get(apiUrl, null);

    }

    /**
     * GET 방식으로 OpenAPI 호출하기
     * 네이버 API 전송 소스 참고하여 구현
     *
     * @param apiUrl         호출할 OpenAPI URL 주소
     * @param requestHeaders 전송하고 싶은 해더 정보
     */
    public static String get(String apiUrl, @Nullable Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);

        try {
            con.setRequestMethod("GET");

            // 전송할 헤더 값이 존재하면, 해더 값 추가하기
            if (requestHeaders != null) {
                for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                    con.setRequestProperty(header.getKey(), header.getValue());
                }
            }

            // API 호출 후, 결과 받기
            int responseCode = con.getResponseCode();

            // API 호출이 성공하면..
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream()); // 성공 결과 값을 문자열로 변환하기

            } else { // 에러 발생
                return readBody(con.getErrorStream()); // 실패 결과 값을 문자열로 변환하기

            }

        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);

        } finally {
            con.disconnect();
        }
    }

    /**
     * POST 방식으로 OpenAPI 호출하기
     * 네이버 API 전송 소스 참고하여 구현
     *
     * @param apiUrl         호출할 OpenAPI URL 주소
     * @param postParams     전송할 파라미터
     * @param requestHeaders 전송하고 싶은 해더 정보
     */
    public static String post(String apiUrl, @Nullable Map<String, String> requestHeaders, String postParams) {
        HttpURLConnection con = connect(apiUrl);

        try {
            con.setRequestMethod("POST");

            // 전송할 헤더 값이 존재하면, 해더 값 추가하기
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            // POST 방식으로 전송할때, 전송할 파라미터 정보 넣기(GET 방식은 필요없음)
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postParams.getBytes());
                wr.flush();
            }

            // API 호출 후, 결과 받기
            int responseCode = con.getResponseCode();

            // API 호출이 성공하면..
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream()); // 성공 결과 값을 문자열로 변환하기

            } else { // 에러 발생
                return readBody(con.getErrorStream()); // 실패 결과 값을 문자열로 변환하기

            }

        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    /**
     * OpenAPI URL에 접속하기
     * 이 함수는 NetworkUtil에서만 사용하기에 접근 제한자를 private으로 선언함
     * 외부 자바 파일에서 호출 불가
     *
     * @param apiUrl 호출할 OpenAPI URL 주소
     */
    private static HttpURLConnection connect(String apiUrl) {

        try {
            URL url = new URL(apiUrl);

            return (HttpURLConnection) url.openConnection();

        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);

        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);

        }
    }

    /**
     * OpenAPI 호출 후, 받은 결과를 문자열로 변환하기
     * 이 함수는 NetworkUtil에서만 사용하기에 접근 제한자를 private으로 선언함
     * 외부 자바 파일에서 호출 불가
     *
     * @param body 읽은 결과값
     */
    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }


}

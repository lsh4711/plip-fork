package com.server.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.server.domain.member.dto.MemberDto;
import com.server.domain.place.dto.PlaceDto;
import com.server.domain.record.dto.RecordDto;
import com.server.domain.schedule.dto.ScheduleDto;
import com.server.global.auth.dto.LoginDto;
import com.server.global.auth.jwt.JwtTokenizer;

public class StubData {
    public static class MockSecurity {
        public static String getValidAccessToken(String secretKey) {
            JwtTokenizer jwtTokenizer = new JwtTokenizer();
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", "test@test.com");
            claims.put("memberId", 1);
            claims.put("roles", List.of("USER"));

            String subject = "test access token";
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 1);
            Date expiration = calendar.getTime();

            String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(secretKey);

            String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

            return accessToken;
        }
    }

    public static class MockMember {
        private static Map<String, Object> stubRequestBody;

        static {
            stubRequestBody = new HashMap<>();
            MemberDto.Post memberPost = MemberDto.Post.builder()
                    .email("test123@naver.com")
                    .password("q12345678!")
                    .nickname("테스트계정")
                    .build();

            LoginDto loginDto = LoginDto.builder()
                    .username("admin")
                    .password("admin")
                    .build();

            MemberDto.Patch memberPatch = MemberDto.Patch.builder()
                    .nickname("테스트수정테스트")
                    .password("12345678!a")
                    .build();

            stubRequestBody.put("memberPost", memberPost);
            stubRequestBody.put("loginDto", loginDto);
            stubRequestBody.put("memberPatch", memberPatch);
        }

        public static Object getRequestBody(String valueName) {
            return stubRequestBody.get(valueName);
        }

        public static MemberDto.Response getSingleResponseBody() {
            return MemberDto.Response.builder()
                    .nickname("테스트수정계정")
                    .build();
        }
    }

    public static class MockRecord {
        private static Map<String, Object> stubRequestBody;
        private static Map<String, List<RecordDto.Response>> stubDatas;

        static {
            stubRequestBody = new HashMap<>();
            stubDatas = new HashMap<>();

            RecordDto.Post post = RecordDto.Post.builder()
                    .title("서울 롯데월드")
                    .content("롯데월드에서는..")
                    .build();
            stubRequestBody.put("recordPost", post);

            RecordDto.Patch patch = RecordDto.Patch.builder()
                    .title("서울 남산")
                    .content("남산에서는..")
                    .build();
            stubRequestBody.put("recordPatch", patch);

            RecordDto.Response response = RecordDto.Response.builder()
                    .recordId(1L)
                    .title("서울 롯데월드")
                    .content("롯데월드에서는..")
                    .memberId(1L)
                    .createdAt(LocalDateTime.now().withNano(0))
                    .modifiedAt(LocalDateTime.now().withNano(0))
                    .build();
            stubRequestBody.put("recordResponse", response);

            RecordDto.Response patchResponse = RecordDto.Response.builder()
                    .recordId(1L)
                    .title("서울 남산")
                    .content("남산에서는..")
                    .memberId(1L)
                    .createdAt(LocalDateTime.now().withNano(0))
                    .modifiedAt(LocalDateTime.now().withNano(0))
                    .build();
            stubRequestBody.put("recordPatchResponse", patchResponse);

            List<RecordDto.Response> responses = List.of(
                RecordDto.Response.builder()
                        .recordId(1L)
                        .title("서울 롯데월드")
                        .content("롯데월드에서는..")
                        .memberId(1L)
                        .createdAt(LocalDateTime.now().withNano(0))
                        .modifiedAt(LocalDateTime.now().withNano(0))
                        .build(),

                RecordDto.Response.builder()
                        .recordId(2L)
                        .title("서울 남산")
                        .content("남산에서는..")
                        .memberId(1L)
                        .createdAt(LocalDateTime.now().withNano(0))
                        .modifiedAt(LocalDateTime.now().withNano(0))
                        .build());

            stubDatas.put("recordResponses", responses);
        }

        public static Object getRequestBody(String valueName) {
            return stubRequestBody.get(valueName);
        }

        public static List<RecordDto.Response> getRequestDatas(String valueName) {
            return stubDatas.get(valueName);
        }

    }

    public static class MockSchedule {
        public static ScheduleDto.Post postDto = new ScheduleDto.Post();

        static {
            postDto.setTitle("제목");
            postDto.setContent("일정에 대한 메모");
            postDto.setMemberCount(1);
            postDto.setRegion("서울");
            postDto.setStartDate(LocalDate.now());
            postDto.setEndDate(LocalDate.now().plusDays(5));
        }
    }

    public static class MockPlace {
        public static List<PlaceDto.Post> postDtos = new ArrayList<>();

        static {
            String[] placeNames = {"감귤 농장", "초콜릿 박물관", "제주도 바닷가"};

            for (int i = 1; i <= 3; i++) {
                PlaceDto.Post postDto = new PlaceDto.Post();
                postDto.setApiId(i * 10 + i);
                postDto.setName(placeNames[i - 1]);
                postDto.setAddress("제주도 무슨동 무슨길" + i);
                postDto.setLatitude(String.format("%d.%d", i * 205 + i * 17 + i * 8, i * 27));
                postDto.setLongitude(String.format("%d.%d", i * 121 + i * 23 + i * 3, i * 31));
                // SchedulePlace
                postDto.setDays(i);
                postDto.setOrders(i);
                postDtos.add(postDto);
            }

        }
    }
}

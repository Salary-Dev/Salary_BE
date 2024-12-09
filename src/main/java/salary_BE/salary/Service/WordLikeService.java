package salary_BE.salary.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import salary_BE.salary.DTO.ArticleDto;
import salary_BE.salary.DTO.WordLikeDto;
import salary_BE.salary.DTO.WordRemindingDto;
import salary_BE.salary.DTO.WordmainDto;
import salary_BE.salary.Domain.*;
import salary_BE.salary.Repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.tomcat.util.http.FastHttpDateFormat.getCurrentDate;


@Service
@RequiredArgsConstructor
@Transactional
public class WordLikeService {

    private final WordRepository wordRepository;
    private final WordLikeRepository wordLikeRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AttendanceService attendanceService;
    private final TodayStudyRepository todayStudyRepository;
    private final AttendanceRepository attendanceRepository;

    // 단어장 저장 (변경 사항 저장)
    // 이미 저장되어있는 단어라면 저장하지 않음
    public WordLike addWordToWordBook(Long wordId, User user) {

        // Word 엔티티 조회
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new IllegalArgumentException("해당 단어를 찾을 수 없습니다."));

        // 이미 저장된 단어인지 확인
//        Optional<WordLike> existingWordLike = wordLikeRepository.findByWordId(wordId);
//        if (existingWordLike.isPresent() && existingWordLike.get().getUser().equals(currentUser)) {
//            throw new IllegalArgumentException("이미 단어장이 북마크되어 있습니다."); // 중복 저장 방지
//        }

        // WordLike 엔티티 생성
        WordLike wordLike = new WordLike();
        wordLike.setUser(user);
        wordLike.setWord(word);
        wordLike.setWordBookmark(true);
        wordLike.setLikeDate(LocalDateTime.now());

        return wordLikeRepository.save(wordLike);
    }


    // 단어장 조회
    public List<Map<String, Object>> getUserLikedWords(String loginId) {

        User user = userRepository.findByLoginId(loginId);
        // 현재 사용자가 저장한 단어 중 word_bookmark가 1인 것 조회
        return wordLikeRepository.findByUserAndWordBookmark(user, true).stream()
                .filter(wordLike -> wordLike.getLikeDate() != null) // like_date가 null인 데이터 제외 (db 재설정 시 삭제해도 되는 조건)
                .map(wordLike -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("word_id", wordLike.getWord().getId());
                    result.put("word", wordLike.getWord().getWord());
                    result.put("like_date", wordLike.getLikeDate());
                    return result;
                })
                .collect(Collectors.toList());
    }

    // 단어장 삭제
    public void deleteWordLike(Long wordId, Long userId) {
        // Word ID로 WordLike 엔티티 조회
        WordLike wordLike = wordLikeRepository.findByWordIdAndUserId(wordId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 단어는 북마크되지 않았습니다."));

        // 현재 사용자 확인 -> findByWordIdAndUserId 하나로 합침
//        if (!wordLike.getUser().equals(userId)) {
//            throw new IllegalArgumentException("현재 사용자가 이 단어를 북마크하지 않았습니다.");
//        }

        // WordLike 삭제
        wordLikeRepository.delete(wordLike);
    }

    // 단어장 리마인더
    public List<WordRemindingDto> getRandomWords(User user) {

        if (user == null) {
            throw new IllegalStateException("사용자가 유혀하지 않습니다.");
        }

        List<WordLike> wordLikes = wordLikeRepository.findRandomWordLikesLimit12();

        // wordLikes가 null일 경우 빈 리스트로 처리
        if (wordLikes == null) {
            wordLikes = new ArrayList<>();
        }

        return wordLikes.stream()
                .filter(wordLike -> Objects.equals(wordLike.getUser(), user))
                .map(wordLike -> {
                    if (wordLike.getWord() != null && wordLike.getWord().getWord() != null) {
                        return new WordRemindingDto(
                                wordLike.getWord().getWord(),
                                wordLike.getWord().getMean()
                        );
                    } else {
                        return new WordRemindingDto("N/A", "N/A");
                    }
                })
                .collect(Collectors.toList());
    }

    // 단어 학습 여부
    @Transactional
    public String completeWord(Long wordId, User user) {
        LocalDate todayDate = getCurrentDate();

        // [출석률] 테이블에서 유저의 [오늘 학습] 테이블 정보 가져오기 없으면 초기화
        Attendance attendance = attendanceRepository.findByUserIdAndAttendanceDate(user.getId(), todayDate);
        if (attendance == null) {
            attendance = new Attendance();
            attendance.setUser(user);
            attendance.setAttendanceDate(todayDate); // 출석 날짜 초기화
            attendance.setAttendanceState(0); // 학습 상태 초기화
            attendance.setLastWordId(wordId); // 받은 word_id를 초기화 시 저장
            attendance = attendanceRepository.save(attendance);
        } else {
            // 3. 기존 Attendance에 lastWordId 업데이트
            attendance.setLastWordId(wordId);
            attendanceRepository.save(attendance);
        }

        // [오늘 학습] 테이블에서 학습 정보 가져오기 없으면 초기화
        TodayStudy todayStudy = todayStudyRepository.findByAttendanceId(attendance.getId());
        if (todayStudy == null) {
            todayStudy = new TodayStudy();
            todayStudy.setAttendance(attendance); // 출석률 테이블과 연결
            todayStudy.setWord(false); // 초기 학습 상태 설정
            todayStudy = todayStudyRepository.save(todayStudy);
        }

        // 1. [오늘 학습] 테이블에 단어 학습 여부 반영
        todayStudy.setWord(true); // 학습 완료로 설정
        todayStudyRepository.save(todayStudy);

        // 2. 출석률 테이블 [attendance_state] 속성 + 3
        int currentState = attendance.getAttendanceState();
        attendance.setAttendanceState(currentState + 3); // 학습 완료 시 출석률 증가
        attendanceRepository.save(attendance);

        return "success";
    }

    // 오늘 날짜 가져오기
    private LocalDate getCurrentDate() {
        return LocalDate.now(ZoneId.of("Asia/Seoul"));
    }
}
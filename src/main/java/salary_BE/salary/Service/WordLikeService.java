package salary_BE.salary.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import salary_BE.salary.DTO.WordLikeDto;
import salary_BE.salary.DTO.WordRemindingDto;
import salary_BE.salary.Domain.*;
import salary_BE.salary.Repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.tomcat.util.http.FastHttpDateFormat.getCurrentDate;


@Service
@RequiredArgsConstructor

public class WordLikeService {

    private final WordRepository wordRepository;
    private final WordLikeRepository wordLikeRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AttendanceService attendanceService;
    private final TodayStudyRepository todayStudyRepository;
    private final AttendanceRepository attendanceRepository;

    public WordLike addWordToWordBook(String wordName) {
        // 단어 이름으로 Word 엔티티 조회
        Word word = wordRepository.findByWord(wordName)
                .orElseThrow(() -> new IllegalArgumentException("해당 단어를 찾을 수 없습니다: " + wordName));

        // WordLike 엔티티 생성 및 설정
        WordLike wordLike = new WordLike();
        wordLike.setWord(word);
        wordLike.setWordBookmark(true);
        wordLike.setLikeDate(LocalDateTime.now());

        // WordLike 엔티티 저장
        return wordLikeRepository.save(wordLike);
    }

    public List<WordLikeDto> getUserLikedWords() {

        // 해당 유저가 좋아요한 단어 조회
        return wordLikeRepository.findAll().stream()
                .map(wordLike -> new WordLikeDto(
                        wordLike.getWord().getId(),
                        wordLike.getWord().getWord(),
                        wordLike.getLikeDate()
                ))
                .collect(Collectors.toList());
    }

    public void deleteWordLike(Long word_id) {
        // wordId로 WordLike 엔티티 조회
        WordLike wordLike = wordLikeRepository.findByWordId(word_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 단어는 북마크되지 않았습니다."));

        // WordLike 엔티티 삭제
        wordLikeRepository.delete(wordLike);
    }

    public List<WordRemindingDto> getRandomWords() {
        // 무작위로 12개의 WordLike 엔티티를 가져와 WordDto로 변환
        return wordLikeRepository.findRandomWordLikesLimit12().stream()
                .map(wordLike -> new WordRemindingDto(wordLike.getWord().getWord(), wordLike.getWord().getMean()))
                .collect(Collectors.toList());
    }

    // 단어 학습 여부
    // 단어 학습 여부
    @Transactional
    public String completeWord(Long wordId) {
        User currentUser = userService.getCurrentUser(); // 현재 사용자 식별
        LocalDate todayDate = getCurrentDate();

        // [출석률] 테이블에서 유저의 [오늘 학습] 테이블 정보 가져오기 없으면 초기화
        Attendance attendance = attendanceRepository.findByUserIdAndAttendanceDate(currentUser.getId(), todayDate);
        if (attendance == null) {
            attendance = new Attendance();
            attendance.setUser(currentUser);
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
        return java.time.LocalDate.now();
    }
}
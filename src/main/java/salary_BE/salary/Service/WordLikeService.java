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
    @Transactional
    public String completeWord(Long word_id, boolean word) {
        User currentUser = userService.getCurrentUser(); // 사용자 식별
        LocalDate todayDate = getCurrentDate();

        // [출석률] 테이블에서 유저의 [오늘 학습] 테이블 정보 가져오기 없으면 정보 초기화
        Attendance attendance = attendanceRepository.findByUserIdAndAttendanceDate(currentUser.getId(), todayDate);
        if (attendance == null) {
            attendance = new Attendance();
            attendance.setAttendanceDate(todayDate); // 출석 날짜 초기화
            attendance.setAttendanceState(0); // 학습 상태 초기화
            attendance = attendanceRepository.save(attendance);
        }

        // [오늘 학습] 테이블에서 학습 정보 가져오기 없으면 초기화
        TodayStudy todayStudy = todayStudyRepository.findByAttendanceId(attendance.getId());
        if (todayStudy == null) {
            todayStudy = new TodayStudy();
            todayStudy.setAttendance(attendance); // 출석률 테이블과 연결
            todayStudy.setWord(false); // 초기 학습 상태 설정
            todayStudy = todayStudyRepository.save(todayStudy);
        }

        // [단어 북마크] 테이블에서 정보 가져오기 없으면 초기화
        WordLike wordLike = wordLikeRepository.findByUserAndWordId(currentUser, word_id)
                .orElse(null);
        if (wordLike == null) {
            wordLike = new WordLike();
            wordLike.setUser(currentUser);
            // wordLike.setWord(); // 초기 word_id 상태 초기화 (이 부분 수정 필요)
            wordLike.setWordBookmark(false); // 초기 북마크 상태 초기화
            wordLikeRepository.save(wordLike);
        }

        // 1. [오늘 학습] 테이블에 단어 학습 여부 반영
        todayStudy.setWord(word);
        todayStudyRepository.save(todayStudy);

        // 2. 출석률 테이블 [attendance_state] 속성 + 3
        if (word) { // 학습이 완료된 경우에만 출석률 증가 (word = true)
            int currentState = attendance.getAttendanceState();
            attendance.setAttendanceState(currentState + 3);
            attendanceRepository.save(attendance);
        }

        // 3. 단어 북마크 테이블에 word_id + 1 추가
        WordLike nextWordLike = wordLikeRepository.findByUserAndWordId(currentUser, word_id + 1).orElse(null); // Optional 처리
        return "success";
    }

    // 오늘 날짜 가져오기
    private LocalDate getCurrentDate() {
                return java.time.LocalDate.now();
    }

}




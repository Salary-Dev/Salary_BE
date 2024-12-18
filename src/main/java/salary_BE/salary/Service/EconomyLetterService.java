package salary_BE.salary.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import salary_BE.salary.Domain.EconomyLetter;
import salary_BE.salary.Repository.EconomyLetterRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EconomyLetterService {

    private final EconomyLetterRepository economyLetterRepository;

    // 구독 중인 경제레터 조회
    public List<Map<String, String>> subscribeLetter(Long userId) {

        // 나중에 로직 수정 필요
        List<EconomyLetter> economyLetters = (List<EconomyLetter>) economyLetterRepository.findByEditor_Id(1L);

        List<Map<String, String>> response = new ArrayList<>();

        for (EconomyLetter letter : economyLetters) {
            Map<String, String> letterMap = new HashMap<>();
            letterMap.put("title", letter.getTitle());
            letterMap.put("editor", letter.getEditor().getName());
            letterMap.put("elapsedTime", "1시간 전");
            letterMap.put("uploadDate", String.valueOf(letter.getCreatedAt()));
            letterMap.put("text", letter.getText());
            response.add(letterMap);
        }

        return response;
    }

    // 도착한 경제레터 조회
    public List<Map<String, String>> economyLetter() {

        // 나중에 로직 수정 필요
        List<EconomyLetter> economyLetters = (List<EconomyLetter>) economyLetterRepository.findAll();

        List<Map<String, String>> response = new ArrayList<>();

        for (EconomyLetter letter : economyLetters) {
            Map<String, String> letterMap = new HashMap<>();
            letterMap.put("title", letter.getTitle());
            letterMap.put("editor", letter.getEditor().getName());
            letterMap.put("elapsedTime", "1시간 전");
            letterMap.put("uploadDate", String.valueOf(letter.getCreatedAt()));
            letterMap.put("text", letter.getText());
            response.add(letterMap);
        }

        return response;
    }
}

package salary_BE.salary.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import salary_BE.salary.Service.SeedService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SeedController {

    private final SeedService seedService;

    // 시드 변경
    @PatchMapping("/seed/update")
    public ResponseEntity<Map<String, String>> updateSeed(@RequestBody Map<String, Integer> requestBody) {

        Integer seed_earned = requestBody.get("seed_earned");
        Integer seed_used = requestBody.get("seed_used");

        seedService.updateSeed(seed_earned, seed_used);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
}

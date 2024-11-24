package salary_BE.salary.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    @Column(name = "attendance_state")
    private Integer attendanceState;

    @Column(name = "today_salaryPoint") // 로직 수정 후 삭제 할 칼럼
    private Integer todaySalaryPoint;

    // 시드 적립과 사용 구분
    @Column(name = "today_SalaryPoint_earned")
    private Integer todaySalaryPoint_earned;

    @Column(name = "today_SalaryPoint_used")
    private Integer todaySalaryPoint_used;

    @OneToMany(mappedBy = "attendance", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TodayStudy> todayStudies;

    @Column(name = "last_word_id", nullable = false)
    private Long lastWordId = 0L; // 마지막 학습한 단어 ID 저장을 위한 속성

}

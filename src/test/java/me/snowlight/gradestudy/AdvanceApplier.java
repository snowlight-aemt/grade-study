package me.snowlight.gradestudy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AdvanceApplier {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;

    public AdvanceApplier(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ApplyResult apply(Targets targets) {
        logger.info("Start Batch Update");
        Map<Integer, GradeCount> gradeCountMap = new HashMap<>();

//        int count = 1000;
//        final String sql = "update student set grade = ? where stu_id = ?";
//        LongAdder adder = new LongAdder();
//        Flux.fromStream(targets.getUsers().stream())
//                .buffer(count)
//                .subscribe((users) -> {
//                    this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//                        @Override
//                        public void setValues(PreparedStatement ps, int i) throws SQLException {
//                            User user = users.get(i);
//                            int nextGrade = user.getGrade() + 1;
//                            ps.setInt(1, nextGrade);
//                            ps.setInt(2, user.getId());
//                        }
//
//                        @Override
//                        public int getBatchSize() {
//                            return users.size();
//                        }
//                    });
//                    adder.add(users.size());
//                    System.out.println("Processing ... " + (adder.intValue()));
//                });


        for (User user : targets.getUsers()) {
            int nextGrade = user.getGrade() + 1;
            jdbcTemplate.update("update student set grade = ? where stu_id = ?",
                    nextGrade,
                    user.getId());

            GradeCount gradeCount = gradeCountMap.get(nextGrade);
            if (gradeCount == null) {
                gradeCount = new GradeCount(nextGrade, 0);
                gradeCountMap.put(nextGrade, gradeCount);
            }
            gradeCount.inc();
        }
        logger.info("End Batch Update");

        return new ApplyResult(gradeCountMap);
    }
}


//2023-05-25T23:41:30.902+09:00  INFO 3121 --- [    Test worker] me.snowlight.gradestudy.AdvanceApplier   : Start Batch Update
//2023-05-25T23:41:44.582+09:00  INFO 3121 --- [    Test worker] me.snowlight.gradestudy.AdvanceApplier   : End Batch Update



//2023-05-26T00:17:14.260+09:00  INFO 3490 --- [    Test worker] me.snowlight.gradestudy.AdvanceApplier   : Start Batch Update
//Processing ... 1000
//Processing ... 2000
//Processing ... 3000
//Processing ... 4000
//Processing ... 5000
//Processing ... 6000
//Processing ... 7000
//Processing ... 8000
//Processing ... 9000
//Processing ... 10000
//2023-05-26T00:17:27.119+09:00  INFO 3490 --- [    Test worker] me.snowlight.gradestudy.AdvanceApplier   : End Batch Update




// 2023-05-26T00:14:42.469+09:00  INFO 3461 --- [    Test worker] me.snowlight.gradestudy.AdvanceApplier   : Start Batch Update
// Processing ... 10000
// 2023-05-26T00:14:55.706+09:00  INFO 3461 --- [    Test worker] me.snowlight.gradestudy.AdvanceApplier   : End Batch Update



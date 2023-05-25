package me.snowlight.gradestudy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("test")
public class TenThousandInsertTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        this.jdbcTemplate.execute("TRUNCATE TABLE student");
    }

    @Test
    void insert() {
        final String sql = "INSERT INTO student (stu_id, grade) VALUES (?, ?)";

        IntStream range = IntStream.rangeClosed(0, 10000);
        Collection<List<Integer>> values = range.boxed().collect(Collectors.groupingBy(o -> o / 1000)).values();
//
        LongAdder adder = new LongAdder();
        values.forEach(cnts -> {
            this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, cnts.get(i));
                    ps.setInt(2, (cnts.get(i) % 3) + 1);
                }

                @Override
                public int getBatchSize() {
                    return cnts.size();
                }
            });
            adder.increment();
            System.out.println("Processing ... " + (cnts.size() * adder.intValue()));
        });

    }
}

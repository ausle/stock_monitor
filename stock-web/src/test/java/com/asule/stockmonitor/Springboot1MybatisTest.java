package com.asule.stockmonitor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class Springboot1MybatisTest {

    @Autowired
    TeamMapper teamMapper;

    @Test
    public void test1() {
        Team team = teamMapper.selectByPrimaryKey(1);
        System.out.println(team);
    }
}

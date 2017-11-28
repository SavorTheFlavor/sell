package com.me;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2017/11/28.
 */
@RunWith(SpringRunner.class)  //SpringRunner is an alias for the SpringJUnit4ClassRunner.
@SpringBootTest
//@Slf4j //Simple Logging Facade for Java
public class LoggerTest {
    private final Logger logger = LoggerFactory.getLogger(Logger.class);

    @Test
    public void test1(){
        logger.info("name={}, password={} ","may","1234");
        logger.error("!!!!!!!!!!!!!!!!");
    }
}

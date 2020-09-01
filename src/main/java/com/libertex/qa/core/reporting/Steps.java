package com.libertex.qa.core.reporting;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Быстрый воркэраунд для более подробного репортинга в аллюр,
 * хотя я бы предпочёл отказаться от его анатаций в тестах
 */
public class Steps {
    private final static Logger log = LogManager.getLogger(Steps.class);

    @Step(" +++ [Step] {message}")
    public static void info(String message){
        log.info("Step: " + message);
    }
}

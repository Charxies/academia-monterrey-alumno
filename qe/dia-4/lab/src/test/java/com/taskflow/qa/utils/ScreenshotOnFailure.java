package com.taskflow.qa.utils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestWatcher;

/**
 * ScreenshotOnFailure (MP-9) — captura un screenshot en el INSTANTE del fallo y lo adjunta a Allure.
 *
 * La trampa: TestWatcher.testFailed corre DESPUÉS de @AfterEach (driver YA cerrado →
 * NoSuchSessionException). Por eso NO se captura ahí. Se captura en handleTestExecutionException
 * (TestExecutionExceptionHandler): momento exacto del fallo, driver vivo. Se RE-LANZA la
 * excepción para que el fallo siga siendo fallo; testFailed solo loguea.
 *
 * Las interfaces ya están declaradas. Completa los cuerpos con TODO.
 */
public class ScreenshotOnFailure implements TestExecutionExceptionHandler, TestWatcher {

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable)
            throws Throwable {
        // TODO MP-9: obtener el WebDriver de la instancia de test (getDriver()), sacar
        //            getScreenshotAs(OutputType.BYTES) y Allure.addAttachment(...); LUEGO re-lanzar.
        throw throwable;
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        // TODO MP-9: corre DESPUÉS de @AfterEach (driver cerrado): aquí SOLO se loguea el resultado.
    }
}

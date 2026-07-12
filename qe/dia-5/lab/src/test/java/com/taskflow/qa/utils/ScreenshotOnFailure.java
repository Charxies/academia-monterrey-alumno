package com.taskflow.qa.utils;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;

/**
 * ScreenshotOnFailure — captura un screenshot en el INSTANTE del fallo y lo adjunta a Allure.
 *
 * La trampa (MP-9): TestWatcher.testFailed corre DESPUÉS de @AfterEach, o sea con el driver YA
 * cerrado (NoSuchSessionException). Por eso NO capturamos ahí. Capturamos en
 * handleTestExecutionException (TestExecutionExceptionHandler): ese momento es exacto — el
 * driver sigue vivo y el test-case de Allure sigue activo. Re-lanzamos la excepción para que
 * el fallo SIGA siendo un fallo. testFailed queda solo para loguear el resultado.
 *
 * Se activa poniendo @ExtendWith(ScreenshotOnFailure.class) en BaseTest.
 */
public class ScreenshotOnFailure implements TestExecutionExceptionHandler, TestWatcher {

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable)
            throws Throwable {
        WebDriver driver = driverDe(context);
        if (driver instanceof TakesScreenshot ts) {
            byte[] png = ts.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("screenshot al fallo", "image/png",
                    new ByteArrayInputStream(png), "png");
        }
        // Re-lanzar SIEMPRE: no tragarse el fallo, solo documentarlo.
        throw throwable;
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        // Corre DESPUÉS de @AfterEach (driver ya cerrado): aquí NO se captura, solo se loguea.
        System.out.println("[ScreenshotOnFailure] test falló: " + context.getDisplayName());
    }

    /**
     * Obtiene el WebDriver de la instancia de test (BaseTest.getDriver()) por reflexión, para
     * no acoplar utils/ a la clase concreta de tests/. Si no lo encuentra, devuelve null y el
     * fallo se re-lanza sin screenshot.
     */
    private static WebDriver driverDe(ExtensionContext context) {
        Object testInstance = context.getTestInstance().orElse(null);
        if (testInstance == null) {
            return null;
        }
        try {
            Method getDriver = testInstance.getClass().getMethod("getDriver");
            Object valor = getDriver.invoke(testInstance);
            return (valor instanceof WebDriver wd) ? wd : null;
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }
}

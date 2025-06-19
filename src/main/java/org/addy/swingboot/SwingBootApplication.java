package org.addy.swingboot;

import lombok.extern.slf4j.Slf4j;
import org.addy.swingboot.ui.MainWindow;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.awt.*;

@Slf4j
@SpringBootApplication
public class SwingBootApplication {
	private static ConfigurableApplicationContext context;

	public static <T> T getBean(Class<T> beanClass) {
		return context.getBean(beanClass);
	}

	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}

	public static void main(String[] args) {
		context = new SpringApplicationBuilder(SwingBootApplication.class)
				.headless(false)
				.web(WebApplicationType.NONE)
				.run(args);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			log.warn("Could not initialize the look-and-feel: {}", e.getLocalizedMessage());
		}

		EventQueue.invokeLater(() -> {
			var mainWindow = getBean(MainWindow.class);
			mainWindow.setVisible(true);
		});
	}

}

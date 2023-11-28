package me.gefro.ngrokmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling


@EnableAsync
@ComponentScan(
	basePackages = [
		"tools",
		"ngrok"
	]
)
@SpringBootApplication
class NgrokManagerApplication

fun main(args: Array<String>) {
	runApplication<NgrokManagerApplication>(*args)
}

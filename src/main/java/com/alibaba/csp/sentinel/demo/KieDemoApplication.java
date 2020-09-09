package com.alibaba.csp.sentinel.demo;

import com.alibaba.csp.sentinel.datasource.DataSourceCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication()
@ComponentScan(value = "com.alibaba.csp.sentinel.datasource")
public class KieDemoApplication {
	@Autowired
	DataSourceCenter dataSourceCenter;

	public static void main(String[] args) {
		SpringApplication.run(KieDemoApplication.class, args);

		FlowQpsRunner flowQpsRunner = new FlowQpsRunner();
		flowQpsRunner.simulateTraffic();
		flowQpsRunner.tick();
	}
}

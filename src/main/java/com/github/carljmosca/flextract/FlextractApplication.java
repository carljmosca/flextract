/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.flextract;

import com.github.carljmosca.flextract.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlextractApplication implements CommandLineRunner {

	// @Value to inject
	// command line args ('--name=whatever') or application properties

	@Autowired
	private ExportService exportService;

	@Override
	public void run(String... args) {
            exportService.process();
//		System.out.println(this.exportService.getHelloMessage());
//		if (args.length > 0 && args[0].equals("exitcode")) {
//			throw new ExitException();
//		}
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(FlextractApplication.class, args);
	}

}
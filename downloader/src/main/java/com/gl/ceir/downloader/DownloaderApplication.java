package com.gl.ceir.downloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@EnableEncryptableProperties
@SpringBootApplication
public class DownloaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DownloaderApplication.class, args);
	}

}

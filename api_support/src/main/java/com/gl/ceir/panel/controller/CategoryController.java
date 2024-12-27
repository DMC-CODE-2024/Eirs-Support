package com.gl.ceir.panel.controller;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.constant.AccessEnum;
import com.gl.ceir.panel.constant.CategoryEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@RestController
@Log4j2
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {
	@GetMapping("list")
	public @ResponseBody ResponseEntity<?> accessList() {
		return new ResponseEntity<>(Arrays.asList(CategoryEnum.values()), HttpStatus.OK);
	}
}

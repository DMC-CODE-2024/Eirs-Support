package com.gl.ceir.panel.validation;


import org.springframework.stereotype.Component;

import com.gl.ceir.panel.service.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Component
@RequiredArgsConstructor
@Log4j2
public class EmailValidator implements ConstraintValidator<EmailConstraint, String> {

	@Override
	public void initialize(EmailConstraint email) {

	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		return UserService.getUserService().emailExist(email);
	}

}

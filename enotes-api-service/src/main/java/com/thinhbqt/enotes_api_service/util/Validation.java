package com.thinhbqt.enotes_api_service.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.thinhbqt.enotes_api_service.dto.CategoryDto;
import com.thinhbqt.enotes_api_service.dto.TodoDto;
import com.thinhbqt.enotes_api_service.dto.TodoDto.StatusDto;
import com.thinhbqt.enotes_api_service.dto.UserDto;
import com.thinhbqt.enotes_api_service.enums.TodoStatus;
import com.thinhbqt.enotes_api_service.exception.ResourceNotFoundException;
import com.thinhbqt.enotes_api_service.exception.ValidationException;
import com.thinhbqt.enotes_api_service.repository.RoleRepository;
import com.thinhbqt.enotes_api_service.repository.UserRepository;

@Component
public class Validation{
	@Autowired
    private RoleRepository roleRepository;
	@Autowired	
	private UserRepository userRepository;
    
    public void categoryValidation(CategoryDto categoryDto) {

		Map<String, Object> error = new LinkedHashMap<>();

		if (ObjectUtils.isEmpty(categoryDto)) {
			throw new IllegalArgumentException("category Object/JSON shouldn't be null or empty");
		} else {

			// validation name field
			if (ObjectUtils.isEmpty(categoryDto.getName())) {
				error.put("name", "name field is empty or null");
			} else {
				if (categoryDto.getName().length() < 3) {
					error.put("name", "name length min 3");
				}
				if (categoryDto.getName().length() > 100) {
					error.put("name", "name length max 100");
				}
			}

			// validation dscription
			if (ObjectUtils.isEmpty(categoryDto.getDescription())) {
				error.put("description", "description field is empty or null");
			}

			// validation isActive
			if (ObjectUtils.isEmpty(categoryDto.getIsActive())) {
				error.put("isActive", "isActive field is empty or null");
			} else {
				if (categoryDto.getIsActive() != Boolean.TRUE.booleanValue()
						&& categoryDto.getIsActive() != Boolean.FALSE.booleanValue()) {
					error.put("isActive", "invalid value isActive field ");
				}
			}
		}
		if (!error.isEmpty()) {
			throw new ValidationException(error);
		}

	}
	public void todoValidation(TodoDto todoDto){
		StatusDto reqStatus = todoDto.getStatus();
		Boolean statusFound = false;
		for (TodoStatus st : TodoStatus.values()) {
			if (st.getId().equals(reqStatus.getId())) {
				statusFound = true;
			}
		}
		if (!statusFound) {
			throw new ResourceNotFoundException("invalid status");
		}
	}
	

    public void validateUser(UserDto userDto) {
        if (!StringUtils.hasText(userDto.getFirstName())) {
            throw new IllegalArgumentException("First name is invalid");
        }
        if (!StringUtils.hasText(userDto.getLastName())) {
            throw new IllegalArgumentException("Last name is invalid");
        }
        
        if (!StringUtils.hasText(userDto.getEmail()) || !userDto.getEmail().matches(Constants.EMAIL_REGEX)) {
            throw new IllegalArgumentException("Email is invalid");
        }
		else{
			if(userRepository.existsByEmail(userDto.getEmail())){
				throw new IllegalArgumentException("Email already exists");
			}
		}
        if (!StringUtils.hasText(userDto.getMobileNumber()) || !userDto.getMobileNumber().matches(Constants.MOBILE_REGEX)) {
            throw new IllegalArgumentException("Mobile number is invalid");
        }
        if (CollectionUtils.isEmpty(userDto.getRoles())) {
            throw new IllegalArgumentException("Role is invalid");
        }

        List<Integer> dbRoleIds = roleRepository.findAll().stream()
                .map(role -> role.getId())
                .collect(Collectors.toList());

        List<Integer> invalidIds = userDto.getRoles().stream()
                .map(UserDto.RoleDto::getId)
                .filter(id -> !dbRoleIds.contains(id))
                .collect(Collectors.toList());

        if (!invalidIds.isEmpty()) {
            throw new IllegalArgumentException("Role invalid: " + invalidIds);
        }
    }
}

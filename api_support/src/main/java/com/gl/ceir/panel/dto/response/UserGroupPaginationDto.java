package com.gl.ceir.panel.dto.response;

import java.io.Serializable;
import java.util.List;

import com.gl.ceir.panel.dto.GroupDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserGroupPaginationDto implements Serializable {
	private static final long serialVersionUID = -7574572958525730695L;
	private Long id;
	private String userName;
	private List<GroupDto> groups;
}

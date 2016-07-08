package com.example.controller;


import java.security.Principal;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.BMCCellDescription;
import com.example.model.User;

@RestController
public class ProjectStatusRetrieverController {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "/status/getActiveProjectsData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public int getActiveProjectsData() {

		String str = "select count(projectid) from projectmaster where projectstatus='active'";

		int count = jdbcTemplate.queryForObject(str, Integer.class);

		return count;
	}

	@RequestMapping(value = "/status/getGraduatedProjectsData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public int getGraduatedProjectsData() {
		String str = "select count(projectid) from projectmaster where projectstatus='graduated'";
		int count = jdbcTemplate.queryForObject(str, Integer.class);

		return count;
	}

	@RequestMapping(value = "/status/getUserCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public int getUserCount() {

		String str = "select count(userid) from userinfo";
		int count = jdbcTemplate.queryForObject(str, Integer.class);

		return count;

	}

}

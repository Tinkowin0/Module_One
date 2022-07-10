package com.jdc.project.model.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import com.jdc.project.model.dto.Project;
import com.jdc.project.model.service.utils.ProjectHelper;

@Service
public class ProjectService {

	@Autowired
	private ProjectHelper projectHelper;

	@Autowired
	private SimpleJdbcInsert projectInsert;

	public int create(Project project) {
		projectHelper.validate(project);
		return projectInsert.executeAndReturnKey(projectHelper.insertParams(project)).intValue();

	}

	private BeanPropertyRowMapper<Project> row;

	public ProjectService() {
		row = new BeanPropertyRowMapper<Project>(Project.class);
	}

	private String find = """
			select p.id, p.name, p.description, p.start StartDate, p.months ,m.id ManagerId, m.name managerName,
			 m.login_id managerLogin from project p inner join member m on p.manager = m.id where p.id  =?
			""";

	public Project findById(int id) {
		return projectInsert.getJdbcTemplate().queryForObject(find, row, id);
	}

	private String search = """
				select p.id, p.name, p.description, p.start StartDate, p.months ,m.id ManagerId, m.name managerName,
			m.login_id managerLogin, Month(start)+months EndDate from project p inner join member m on p.manager = m.id where
					 (p.name like ? OR m.name like ? OR p.start like ?)
					 """;

	private String select = """
			select p.id, p.name, p.description, p.start StartDate, p.months ,m.id ManagerId, m.name managerName,
			m.login_id managerLogin, Month(start)+months EndDate from project p inner join member m on p.manager = m.id
				""";

	public List<Project> search(String project, String manager, LocalDate dateFrom, LocalDate dateTo) {

		if (project == null && manager == null && dateFrom == null && dateTo == null) {
			return projectInsert.getJdbcTemplate().query(select, row);
		} else if (dateTo != null) {

			var p = new ArrayList<Project>();
			for (Project i : projectInsert.getJdbcTemplate().query(select, row)) {

				if (i.getEndDate() % 12 == dateTo.getMonthValue())
					p.add(i);
			}
			return p;
		} else {
			var st = dateFrom != null ? dateFrom.toString().substring(0, 7) : null;
			return projectInsert.getJdbcTemplate().query(search, row, project + "%", manager + "%", st + "%");

		}
	}

	private String sql = "update project set name = ?, description = ?, start = ?, months = ?  where id =?";

	public int update(int id, String name, String description, LocalDate startDate, int month) {
		return projectInsert.getJdbcTemplate().update(sql, name, description, startDate, month, id);
	}

	public int deleteById(int id) {
		return projectInsert.getJdbcTemplate().update("delete from project where id = ?", id);
	}
}

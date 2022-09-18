package com.jdc.leaves.model.service;

import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdc.leaves.model.dto.input.LeaveForm;
import com.jdc.leaves.model.dto.output.LeaveListVO;
import com.jdc.leaves.model.dto.output.LeaveSummaryVO;

@Service
public class LeaveService {

	private NamedParameterJdbcTemplate template;
	
	private static final String LEAVE_COUNT_SQL = """
			select count(leave_date) from leaves_day 
			where leave_date = :target and leaves_classes_id = :classId
			""";
	@Autowired
	private ClassService classService;
	
	@Autowired
	private StudentService studentService;
	
	private SimpleJdbcInsert insert;
	
	private SimpleJdbcInsert insertDay;

	public LeaveService(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
		
		insert = new SimpleJdbcInsert(dataSource);
		insert.setTableName("leaves");
		
		insertDay = new SimpleJdbcInsert(dataSource);
		insertDay.setTableName("leaves_day");
		
		
	}
	
	private static final String SELECT = """
			 select cs.description className , at.name student, a.name teacher, s.phone studentPhone, l.apply_date applyDate, 
             l.start_date startDate, l.days , l.reason from 
             leaves l join student s on s.id = l.student_id
             join classes cs on cs.id = l.classes_id
             join teacher t on t.id = cs.teacher_id
             join account at on at.id = s.id
             join account a on a.id = t.id
			""";
	private static final String GROUP = """
			 group by cs.description, at.name, a.name, s.phone, l.apply_date, l.start_date, l.days , l.reason
			""";
	public List<LeaveListVO> search(Optional<Integer> classId, Optional<LocalDate> from,
			Optional<LocalDate> to) {
		var where = new StringBuffer();
		var param = new HashMap<String, Object>();
		
		if(classId == null) {
			param.put("std", getStuentId());
			where.append("and l.student_id = :std");
			
		}else {
			where.append(classId.map(a -> {
				param.put("id", a);
				return "and l.classes_id = :id";
			}).orElse(""));
		}
		where.append(from.map(a -> {
			param.put("from", Date.valueOf(a));
			return " and l.apply_date >= :from";
		}).orElse(""));

		where.append(to.map(a -> {
			param.put("to", Date.valueOf(a));
			return " and l.start_date <= :to";
		}).orElse(""));
		
		var sql = "%s where 1 = 1 %s %s".formatted(SELECT,where.toString(),GROUP);
		
		var result = template.query(sql, param,
				new BeanPropertyRowMapper<>(LeaveListVO.class));
		
		return result;
	}

	@Transactional
	public void save(LeaveForm form) throws DuplicateKeyException{
		
		if(form.getStartDate().isAfter(LocalDateTime.now().minusDays(1).toLocalDate()) ) {
			
			insert.execute(
					Map.of("apply_date", LocalDateTime.now(),
					"classes_id",form.getClassId(),
					"student_id",form.getStudentId(),
					"start_date", Date.valueOf(form.getStartDate()),
					"days", form.getDays(),
					"reason", form.getReason()
					));
						
				insertDay.execute(Map.of(
						"leave_date", Date.valueOf(form.getStartDate()),
						"leaves_apply_date", LocalDateTime.now(),
						"leaves_classes_id",form.getClassId(),
						"leaves_student_id",form.getStudentId()
						));
		}else {
			 throw new DateTimeException("The start date should be bigger than or equal today's date (%s)"
					 .formatted(LocalDateTime.now().toLocalDate()));
		}	
	}

	public List<LeaveSummaryVO> searchSummary(Optional<LocalDate> target) {

		// Find Classes
		var classes = classService.search(Optional.ofNullable(null), target,
				Optional.ofNullable(null));
		
		var result = classes.stream().map(LeaveSummaryVO::new).toList();
		
		for(var vo : result) {
			vo.setLeaves(findLeavesForClass(vo.getClassId(), target.orElse(LocalDate.now()))); 
		}
		return result;
	}

	private long findLeavesForClass(int classId, LocalDate date) {
		return template.queryForObject(LEAVE_COUNT_SQL, 
				Map.of("classId", classId, "target", Date.valueOf(date)), Long.class);
	}
	
	private Integer getStuentId() {
		var stdId = studentService.findStudentByEmail(SecurityContextHolder
				.getContext().getAuthentication().getName());
		return stdId;
	}
}
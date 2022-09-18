package com.jdc.leaves.model.dto.input;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class LeaveForm {

	public LeaveForm() {
	}

	public LeaveForm(int classId, int studentId) {
		super();
		this.classId = classId;
		this.studentId = studentId;
	}

	public LeaveForm(int classId, int studentId, LocalDate applyDate, LocalDate startDate, int days, String reason) {
		super();
		this.classId = classId;
		this.studentId = studentId;
		this.applyDate = applyDate;
		this.startDate = startDate;
		this.days = days;
		this.reason = reason;
	}

	@Override
	public int hashCode() {
		return Objects.hash(applyDate, classId, days, reason, startDate, studentId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LeaveForm other = (LeaveForm) obj;
		return Objects.equals(applyDate, other.applyDate) && classId == other.classId && days == other.days
				&& Objects.equals(reason, other.reason) && Objects.equals(startDate, other.startDate)
				&& studentId == other.studentId;
	}

	private int classId;

	private int studentId;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate applyDate;

	@NotNull(message = "Please enter leave start date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	@Min(value = 1, message = "Please enter leave days.")
	private int days;

	@NotEmpty(message = "Please enter reason for leaves.")
	private String reason;

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDate getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(LocalDate applyDate) {
		this.applyDate = applyDate;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
}
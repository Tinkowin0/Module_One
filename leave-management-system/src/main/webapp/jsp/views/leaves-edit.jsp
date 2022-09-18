<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Leaves | Home</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css">

</head>
<body>

	<c:import url="/jsp/include/navbar.jsp">
		<c:param name="view" value="leaves"></c:param>
	</c:import>

	<div class="container">
		
		<h3 class="my-4">Leave Application</h3>
		<div>
			<span class="text text-danger">${res}</span>
		</div>
		<div class="row">
		
			<c:url var="leaves" value="/leaves"></c:url>
			<sf:form method="post" modelAttribute="form" action="${leaves }" cssClass="col-6">
					<sf:hidden path="classId"/>
					<sf:hidden path="studentId"/>
				<div class="mb-3">
					<label class="form-label">Start Date</label>
					<sf:input type = "date" path="startDate" cssClass="form-control"/>
					<sf:errors path="startDate" cssClass="text-secondary"></sf:errors>
				</div>
				<div class="mb-3">
					<label class="form-label">Leave Day</label>
					<sf:input type = "number" path="days" cssClass="form-control"/>
					<sf:errors path="days" cssClass="text-secondary"></sf:errors>
				</div>
				<div class="mb-3">
					<label class="form-label">Reasons</label>
					<sf:textarea path="reason" cssClass="form-control" placeholder = "Enter reason"/>
					<sf:errors path="reason" cssClass="text-secondary"></sf:errors>
				</div>
				<div>
				<button class="btn btn-outline-danger">
					<i class="bi bi-save">Save</i>
				</button>
				</div>
			</sf:form>
		</div>
	</div>
</body>
</html>
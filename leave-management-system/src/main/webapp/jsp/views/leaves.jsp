<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Leaves | Home</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css">

<c:url var="commonCss" value="/resources/application.css"></c:url>
<link rel="stylesheet" href="${commonCss}" />
</head>
<body>

	<c:import url="/jsp/include/navbar.jsp">
		<c:param name="view" value="leaves"></c:param>
	</c:import>

	<div class="container">
		
		<h3 class="my-4">Your Leaves</h3>
		
		<!-- Search Form -->
		<form class="row mb-4">
			<!-- Date From -->		
			<div class="col-auto">
				<label class="form-label">Date From</label>
				<input type="date" name="from" class="form-control" value="${ param.from }" />
			</div>
			<!-- Date To -->	
			<div class="col-auto">
				<label class="form-label">Date To</label>
				<input type="date" name="to" class="form-control" value="${ param.to }" />
			</div>
			<!-- Search Button -->
			<div class="col btn-wrapper">
				<button class="btn btn-outline-success me-2"><i class="bi bi-search"></i> Search</button>
			</div>
		</form>		
		
		<!-- Search Result -->
		<c:choose>
			<c:when test="${empty dto }">
				<div class="alert alert-info">
					There is no data.
				</div>
			</c:when>
			<c:otherwise>
			<table class="table table-hover">
				<thead>
					<tr>
						<th>Teacher</th>
						<th>Class</th>
						<th>Apply Date</th>
						<th>Start Date</th>
						<th>Days</th>
						<th>Reason</th>
					</tr>
				</thead>
				
				<tbody>
				<c:forEach var="lev" items="${dto }">
					<tr>
						<td>${lev.teacher}</td>
						<td>${lev.className}</td>
						<td>${lev.applyDate}</td>
						<td>${lev.startDate}</td>
						<td>${lev.days} Days</td>
						<td>${lev.reason}</td>
					</tr>
					</c:forEach>
				</tbody>
			
			</table>
			
			</c:otherwise>
		</c:choose>
	
	</div>
</body>
</html>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<c:set var="title" value="Sign up"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body class="gradient-custom-for-page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<fmt:message key="register.user_exists" var="user_exists"/>
<fmt:message key="register.success" var="success"/>
<fmt:message key="message.error" var="error"/>
<fmt:message key="message.invalid_input" var="invalid_input"/>
<fmt:message key="register.input.name" var="name"/>
<fmt:message key="register.input.surname" var="surname"/>
<fmt:message key="register.input.login" var="login"/>
<fmt:message key="register.input.password" var="password"/>
<fmt:message key="register.input.email" var="email"/>
<fmt:message key="register.input.phone" var="phone"/>
<fmt:message key="register.btn.signup" var="signup"/>
<fmt:message key="register.header.signup" var="header_signup"/>

<c:if test="${not empty sessionScope.userId}">
    <c:redirect url="/controller?command=go_to_main_page"/>
</c:if>
<div class="text-center mt-5">
    <form action="${pageContext.request.contextPath}/controller?command=register" method="post"
          style="max-width: 480px;margin: auto">
        <h1 class="h3 mb-3 font-weight-normal">${header_signup}</h1>
        <div class="form-group row">
            <label for="name" class="mt-2 col-md-4 col-form-label text-end">${name}</label>
            <div class="col-md-8 mt-2">
                <input type="text" id="name" class="form-control" name="name">
            </div>
        </div>
        <div class="form-group row">
            <label for="surname" class="mt-2 col-md-4 col-form-label text-end">${surname}</label>
            <div class="col-md-8 mt-2">
                <input type="text" id="surname" class="form-control" name="surname">
            </div>
        </div>
        <div class="form-group row">
            <label for="login" class="mt-2 col-md-4 col-form-label text-end">${login}</label>
            <div class="col-md-8 mt-2">
                <input type="text" id="login" class="form-control" name="login">
            </div>
        </div>
        <div class="form-group row">
            <label for="password" class="mt-2 col-md-4 col-form-label text-end">${password}</label>
            <div class="col-md-8 mt-2">
                <input type="password" id="password" class="form-control" name="password">
            </div>
        </div>
        <div class="form-group row">
            <label for="email" class="mt-2 col-md-4 col-form-label text-end">${email}</label>
            <div class="col-md-8 mt-2">
                <input type="text" id="email" class="form-control" name="email">
            </div>
        </div>
        <div class="form-group row">
            <label for="phone" class="mt-2 col-md-4 col-form-label text-end">${phone}</label>
            <div class="col-md-8 mt-2">
                <input type="text" id="phone" class="form-control" name="phone">
            </div>
        </div>
        <c:if test="${not empty pageContext.request.getParameter('message')}">
            <c:if test="${pageContext.request.getParameter('message') eq 'success'}">
                <div class="alert alert-success alert-dismissible fade show mt-2 d-flex justify-content-between"
                     role="alert">
                    <div class="left"><strong>Sorry!</strong><c:out value="${success}"/></div>
                    <div class="right">
                <span type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </span>
                    </div>
                </div>
            </c:if>
        </c:if>
        <c:if test="${not empty pageContext.request.getParameter('message')}">
            <div class="alert alert-danger alert-dismissible fade show mt-2 d-flex justify-content-between"
                 role="alert">
                <div class="left">
                    <strong>Sorry!</strong>
                    <c:if test="${pageContext.request.getParameter('message') eq 'error'}">
                        <c:out value="${error}"/>
                    </c:if>
                    <c:if test="${pageContext.request.getParameter('message') eq 'user_exists'}">
                        <c:out value="${user_exists}"/>
                    </c:if>
                    <c:if test="${pageContext.request.getParameter('message') eq 'invalid_input'}">
                        <c:out value="${invalid_input}"/>
                    </c:if>
                </div>
                <div class="right">
                <span type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </span>
                </div>
            </div>
        </c:if>
        <div class="mt-3 mb-3 d-grid">
            <button class="btn btn-lg btn-success btn-block">${signup}</button>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/jspf/bootstrapScripts.jspf" %>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>




<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<!DOCTYPE html>
<c:set var="title" value="Login"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body class="gradient-custom-for-page">
<fmt:message key="message.error" var="error"/>
<fmt:message key="login.incorrect_credentials" var="incorrect_credentials"/>
<fmt:message key="login.user_logged" var="user_logged"/>

<fmt:message key="register.input.login" var="input_login"/>
<fmt:message key="register.input.password" var="password"/>
<fmt:message key="login.btn.signin" var="signin"/>
<fmt:message key="login.header.signin" var="header_signin"/>
<fmt:message key="login.btn.register" var="register"/>

<%@ include file="/WEB-INF/jspf/header.jspf" %>
<c:if test="${not empty sessionScope.userId}">
    <c:redirect url="/controller?command=go_to_main_page"/>
</c:if>
<div class="text-center mt-5">
    <div class="container message" style="max-width: 480px;margin: auto">
        <c:if test="${not empty pageContext.request.getParameter('message')}">
            <div class="alert alert-danger alert-dismissible fade show mt-2 d-flex justify-content-between"
                 role="alert">
                <div class="left">
                    <strong>Sorry!</strong>
                    <c:if test="${pageContext.request.getParameter('message') eq 'user_logged'}">
                        <c:out value="${user_logged}"/>
                    </c:if>
                    <c:if test="${pageContext.request.getParameter('message') eq 'incorrect_credentials'}">
                        <c:out value="${incorrect_credentials}"/>
                    </c:if>
                    <c:if test="${pageContext.request.getParameter('message') eq 'error'}">
                        <c:out value="${error}"/>
                    </c:if>
                </div>
                <div class="right">
            <span type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </span>
                </div>
            </div>
        </c:if>
    </div>
    <form action="${pageContext.request.contextPath}/controller" method="post"
          style="max-width: 480px;margin: auto">
        <input type="hidden" name="command" value="login">
        <h1 class="h3 mb-3 font-weight-normal">${header_signin}</h1>
        <label class="sr-only invisible" for="login">${input_login}</label>
        <input type="text" id="login" class="form-control" placeholder="${input_login}" name="login" required autofocus/>
        <label class="sr-only invisible" for="password">${password}</label>
        <input type="password" id="password" class="form-control" placeholder="${password}" name="password" required
               autofocus autocomplete/>
        <div class="mt-3 mb-3 d-grid">
            <button class="btn btn-lg btn-primary btn-block">${signin}</button>
        </div>
    </form>
    <div class="mt-3 mb-3 d-grid" style="max-width: 480px;margin: auto">
        <a class="btn btn-lg btn-secondary btn-block"
           href="${pageContext.request.contextPath}/controller?command=get_register_page" role="button">${register}</a>
    </div>
</div>
<%@ include file="/WEB-INF/jspf/bootstrapScripts.jspf" %>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>

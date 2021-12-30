<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<fmt:message key="message.error" var="error"/>

<!DOCTYPE html>
<c:set var="title" value="Error"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body class="gradient-custom-for-page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<p class="text-center fs-1 fw-bold">
    505
</p>
<c:if test="${not empty errorMessage}">
    <p class="text-center fs-2 fw-light ">${error}</p></c:if>
<div class="gradient-custom-for-hr">
    <hr style="margin: 1%">
</div>
<div class="container text-center">
    <button class="btn btn-dark" style="width: 50%" type="button" name="back"
            onclick="history.back()">
        Back
    </button>
</div>

<%@ include file="/WEB-INF/jspf/bootstrapScripts.jspf" %>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
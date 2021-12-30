<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<fmt:message key="search.no_available_apartments" var="No_available_apartments"/>
<fmt:message key="action.confirm" var="confirm"/>

<!DOCTYPE html>
<c:set var="title" value="Order processing"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body class="gradient-custom-for-page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<p class="text-center fs-1 fw-bold">Order processing</p>
<div class="container">
    <div class="album py-5">
        <div class="container">
            <div class="row">
                <c:choose>
                    <c:when test="${fn:length(apartments) == 0}">
                        <p class="fs-1 fw-light ">
                            ${No_available_apartments}
                        </p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="item" items="${apartments}">
                            <div class="col-md-4">
                                <div class="card mb-4 box-shadow">
                                    <img class="card-img-top"
                                         data-src="holder.js/100px225?theme=thumb&amp;bg=55595c&amp;fg=eceeef&amp;text=Thumbnail"
                                         alt="Thumbnail [100%x225]"
                                         style="height: 225px; width: 100%; display: block;"
                                         src="data:image/svg+xml;charset=UTF-8,%3Csvg%20width%3D%22348%22%20height%3D%22225%22%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20viewBox%3D%220%200%20348%20225%22%20preserveAspectRatio%3D%22none%22%3E%3Cdefs%3E%3Cstyle%20type%3D%22text%2Fcss%22%3E%23holder_179ae30d15b%20text%20%7B%20fill%3A%23eceeef%3Bfont-weight%3Abold%3Bfont-family%3AArial%2C%20Helvetica%2C%20Open%20Sans%2C%20sans-serif%2C%20monospace%3Bfont-size%3A17pt%20%7D%20%3C%2Fstyle%3E%3C%2Fdefs%3E%3Cg%20id%3D%22holder_179ae30d15b%22%3E%3Crect%20width%3D%22348%22%20height%3D%22225%22%20fill%3D%22%2355595c%22%3E%3C%2Frect%3E%3Cg%3E%3Ctext%20x%3D%22116.7109375%22%20y%3D%22120.1078125%22%3EThumbnail%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fsvg%3E"
                                         data-holder-rendered="true">
                                    <div class="card-body">
                                        <p class="card-text">${item.name}</p>
                                        <p class="card-text">${item.type}</p>
                                        <div class="d-flex justify-content-between align-items-center">
                                            <div class="btn-group">
                                                <a class="btn btn-sm btn-outline-secondary"
                                                   href="${pageContext.request.contextPath}/controller?command=process_order&apartment_id=${item.id}&order_id=${order_id}"
                                                   role="button">${confirm}</a>
                                            </div>
                                            </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                <div class="container text-center">
                    <button class="btn btn-dark" style="width: 50%" type="button" name="back"
                            onclick="history.back()">
                        Back
                    </button>
                </div>

            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jspf/bootstrapScripts.jspf" %>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>


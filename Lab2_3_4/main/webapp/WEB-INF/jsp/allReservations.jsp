<%@ taglib prefix="customtag" uri="http://localhost:8080/controller/customatag" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<fmt:message key="reservations.header" var="manage_reservations"/>
<fmt:message key="search.sort_by" var="sort_by"/>
<fmt:message key="search.show" var="show"/>
<fmt:message key="reservations.sort.reservation_date" var="reservation_date"/>
<fmt:message key="search.date.in" var="sort_date_in"/>
<fmt:message key="search.date.out" var="input_date_out"/>
<fmt:message key="search.status" var="sort_status"/>
<fmt:message key="orders.sort.asc" var="asc"/>
<fmt:message key="orders.sort.desc" var="desc"/>
<fmt:message key="search.number_of_places" var="input_number_of_places"/>
<fmt:message key="orders.table.actions" var="actions"/>
<fmt:message key="orders.table.user_id" var="user_id"/>
<fmt:message key="orders.table.id" var="id"/>
<fmt:message key="reservations.table.apartment_id" var="apartment_id"/>
<fmt:message key="reservations.processing_reservations" var="processing_reservations"/>
<fmt:message key="apartment.type" var="apartment_type"/>
<fmt:message key="order.status.canceled" var="canceled"/>
<fmt:message key="apartment.action.view" var="action_view"/>
<fmt:message key="action.confirm" var="confirm"/>
<fmt:message key="action.cancel" var="cancel"/>
<fmt:message key="pagination.previous" var="previous"/>
<fmt:message key="pagination.next" var="next"/>
<fmt:message key="reservations.status.processing" var="processing"/>
<fmt:message key="reservations.status.confirmed" var="confirmed"/>

<!DOCTYPE html>
<c:set var="title" value="Manage reservations"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body class="gradient-custom-for-page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<p class="text-center fs-1">${manage_reservations}</p>
<div class="container">
    <div class="d-flex justify-content-center mt-2">
        <div class="w-50">
            <form class="d-flex justify-content-center" action="${pageContext.request.contextPath}/controller">
                <div class="container px-1">
                </div>
            </form>
        </div>
    </div>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show mt-2 d-flex justify-content-between"
             role="alert">
            <div class="left">
                <strong>Sorry!</strong>
                    ${errorMessage}
            </div>
            <div class="right">
                <span type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </span>
            </div>
        </div>
    </c:if>
    <table class="table text-center align-content-center">
        <caption>${processing_reservations}</caption>
        <thead>
        <tr>
            <th scope="col">${id}</th>
            <c:if test="${userRole.name eq 'manager'}">
                <th scope="col">${user_id}</th>
            </c:if>
            <th scope="col">${apartment_id}</th>
            <th scope="col">${reservation_date}</th>
            <th scope="col">${sort_date_in}</th>
            <th scope="col">${input_date_out}</th>
            <th scope="col">${input_number_of_places}</th>
            <th scope="col">${sort_status}</th>
            <th scope="col">${actions}</th>
        </tr>
        </thead>
        <c:forEach var="item" items="${reservations}">
            <tr class="table-primary">
                <td>${item.id}</td>
                <c:if test="${userRole.name eq 'manager'}">
                    <td>${item.userId}</td>
                </c:if>
                <td>${item.apartmentId}</td>
                <td>
                    <customtag:formatDateTag value="${item.reservationDate}" pattern="dd-MM-yyyy hh:mm:ss"
                                             locale="${locale}"/>
                </td>
                <td>${item.dateIn}</td>
                <td>${item.dateOut}</td>
                <td>${item.personCount}</td>
                <td>
                    <c:choose>
                        <c:when test="${item.status.name eq 'processing'}">
                            ${processing}
                        </c:when>
                        <c:when test="${item.status.name eq 'canceled'}">
                            ${canceled}
                        </c:when>
                        <c:when test="${item.status.name eq 'confirmed'}">
                            ${confirmed}
                        </c:when>
                    </c:choose>
                </td>
                <td>
                    <div class="container">
                        <div class="row">
                            <c:if test="${not item.status.name eq 'canceled'}">
                                <div class="col">
                                    <form action="${pageContext.request.contextPath}/controller"
                                          method="post">
                                        <input type="hidden" name="command" value="cancel_reservation">
                                        <input type="hidden" name="user_id" value="${item.userId}">
                                        <input type="hidden" name="reservation_id" value="${item.id}">
                                        <button type="submit" class="btn btn-outline-danger">
                                                ${cancel}
                                        </button>
                                    </form>
                                </div>
                            </c:if>
                            <div class="col">
                                <form action="${pageContext.request.contextPath}/controller">
                                    <input type="hidden" name="command" value="get_apartment_page">
                                    <input type="hidden" name="apartment_id" value="${item.apartmentId}">
                                    <input type="hidden" name="date_in" value="${item.dateIn}">
                                    <input type="hidden" name="date_out" value="${item.dateOut}">
                                    <input type="hidden" name="place_count" value="${item.personCount}">
                                    <div class="btn-group">
                                        <button class="btn btn-outline-secondary">${action_view}</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>
        </c:forEach>
    </table>
    <nav class="row-cols-md-4 d-flex justify-content-center">
        <ul class="pagination justify-content-center">
            <c:choose>
                <c:when test="${curr_page eq 1}">
                    <li class="page-item disabled">
                        <a class="page-link" href="#" tabindex="-1" aria-disabled="true">${previous}</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/controller?command=get_all_reservations_page&curr_page=${curr_page-1}&records_on_page=${records_on_page}"
                           tabindex="-1" aria-disabled="false">${previous}</a>
                    </li>
                </c:otherwise>
            </c:choose>
            <c:forEach begin="1" end="${pages_count}" var="i">
                <c:choose>
                    <c:when test="${curr_page eq i}">
                        <li class="page-item active" aria-current="page">
                            <a class="page-link" href="#">${i}</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/controller?command=get_all_reservations_page&curr_page=${i}&records_on_page=${records_on_page}">${i}</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:choose>
                <c:when test="${curr_page eq pages_count}">
                    <li class="page-item disabled">
                        <a class="page-link" href="#" tabindex="-1" aria-disabled="true">${next}</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/controller?command=get_all_reservations_page&curr_page=${curr_page+1}&records_on_page=${records_on_page}"
                           tabindex="-1" aria-disabled="false">${next}</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
</div>
<%@ include file="/WEB-INF/jspf/bootstrapScripts.jspf" %>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
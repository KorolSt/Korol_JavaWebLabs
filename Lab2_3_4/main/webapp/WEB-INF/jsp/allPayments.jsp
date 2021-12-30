<%@ taglib prefix="customtag" uri="http://localhost:8080/controller/customatag" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<!DOCTYPE html>
<c:set var="title" value="Manage payments"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body class="gradient-custom-for-page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<fmt:message key="apartment.amount.currency" var="currency"/>
<fmt:message key="payments.header" var="manage_payments"/>
<fmt:message key="payments.sort_by.expire_date" var="sort_expire_date"/>
<fmt:message key="search.show" var="show"/>
<fmt:message key="search.sort_by" var="sort_by"/>
<fmt:message key="search.status" var="sort_status"/>
<fmt:message key="orders.sort.asc" var="asc"/>
<fmt:message key="orders.sort.desc" var="desc"/>
<fmt:message key="payments.processing_payments" var="processing_payments"/>
<fmt:message key="payments.table.reservation_id" var="reservation_id"/>
<fmt:message key="orders.table.id" var="id"/>
<fmt:message key="orders.table.actions" var="actions"/>
<fmt:message key="payments.table.amount" var="amount"/>
<fmt:message key="payments.action.pay" var="pay"/>
<fmt:message key="action.cancel" var="cancel"/>
<fmt:message key="pagination.previous" var="previous"/>
<fmt:message key="pagination.next" var="next"/>
<fmt:message key="order.status.canceled" var="canceled"/>
<fmt:message key="payments.status.failed" var="failed"/>
<fmt:message key="payments.status.paid" var="paid"/>
<fmt:message key="payments.status.waiting" var="waiting"/>
<fmt:message key="payments.update" var="update_payments"/>

<p class="text-center fs-1">${manage_payments}</p>
<div class="container">
    <div class="d-flex justify-content-center mt-2">
        <div class="w-50">
            <form class="d-flex justify-content-center" action="${pageContext.request.contextPath}/controller">
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
        <caption>${processing_payments}</caption>
        <thead>
        <tr>
            <th scope="col">${id}</th>
            <th scope="col">${reservation_id}</th>
            <th scope="col">${sort_expire_date}</th>
            <th scope="col">${amount}</th>
            <th scope="col">${sort_status}</th>
            <th scope="col">${actions}</th>
        </tr>
        </thead>
        <c:forEach var="item" items="${payments}">
            <tr class="table-primary">
                <td>${item.id}</td>
                <td>${item.reservationId}</td>
                <td>
                    <customtag:formatDateTag value="${item.expireDate}" pattern="dd-MM-yyyy hh:mm:ss" locale="${locale}"/>
                </td>
                <td>
                    <c:if test="${locale eq 'uk'}">
                        <c:set var="amount" scope="page" value="${item.amount}"/>
                    </c:if>
                    <c:if test="${locale eq 'en'}">
                        <c:set var="amount" scope="page" value="${item.amount / 30}"/>
                    </c:if>
                    <strong class="text-success">
                        <fmt:formatNumber value="${amount}" type="currency"
                                          currencySymbol="${currency}"
                                          pattern="#,##0.00 ¤;-#,##0.00 ¤"/>
                    </strong>
                </td>
                <td>
                    <c:if test="${item.status.name eq 'paid'}">
                        <div class="col">
                            <p class="fs-6">${paid}</p>
                        </div>
                    </c:if>
                    <c:if test="${item.status.name eq 'canceled'}">
                        <div class="col">
                            <p class="fs-6">${canceled}</p>
                        </div>
                    </c:if>
                    <c:if test="${item.status.name eq 'failed'}">
                        <div class="col">
                            <p class="fs-6">${failed}</p>
                        </div>
                    </c:if>
                    <c:if test="${item.status.name eq 'waiting'}">
                        <div class="col">
                            <p class="fs-6">${waiting}</p>
                        </div>
                    </c:if>
                </td>
                <td>
                    <div class="container">
                        <div class="row">
                            <c:choose>
                                <c:when test="${userRole.name eq 'manager'}">
                                    <c:if test="${item.status.name eq 'waiting'}">
                                        <div class="col">
                                            <form action="${pageContext.request.contextPath}/controller"
                                                  method="post">
                                                <input type="hidden" name="command" value="cancel_payment">
                                                <input type="hidden" name="payment_id" value="${item.id}">
                                                <button type="submit" class="btn btn-outline-danger">
                                                    ${cancel}
                                                </button>
                                            </form>
                                        </div>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${item.status.name eq 'waiting'}">
                                        <div class="col">
                                            <form action="${pageContext.request.contextPath}/controller"
                                                  method="post">
                                                <input type="hidden" name="command" value="cancel_payment">
                                                <input type="hidden" name="payment_id" value="${item.id}">
                                                <button type="submit" class="btn btn-outline-danger">
                                                        ${cancel}
                                                </button>
                                            </form>
                                        </div>

                                        <div class="col">
                                            <form action="${pageContext.request.contextPath}/controller"
                                                  method="post">
                                                <input type="hidden" name="command" value="pay">
                                                <input type="hidden" name="payment_id" value="${item.id}">
                                                <button type="submit" class="btn btn-outline-success">
                                                        ${pay}
                                                </button>
                                            </form>
                                        </div>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
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
                           href="${pageContext.request.contextPath}/controller?command=get_all_payments_page&curr_page=${curr_page-1}&records_on_page=${records_on_page}"
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
                        <li class="page-item"><a class="page-link"
                                                 href="${pageContext.request.contextPath}/controller?command=get_all_payments_page&curr_page=${i}&records_on_page=${records_on_page}">${i}</a>
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
                           href="${pageContext.request.contextPath}/controller?command=get_all_payments_page&curr_page=${curr_page+1}&records_on_page=${records_on_page}"
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
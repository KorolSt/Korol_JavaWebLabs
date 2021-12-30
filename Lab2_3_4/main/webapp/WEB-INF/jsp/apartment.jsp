<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>


<fmt:message key="apartment.amount.currency" var="currency"/>
<fmt:message key="apartment.places" var="apartment_places"/>
<fmt:message key="apartment.type" var="apartment_type"/>
<fmt:message key="apartment.name" var="apartment_name"/>
<fmt:message key="button.back" var="back"/>
<fmt:message key="apartment.reserve" var="reserve"/>
<fmt:message key="apartment.login_to_reserve" var="login_to_reserve"/>
<fmt:message key="search.number_of_places" var="input_number_of_places"/>
<fmt:message key="search.date.in" var="input_date_in"/>
<fmt:message key="search.date.out" var="input_date_out"/>
<fmt:message key="apartment.check_price" var="check_price"/>
<fmt:message key="apartment.text_price" var="text_price"/>
<fmt:message key="message.error" var="error"/>
<fmt:message key="message.invalid_input" var="invalid_input"/>


<!DOCTYPE html>
<c:set var="title" value="Apartment"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body class="gradient-custom-for-page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="container">
    <div class="row mt-xxl-5">
        <div class="info col-md-4">
            <p class="fs-2 fw-bold">${apartment.name}</p>
            <p class="fw-normal"><strong>${apartment_places}</strong>: ${apartment.placeCount}</p>
            <p class="fw-normal"><strong>${apartment_type}</strong>: ${apartment.type}</p>
            <div class="row mt-2">
                <form action="${pageContext.request.contextPath}/controller" method="get">
                    <input type="hidden" name="apartment_id" value="${apartment.id}">
                    <input type="hidden" name="place_count" value="${apartment.placeCount}">
                    <div class="col d-flex">
                        <input type="hidden" name="command" value="get_apartment_page">
                        <div class="row">
                            <c:if test="${not empty price}">
                                <div class="row mt-2">
                                    <div class="col text-center">
                                        <p class="fs-5">${text_price}:</p>
                                    </div>
                                    <div class="col text-center">
                                        <c:if test="${locale eq 'uk'}">
                                            <c:set var="amount" scope="page" value="${price}"/>
                                        </c:if>
                                        <c:if test="${locale eq 'en'}">
                                            <c:set var="amount" scope="page" value="${price / 30}"/>
                                        </c:if>
                                        <p class="fs-5"><strong><fmt:formatNumber value="${amount}" type="currency"
                                                                                  currencySymbol="${currency}"
                                                                                  pattern="#,##0.00 ¤;-#,##0.00 ¤"/></strong>
                                        </p>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div class="order col-md-4">
            <c:choose>
                <c:when test="${userRole.name == 'client'}">
                    <form action="${pageContext.request.contextPath}/controller" method="get">
                        <input type="hidden" name="command" value="reserve">
                        <input type="hidden" name="type" value="no_order">
                        <input type="hidden" name="apartment_id" value="${apartment.id}">

                        <div class="row">
                            <div class="col text-center">
                                <label for="date_in" class="form-label">${input_date_in}</label>
                                <input type="date" min="${date_now}" max="2022-12-31" class="form-control" id="date_in"
                                       name="date_in"
                                <c:choose>
                                <c:when test="${not empty date_in}">
                                       value="${date_in}"
                                </c:when>
                                <c:otherwise>
                                       value="Mark"
                                </c:otherwise>
                                </c:choose> required>
                            </div>
                            <div class="col text-center">
                                <label for="date_out" class="form-label">${input_date_out}</label>
                                <input type="date" min="${date_tomorrow}" class="form-control" id="date_out"
                                       name="date_out"
                                <c:choose>
                                <c:when test="${not empty date_out}">
                                       value="${date_out}"
                                </c:when>
                                <c:otherwise>
                                       value="Otto"
                                </c:otherwise>
                                </c:choose>
                                       required>
                            </div>
                        </div>
                        <div class="row mt-2">
                            <div class="col col text-center">
                                <label for="place_count" class="form-label">${input_number_of_places}</label>
                                <input type="number" class="form-control" min="1" max="${apartment.placeCount}"
                                       name="place_count" id="place_count"
                                <c:choose>
                                <c:when test="${not empty place_count}">
                                       value="${place_count}"
                                </c:when>
                                <c:otherwise>
                                       value="1"
                                </c:otherwise>
                                </c:choose>
                                       required>
                            </div>
                        </div>
                        <div class="row mt-2">
                            <div class="col d-flex">
                                <button style="width: 100%" class="btn btn-lg btn-outline-secondary">${reserve}</button>
                            </div>
                        </div>
                    </form>
                </c:when>
                <c:otherwise>
                    <p class="text-center fs-3 fw-bold">${login_to_reserve}</p>
                </c:otherwise>
            </c:choose>
            <hr>

            <div class="row mt-2">
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show mt-2 d-flex justify-content-between"
                         role="alert">
                        <div class="left">
                            <strong>Sorry!</strong>
                            <c:if test="${errorMessage eq 'invalid_input'}">
                                ${invalid_input}
                            </c:if>
                            <c:if test="${errorMessage eq 'error'}">
                                ${error}
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
        </div>
    </div>
    <div class="container text-center mt-xxl-5">
        <button class="btn btn-dark" style="width: 50%" type="button" name="back"
                onclick="history.back()">
            ${back}
        </button>
    </div>
</div>
<%@ include file="/WEB-INF/jspf/bootstrapScripts.jspf" %>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>


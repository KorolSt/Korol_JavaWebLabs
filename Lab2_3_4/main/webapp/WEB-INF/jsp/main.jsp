<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<!DOCTYPE html>
<c:set var="title" value="Home"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body class="gradient-custom-for-page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<fmt:message key="apartment.types.all" var="type_all"/>
<fmt:message key="apartment.action.view" var="action_view"/>
<fmt:message key="search.sort_by" var="input_sort_by"/>
<fmt:message key="search.sort_by_price" var="label_sort_by_price"/>
<fmt:message key="search.sort_by_place_number" var="label_sort_by_place_number"/>
<fmt:message key="search.sort_by_place_type" var="label_sort_by_type"/>
<fmt:message key="search.sort_by_status" var="label_sort_by_status"/>
<fmt:message key="search.status.free" var="free"/>
<fmt:message key="search.status.reserved" var="reserved"/>
<fmt:message key="search.status.busy" var="busy"/>
<fmt:message key="search.status.not_available" var="not_available"/>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<nav class="navbar navbar-expand-lg navbar-light">
    <div class="container-fluid">
        <div class="collapse navbar-collapse justify-content-center" id="navbarNavAltMarkup">
            <div class="navbar-nav">
                <a class="nav-link px-4"
                   href="${pageContext.request.contextPath}/controller?command=get_main_page&apartment_type=all">${type_all}</a>
                <a class="nav-link px-4"
                   href="${pageContext.request.contextPath}/controller?command=get_main_page&apartment_type=single">Single</a>
                <a class="nav-link px-4"
                   href="${pageContext.request.contextPath}/controller?command=get_main_page&apartment_type=double">Double</a>
                <a class="nav-link px-4"
                   href="${pageContext.request.contextPath}/controller?command=get_main_page&apartment_type=quad">Quad</a>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <p class="fs-6 mb-2">${input_sort_by}</p>
        <div class="btn-group" role="group" aria-label="Basic radio toggle button group">
            <input name="sort_by" value="apartments_by_price_asc" type="checkbox"
                   class="btn-check" id="by_price"
                   <c:if test="${sort_by_price}">checked</c:if>>
            <label class="btn btn-outline-dark" for="by_price">${label_sort_by_price}</label>

            <input name="sort_by" value="apartments_by_place_number_asc" type="checkbox"
                   class="btn-check" id="by_place_number"
                   <c:if test="${sort_by_place_number}">checked</c:if>>
            <label class="btn btn-outline-dark" for="by_place_number">${label_sort_by_place_number}</label>

            <input name="sort_by" value="apartments_by_apartment_type_asc" type="checkbox"
                   class="btn-check" id="by_apartment_type"
                   <c:if test="${sort_by_type}">checked</c:if>>
            <label class="btn btn-outline-dark" for="by_apartment_type">${label_sort_by_type}</label>

            <input name="sort_by" value="apartments_by_status_asc" type="checkbox"
                   class="btn-check" id="by_status"
                   <c:if test="${sort_by_status}">checked</c:if>>
            <label class="btn btn-outline-dark" for="by_status">${label_sort_by_status}</label>
        </div>
        <p class="fs-6 mb-2">${checkbox_status}</p>
        <div class="btn-group" role="group" aria-label="Basic radio toggle button group">

            <input name="status" value="free" type="checkbox" class="btn-check" id="status_free"
                   <c:if test="${status_free}">checked</c:if>>
            <label class="btn btn-outline-dark" for="status_free">${free}</label>

            <input name="status" value="reserved" type="checkbox" class="btn-check" id="btncheck2"
                   <c:if test="${status_reserved}">checked</c:if>>
            <label class="btn btn-outline-dark" for="btncheck2">${reserved}</label>

            <input name="status" value="busy" type="checkbox" class="btn-check" id="btncheck3"
                   <c:if test="${status_busy}">checked</c:if>>
            <label class="btn btn-outline-dark" for="btncheck3">${busy}</label>

            <input name="status" value="not_available" type="checkbox" class="btn-check" id="btncheck4"
                   <c:if test="${status_not_available}">checked</c:if>>
            <label class="btn btn-outline-dark" for="btncheck4">${not_available}</label>
        </div>
    </div>
</nav>
<div class="gradient-custom-for-hr">
    <hr style="margin: 0">
</div>
<div class="container w-25">
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
</div>
<c:choose>
    <c:when test="${fn:length(apartments) == 0}"><p class="text-center fs-1 fw-light ">No available
        apartments</p></c:when>
    <c:otherwise>
        <div class="album py-5">
            <div class="container">
                <div class="row">
                    <c:forEach var="item" items="${apartments}">
                        <div class="col-md-4">
                            <div class="card mb-4 box-shadow">
                                <div class="card-body">
                                    <p class="card-text">${item.name}</p>
                                    <p class="card-text">${item.type}</p>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <form action="${pageContext.request.contextPath}/controller">
                                            <input type="hidden" name="command" value="get_apartment_page">
                                            <input type="hidden" name="apartment_id" value="${item.id}">
                                            <input type="hidden" name="date_in" value="${date_in}">
                                            <input type="hidden" name="date_out" value="${date_out}">
                                            <input type="hidden" name="place_count" value="1">
                                            <div class="btn-group">
                                                <button class="btn btn-sm btn-outline-secondary">${action_view}</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>
<%@ include file="/WEB-INF/jspf/bootstrapScripts.jspf" %>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
<script type="text/javascript">

    $("#by_place_number").click(function () {
        if ($(this).prop('checked')) {
            $(this).val("apartments_by_place_number_asc");
        } else {
            $(this).val("apartments_by_place_number_desc");
        }
    });

    $("#by_price").click(function () {
        if ($(this).prop('checked')) {
            $(this).val("apartments_by_price_asc");
        } else {
            $(this).val("apartments_by_price_desc");
        }
    });

    $("#by_status").click(function () {
        if ($(this).prop('checked')) {
            $(this).val("apartments_by_status_asc");
        } else {
            $(this).val("apartments_by_status_desc");
        }
    });

    $("#by_apartment_type").click(function () {
        if ($(this).prop('checked')) {
            $(this).val("apartments_by_apartment_type_asc");
        } else {
            $(this).val("apartments_by_apartment_type_desc");
        }
    });

</script>


</body>

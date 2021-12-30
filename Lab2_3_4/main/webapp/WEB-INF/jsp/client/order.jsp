<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<fmt:message key="order.form.description" var="form_description"/>
<fmt:message key="order.form" var="form"/>
<fmt:message key="search.date.in" var="input_date_in"/>
<fmt:message key="search.date.out" var="input_date_out"/>
<fmt:message key="apartment.type" var="apartment_type"/>
<fmt:message key="search.number_of_places" var="input_number_of_places"/>
<fmt:message key="order.form.make" var="make_order"/>

<!DOCTYPE html>
<c:set var="title" value="Do order"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body class="gradient-custom-for-page">

<%@ include file="/WEB-INF/jspf/header.jspf" %>
<div class="container">
    <main>
        <div class="py-5 text-center">
            <h2>${form}</h2>
            <p class="lead">
                ${form_description}
            </p>
        </div>

        <div class="container d-flex justify-content-center">
            <div class="col-md-7 col-lg-8">
                <form class="needs-validation" novalidate="" action="${pageContext.request.contextPath}/controller?command=reserve">
                    <input type="hidden" name="command" value="order">

                    <div class="row g-3 text-center">
                        <div class="col-sm-3">
                            <label for="date_in" class="form-label">${input_date_in}</label>
                            <input type="date" min="${date_now}" max="2022-12-31" class="form-control" id="date_in"
                                   name="date_in"
                                   value="Mark" required>
                        </div>
                        <div class="col-sm-3">
                            <label for="date_out" class="form-label">${input_date_out}</label>
                            <input type="date" min="${date_tomorrow}" class="form-control" id="date_out" name="date_out" value="Otto" required>
                        </div>

                        <div class="col-sm-3">
                            <label for="select-apartment-type" class="form-label">${apartment_type}</label>
                            <select class="form-select" aria-label="Default select example" name="apartment_type"
                                    id="select-apartment-type">
                                <option value="single" selected>Single</option>
                                <option value="double">Double</option>
                                <option value="quad">Quad</option>
                                <option value="queen">Queen</option>
                                <option value="king">King</option>
                                <option value="twin">Twin</option>
                                <option value="double_double">Double-double</option>
                                <option value="studio">Studio</option>
                                <option value="master_suite">Master-suite</option>
                                <option value="junior_suite">Junior-suite</option>
                            </select>
                        </div>
                        <div class="col-sm-3">
                            <label for="place_count" class="form-label">${input_number_of_places}</label>
                            <input type="number" class="form-control" min="0" max="10" name="place_count" id="place_count" value="1"
                                   required>
                        </div>
                    </div>

                    <hr class="my-4">

                    <button class="w-100 btn btn-primary btn-lg" type="submit">${make_order}</button>
                </form>
            </div>
        </div>
    </main>
</div>

<%@ include file="/WEB-INF/jspf/bootstrapScripts.jspf" %>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>




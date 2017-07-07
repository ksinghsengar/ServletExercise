<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link  rel="stylesheet" href="Style.css">
</head>
<body>
<div class="well  col-md-12">
    <span class="col-md-3">To The Bloggers'</span>
    <span class=" col-md-offset-3 col-md-1"><a href="#home">Home</a></span>
    <span class="col-md-1"><a href="#about">About</a></span>

            <span class="col-md-2">Hi <%= session.getAttribute("user")%></span>
            <span><button class="button btn-default logout col-md-1" onclick="location.href='/Logout'">Logout</button></span>

    </div>
<div style="font-size:medium; color:white; text-align: center; " class="col-md-12">${message}</div>
    <form method="post" class="blog_form col-md-offset-1 col-md-3" action="Blog" >
        <p> Add Blog </p>
        <p style="color:white;">Hi <%= session.getAttribute("user")%> </p><br/><br/>
        <%--<% request.setAttribute("previous_page","blog.jsp");%>--%>
        <textarea name="blog_content" id="" cols="30" rows="50" placeholder="Add Blog Content here." required></textarea>
        <button type="submit"  class = "button btn-default col-md-5 ">Save</button>
</form>
<%List<String> list = (ArrayList<String>) session.getAttribute("content");
//    System.out.println(list.size());
 for (String content :list ) {%>

<div class="col-md-offset-1 col-md-6">
    <!-- blog1-->
    <div class="panel panel-default">
        <div class="panel-heading">
            Recent shares
        </div>

        <div class="panel-body">

            <div class="col-md-2">
                <span class="glyphicon glyphicon-user"></span>
            </div>

            <div class="col-md-10">
                <b class=""><%= session.getAttribute("user")%></b>
                <span class="text-muted">&commat;<%= session.getAttribute("user")%></span>
                <p> <%= content%></p>
                <a class="fa fa-facebook-square"></a>
                <a class="fa fa-tumblr"></a>
                <a class="fa fa-google-plus"></a>
                <a href=# class="view_post"> view post</a>
            </div>


        </div>
    </div>
</div>
<%
    }
%>

</body>
</html>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="include.inc.jsp"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>线上线下服务平台</title>
    <meta name="renderer" content="webkit" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${ctx}static/css/maroco.css">
    <script src="${ctx}static/maroco.js"></script>
    <style>
        body {
            background: #f0f0f0;
            color: #666
        }
        .brand{
            padding:30px;
            height: 88px;
            text-align: center;
        }
        #loginForm {
            margin: 0 auto 50px;
            width:100%;
            background-color: #fff;
            border-radius: 4px;
            box-shadow: 0 1px 0 rgba(0, 0, 0, .05);
        }
        @media (min-width: 768px) {
            #loginForm {
                width: 700px;
            }
        }
        .form-part {
            width: 300px;
            padding: 55px 0 30px;
            margin: 0 auto;
        }

        #loginForm .footer {
            background: #fcfcfc;
            border-top: 1px solid #eeeff2;
            color: #777;
            padding: 15px 0;
            border-radius: 0 0 4px 4px;
        }

        #loginForm .footer a {
            color: #999;
        }

        #loginForm .footer a:hover {
            color: #666;
        }
        .form-line {
            margin-bottom: 15px;
            clear: both;
        }

        .form-line i {
            position: absolute;
            margin: 9px;
            color: #b0b0b0;
        }

        .form-line .text {
            width: 300px;
            padding-left: 33px;
            border-color: #ebebeb;
            background: #fff;
            font-size: 14px;
            *width: 202px;
        }

        .form-line .text:focus {
            border-color: #74c5f2;
        }

        .form-line .verifycode {
            width: 100px;
            padding-left: 8px;
        }
    </style>
</head>

<body>
<!-- login -->
<div class="brand"><img class="logo" src="${ctx}static/cloud/img/logo.png"></div>
<div id="loginForm" class="w12 tabs">
    <form class="form-part ac-fake" autocomplete="on" action="${pageContext.request.contextPath }/loginUser" method="post">
        <div class="form-line">
            <i class="f f-user"></i>
            <input tabindex="2" class="text ac-userName" placeholder="用户名" name="username" required>
        </div>
        <div class="form-line">
            <i class="f f-lock"></i>
            <input tabindex="3" class="text" type="password" placeholder="密码" value="" autocomplete="off" name="password" required>
        </div>
        <div class="form-line">
        </div>
        <div class="form-line p1 clear">
            <button tabindex="5" type="submit" class="b note w10 xl">登录</button>
        </div>
        <p class="c-error" id="errorLabel"></p>
    </form>
    <!-- footer -->
    <div class="row footer text-center">
        <div class="x2">
            <a href="#"><i class="mr f f-left"></i>登录有问题？请咨询我们的客服专员</a>
        </div>
        <div class="x2">
            <%--<a href="${ctx}regist">快速注册<i class="f f-right"></i></a>--%>

        </div>
    </div>
</div>
<div class="text-center">
</div>
</body>

</html>

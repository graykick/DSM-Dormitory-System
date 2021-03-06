<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>DMS</title>
    <link rel="stylesheet" href="/css/default.css">
    <script src='https://www.google.com/recaptcha/api.js'></script>
  </head>
  <body>
    <div id="card">
      <div id="title">로그인</div>
      <form action="/user/login" method="POST"  id="login_form">
        <input type="text" name="id" id="login_id" placeholder="ID">
        <input type="password" name="password" id="login_pw" placeholder="PW">
        <div id="auto_login"><input type="checkbox" name="autoLogin" id="autoLogin">자동 로그인</div>
        <div id="recaptcha" class="g-recaptcha" data-sitekey="6LdPphUUAAAAALXbBtZQa5Jtau60XENpmPX8YG9-"></div>
        <div id="login">LOGIN</div>
      </form>
    </div>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="/js/remote.js"></script>
  </body>
</html>

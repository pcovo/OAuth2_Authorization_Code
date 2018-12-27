<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>POC OAuth2 - Authorization Code</title>
</head>
<body>
	<h3 style="color: green;">Token</h3>

	<div>
		<form action="http://localhost:8080/oauth/authorize"
			method="post" modelAttribute="emp">
			<p>
				<label>Solicitar Token ao Authorization Server</label>
				 <input type="hidden" name="response_type" value="code" /> 
				 <input type="hidden" name="client_id" value="client-id" />
				 <input type="hidden" name="redirect_uri" value="http://localhost:8090/setToken" />
				 <input type="hidden" name="scope" value="read" /> 
				 <input type="hidden" name="state" value="123456" /> 
				 <input type="SUBMIT" value="Solicitar Token" />
		</form>
	</div>
</body>
</html>

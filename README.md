Application properties example:

create file application.properties with contains

server.port=79
#set unused port

ngrok.enabled=true
ngrok.authToken=(set your ngrok token)

app.ngrok.http.ports=80,81
app.ngrok.tcp.ports=5432

Summary no more than 3 ports all types

Replace application.properties file in resource or any folder in system and add parameter in service for start:

--spring.config.name=(config name without ".properties") --spring.config.location=file:(location file)
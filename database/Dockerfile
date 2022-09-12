FROM mcr.microsoft.com/mssql/server:2022-latest

COPY ./schema.sql ./data.sql ./

ENV ACCEPT_EULA Y
ENV SA_PASSWORD Passw0rd!
ENV SQLCMDPASSWORD Passw0rd!
EXPOSE 1433

RUN /opt/mssql/bin/sqlservr >> /dev/null & \
    sleep 15 \
    && /opt/mssql-tools/bin/sqlcmd -U SA -i schema.sql \
    && pkill sqlservr

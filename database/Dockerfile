FROM mcr.microsoft.com/mssql/server:2022-latest

COPY ./schema.sql ./data.sql ./entrypoint.sh ./

ENV ACCEPT_EULA Y

EXPOSE 1433

CMD ["bash", "./entrypoint.sh"]

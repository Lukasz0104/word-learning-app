#!/bin/bash

# Enable job control
set -m;

# Check if DB_PASSWORD is set
if [ -z ${DB_PASSWORD+x} ]; then
	echo "DB_PASSWORD is not set. Exiting.";
	exit -3;
fi

export MSSQL_SA_PASSWORD=$DB_PASSWORD;

# Start SQL Server in the background
/opt/mssql/bin/sqlservr &

# Store pid of SQL Server
SQLSERVER_PID=$!;

# Sleep
sleep 20;

# Execute schema.sql
/opt/mssql-tools/bin/sqlcmd -U SA -P $DB_PASSWORD -i schema.sql;

# Check exit status
if [[ $? -ne 0 ]]; then
	echo "Could not execute script schema.sql. Exiting.";
	exit -1;
fi

# Conditionally populate database with data
if [ "$POPULATE_DB" = true ]; then
	echo "Executing script data.sql...";
	/opt/mssql-tools/bin/sqlcmd -U SA -P $DB_PASSWORD -i data.sql;

	if [[ $? -ne 0 ]]; then
		echo "Could not execute script data.sql. Exiting.";
		exit -2;
	fi
fi;

# Trap SIGTERM and send to SQL Server
# Source: https://github.com/microsoft/mssql-docker/issues/11#issuecomment-725442078
trap "kill -15 $SQLSERVER_PID" SIGTERM;

wait $SQLSERVER_PID;

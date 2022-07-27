alter session set "_oracle_script"=true;
create user team2 identified by java;

grant connect, resource, unlimited tablespace to team2;

conn team2 / java;

show user;
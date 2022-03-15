drop table if exists accounts;
drop table if exists clients;

create table clients (
	id serial primary key,
	firstname varchar(20) not null,
	lastname varchar(20) not null
);

create table accounts (
	id serial primary key,
	balance int not null,
	clientid int not null references clients(id) on delete cascade on update cascade,
	type varchar(20) not null
);

insert into clients (firstname, lastname)
values 
('Peter', 'Pan'),
('Captain', 'Hook'),
('Mr', 'Shmee'),
('Tinker', 'Bell'),
('Rufio', 'NA');

insert into accounts (balance, clientid, type)
values 
(1000000, 1, 'CHECKING'),
(5000000, 2, 'SAVINGS'),
(1000000, 2, 'CHECKING'),
(100000, 3, 'CHECKING'),
(40000, 4, 'CHECKING'),
(20000000, 5, 'CHECKING');

select clients.firstname, clients.lastname, accounts.balance
from accounts 
inner join clients on accounts.clientid=clients.id;
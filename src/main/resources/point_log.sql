create table server_point_log
(
	id int auto_increment,
	uuid VARCHAR(36) not null,
	action VARCHAR(50) not null,
	point int not null,
	date datetime not null,
	constraint server_point_log_pk
		primary key (id)
);

create index server_point_log_uuid_index
	on server_point_log (uuid);

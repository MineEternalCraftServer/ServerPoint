create table server_point
(
	id int auto_increment,
	uuid VARCHAR(36) not null,
	point int default 0 null,
	date datetime not null,
	constraint server_point_pk
		primary key (id)
);

create index server_point_uuid_index
	on server_point (uuid);
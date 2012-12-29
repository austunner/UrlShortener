create table user(
	user_id bigint not null auto_increment primary key,
	ipaddr varchar(48) not null,
	useragent varchar(200)
);

create table url(
	url_id bigint not null AUTO_INCREMENT PRIMARY KEY,
	url varchar(300) not null unique,
        url_short varchar(100) not null unique,
	date_created date not null,
	user_id bigint not null,
	foreign key (user_id) references user(user_id)
);
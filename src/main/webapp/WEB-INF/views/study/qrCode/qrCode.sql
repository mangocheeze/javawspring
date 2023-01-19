show tables;

create table qrCode (
	/*idx int not null auto_increment primary key,*/
	idx char(8) not null,         /* UUID 앞에서 8글자로 지정*/
	qrCode varchar(200) not null,
	bigo varchar(200)							/*기타정보*/
);

/* 날짜_이메일_고유번호(8글자)*/

drop table qrCode;
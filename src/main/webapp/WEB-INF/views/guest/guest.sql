show tables;

create table guest2 (
	idx int not null auto_increment primary key,  /* 고유번호 (auto_increment로 지정한애는 primary key로 무조건줘야함)*/
	name varchar(20) not null,   		  /*방문자 성명*/
	email varchar(60),    					  /*이메일 주소 ( 이건 써도되고 안써도되는거니까 not null안함*/
	homePage varchar(60), 					  /*홈페이지 주소*/
	visitDate datetime default now(), /*방문일자*/
	hostIp varchar(50) not null,			/*방문자의 IP (욕쓰는거 미연에 방지, 추적)*/
	content text not null 					  /*방문소감*/
);
/*방문자는 이름하고 방문소감만 필수로 쓰면됨 IP는 방문자가쓰는게 아니라 들어오면 자동생성됨*/

desc guest2;

insert into guest2 value (default,'관리자','asdf990515@naver.com','https://blog.naver.com/asdf990515',default,'192.168.50.210','방명록서비스를 개시합니다.');

select * from guest2;

show tables;

create table schedule2(
	idx	  int not null auto_increment primary key,
	mid	  varchar(20) not null,
	sDate datetime not null,			/* 일정 등록한 날짜*/
	part  varchar(20) not null,		/* 1.모임, 2.업무, 3.학습, 4.여행 0:기타 */
	content text not null					/* 일정 상세 내역*/
);

desc schedule2;

insert into schedule2 values (default, 'hkd1234', '2023-01-13', '모임', '가족회의, 장소:집, 시간:19시');
insert into schedule2 values (default, 'hkd1234', '2023-01-13', '모임', '초등 동창회, 장소:충북대사거리, 시간:13시');
insert into schedule2 values (default, 'hkd1234', '2023-01-13', '학습', 'jsp복습');
insert into schedule2 values (default, 'hkd1234', '2023-01-15', '업무', '프로젝트 회의');
insert into schedule2 values (default, 'hkd1234', '2023-01-21', '학습', '스프링공부');
insert into schedule2 values (default, 'hkd1234', '2023-01-01', '여행', '신년여행');
insert into schedule2 values (default, 'hkd1234', '2023-01-18', '기타', '온라인 교육서제출');
insert into schedule2 values (default, 'hkd1234', '2023-01-22', '모임', '독서모임');
insert into schedule2 values (default, 'hkd1234', '2023-01-29', '업무', '프로젝트 시작');
insert into schedule2 values (default, 'hkd1234', '2023-02-15', '업무', '프로젝트 중간');
insert into schedule2 values (default, 'hkd1234', '2023-02-19', '업무', '프로젝트 끝');
insert into schedule2 values (default, 'aaa1234', '2023-01-17', '기타', '집안 청소');
insert into schedule2 values (default, 'aaa1234', '2023-01-21', '학습', '자바공부');
insert into schedule2 values (default, 'aaa1234', '2023-01-19', '여행', '대천여행');

select * from schedule2 where mid='hkd1234' and sDate = '2023-1' order by sDate;  /* 자료안나옴 x*/
select * from schedule2 where mid='hkd1234' and sDate = '2023-01' order by sDate; /* 자료안나옴 x*/
select * from schedule2 where mid='hkd1234' and sDate = '2023-01-13' order by sDate; /* 자료나옴 o */
select * from schedule2 where mid='hkd1234' and substring(sDate,1,7) = '2023-01' order by sDate; /* 자료나옴 o - sDate를 문자로 변환해줌 ,DB에서만 시작위치가 0이아니라 1임*/
select * from schedule2 where mid='hkd1234' and date_format(sDate,'%Y-%m') = '2023-01' order by sDate; /*자료나옴 o - 연도 4자리와 월에해당하는거와 2023-01비교*/
select * from schedule2 where mid='hkd1234' and date_format(sDate,'%Y-%m') = '2023-01' group by substring(sDate,1,7) order by sDate; /* 같은날짜를 묶어서*/
select sDate,count(*) from schedule2 where mid='hkd1234' and date_format(sDate,'%Y-%m') = '2023-01' group by substring(sDate,1,7) order by sDate; /* sDate와 갯수가져옴*/
select sDate,part from schedule2 where mid='hkd1234' and date_format(sDate,'%Y-%m') = '2023-01' order by sDate,part; /* sDate와 part가져옴*/


select * from schedule2 where mid='hkd1234' and date_format(sDate,'%Y-%m-%d') = '2023-01-13';
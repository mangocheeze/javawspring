show tables;

create table webMessage (
	idx     int not null auto_increment primary key,   /* 고유번호 */
	title   varchar(100) not null,    	/* 메세지 제목 */
	content text not null,						  /* 메세지 내용 */
	sendId  varchar(20) not null,       /* 보내는 사람 아이디 */
	sendSw  char(1)  not null,					/* 스위치. 보낸메세지(s), 휴지통(g)-생략(나중에해보기), 휴지통에서 삭제(x표시)=>  (보낸메세지는 휴지통으로안가고 바로삭제시킬거임) */
	sendDate datetime default now(),		/* 메세지 보낸날짜 */
	/* --- 여기까지가 보내는사람의 테이블, 아랜받는사람의 테이블 --- */
	receiveId varchar(20) not null,			/* 받는 사람 아이디 */
	receiveSw char(1) not null,					/* 받은메세지(n), 읽은메세지(r), 휴지통(g), 휴지통에서삭제(x표시)-실제삭제가아니라 휴지통에 x표시를 낼거임 */
	receiveDate datetime default now()	/* 메세지 받은날짜*/
);

desc webMessage;

insert into webMessage values (default,'안녕! 말숙아~','말숙아 주말에 시간있니?','hkd1234','s',default,'kms1234','n',default);
update webMessage set receiveSw = 'r', receiveDate=now() where idx = 1; /* 읽을경우 날짜가 바뀜 */
insert into webMessage values (default,'반가워 길동아~','주말에는 프로젝트 계획이있어','kms1234','s',default,'hkd1234','n',default);
update webMessage set receiveSw = 'r', receiveDate=now() where idx = 2;
update webMessage set receiveSw = 'g' where idx = 2;
update webMessage set sendSw = 'x' where idx = 1; /* 1번 보낸메세지를 휴지통에서 삭제 -x표시 ,휴지통에서 안보여야함*/
update webMessage set receiveSw = 'g' where idx = 1; /* 1번 받은메세지를  휴지통에 넣음*/
update webMessage set receiveSw = 'x' where idx = 1;  /* 받은메세지를 휴지통에서 x표시 */
delete from webMessage where idx = 1;  /* 휴지통에서 x표시 되어있는걸 삭제*/
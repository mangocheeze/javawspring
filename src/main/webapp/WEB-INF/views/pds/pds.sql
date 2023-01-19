show tables;

create table pds2 (
	idx  		 int not null auto_increment, /* 자료실 고유번호 */
	mid  		 varchar(20) not null,        /* 올린이 아이디 -멤버에서 했던거랑 똑같이해줘야함*/
	nickName varchar(20) not null,				/* 올린이 닉네임 */
	fName    varchar(200) not null, 			/* 업로드시 파일명 */
	fSName   varchar(200) not null, 			/* 실제 파일서버에 저장되는 파일명 */
	fSize 	 int not null,								/* 파일의 총 사이즈-우리가 작업할건없음 */
	title    varchar(100) not null,				/* 업로드 파일의 제목 */
	part		 varchar(20)  not null, 			/* 파일 분류 */
	pwd			 varchar(100) not null,       /* 비밀번호 암호화(암호화 - SHA25암호화 처리)*/
	fDate	   datetime default now(), 			/* 파일 업로드 한 날짜*/
	downNum  int		 default 0, 					/* 파일 다운로드 한 횟수*/
	openSw   char(6)  default '공개',			/* 파일 공개(비공개)여부*/
	content text,													/* 업로드 파일의 상세설명 */
	hostIp	 varchar(50) not null, 				/* 업로드 PC IP*/
	primary key (idx)											/* 기본키: 자료실의 고유번호 */
);

desc pds2;

select * from pds2 order by idx desc;
show tables;

create table member2 (
	idx int not null auto_increment ,   /* 회원 고유번호 (idx 를 primary key 직접주지않은이유: 아이디가 중복되지않게할거니까 외래키로 사용될수있어서(외래키로하려면 primary key로되어있어야함) auto_increment로 되어있다는건 무조건 primarykey임, 그럼 primary key가 최소2개이상이된다는소린데 primary key 는 여러개와도됨*/
	mid varchar(20) not null,					  /* 회원 아이디(중복불허)*/
	pwd varchar(100) not null,				  /* 비밀번호(SHA암호화 처리) - 암호화 처리할때 varchar(20) 으로 길이 주면에러남: 길이가 너무짧게줘서, 애초에 크게주기 */
	nickName varchar(20) not null,		  /* 별명(필수입력-수정가능(닉네임은 바꿔도 이전글은 닉네임 원래그대로,글수정할때 비밀번호 확인처리해야함)/중복불허)*/
	name     varchar(20) not null,			/* 회원 성명*/
	gender   varchar(5) default '남자',  /* 성별 -필수입력은 아니지만 기본값주기*/
	birthday datetime   default now(),  /* 생일 */
	tel      varchar(15),								/* 전화번호(010-1234-5678) 선택사항,전화번호 최소자리 13 넉넉하게 15*/
	address  varchar(100),							/* 회원주소(상품 배달시 기본주소로 사용) */
	email    varchar(50) not null,      /* 이메일(아이디/비밀번호 분실시 사용) - 필수입력,형식체크필수 */
	homePage varchar(50) not null,      /* 홈페이지(블로그) 주소*/
	job      varchar(20),								/* 회원 직업*/
	hobby    varchar(100),								/* 회원 취미(2개이상은 '/'로 구분처리) */
	photo		 varchar(100) default 'noimage.jpg' , /*회원사진(아무것도 안넣으면 no image사진-기본값으로)*/
	content  text,											/* 회원 자기소개 */
	userInfor char(6)	default '공개',		/* 회원정보 공개여부 (공개/비공개) - 정해져있는 글자 처리하는건 char() */
	userDel  	char(2) default 'NO',     /* 회원 탈퇴 신청 여부(OK:탈퇴신청한 회원/ NO: 현재가입중인 회원) */
	point     int default 100,					/* 회원 누적포인트(가입시는 기본 100증정, 방문지마다 1회 10 포인트 증가, 최대 1일 50포인트까지 */
	level 		int default 4,						/* 회원 등급(0:관리자, 1: 운영자, 2:우수회원, 3: 정회원 4: 준회원 */
																			/*----따로 다른 테이블로 빼도됨----*/
	visitCnt  int default 0,     				/* 방문횟수*/
	startDate datetime default now(), 	/* 최초 가입일*/
	lastDate  datetime default now(), 	/* 마지막 접속일*/
	todayCnt int default 0,							/* 오늘 방문한 횟수 */
	
	primary key(idx,mid)   							/* 주키: idx(고유번호), mid(아이디) */
);

/*drop table member;*/
desc member2;
/*delete from member where idx =;*/
insert into member2 values (default,'admin','1234','관리맨','관리자',default,default,'010-4050-7759','충북 청주시 상당구 단재로 312','asdf990515@naver.com','https://blog.naver.com/asdf990515','학생','등산/바둑',default,'관리자입니다.',default,default,default,0,default,default,default,default);  /*기본데이터는 하나도빠짐없이 넣어야함*/

select * from member2;
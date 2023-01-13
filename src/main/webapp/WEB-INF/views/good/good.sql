create table good (
  idx     int not null auto_increment primary key,	/* 좋아요 고유번호 */
  part		varchar(20) not null,	/* 어떤 분야(테이블)? */
  partIdx int  not null,				/* 그 분야(테이블)의 고유번호 */
  mid			varchar(20) not null	/* 해당 분야의 해당 게시글에 접속한 사용자의 아이디 */
);

desc good;

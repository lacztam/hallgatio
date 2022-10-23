create table course_students_aud (rev int4 not null, course_id int4 not null, students_id int4 not null, revtype int2, primary key (rev, course_id, students_id));
create table course_teachers_aud (rev int4 not null, course_id int4 not null, teachers_id int4 not null, revtype int2, primary key (rev, course_id, teachers_id));
alter table if exists course_students_aud add constraint FK_course_students_aud_rev foreign key (rev) references revinfo;
alter table if exists course_teachers_aud add constraint FK_course_teachers_aud_rev foreign key (rev) references revinfo;

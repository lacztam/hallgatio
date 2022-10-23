create table course (id int4 not null, name varchar(255), primary key (id));
create table course_students (course_id int4 not null, students_id int4 not null);
create table course_teachers (course_id int4 not null, teachers_id int4 not null);
alter table if exists course_student add constraint FK_CS_to_student foreign key (student_id) references student;
alter table if exists course_student add constraint FK_CS_to_course foreign key (course_id) references course;
alter table if exists course_teacher add constraint FK_CT_to_teacher foreign key (teacher_id) references teacher;
alter table if exists course_teacher add constraint FK_CT_to_course foreign key (course_id) references course;

create table absence (id  bigserial not null, end_date date, location varchar(255), reason varchar(255), start_date date, maintainer_id int8, primary key (id));
create table certificate (id  bigserial not null, name varchar(255), primary key (id));
create table integration_request (id  bigserial not null, error_message varchar(255), request_destination varchar(255), response_code varchar(255), timestamp timestamp, primary key (id));
create table maintainer (id  bigserial not null, employee_id varchar(255), first_name varchar(255), last_name varchar(255), level varchar(255), rank varchar(255), first_shift_preference_id int8, second_shift_preference_id int8, section_id int8, shift_id int8, third_shift_preference_id int8, primary key (id));
create table maintainer_certificates (maintainer_id int8 not null, certificates_id int8 not null);
create table section (id  bigserial not null, name varchar(255), primary key (id));
create table shift (id  bigserial not null, name varchar(255), primary key (id));
alter table if exists absence add constraint FK_absence_maintainer foreign key (maintainer_id) references maintainer;
alter table if exists maintainer add constraint FK_maintainer_first_shift_preference foreign key (first_shift_preference_id) references shift;
alter table if exists maintainer add constraint FK_maintainer_second_shift_preference foreign key (second_shift_preference_id) references shift;
alter table if exists maintainer add constraint FK_maintainer_third_shift_preference foreign key (third_shift_preference_id) references shift;
alter table if exists maintainer add constraint FK_maintainer_section foreign key (section_id) references section;
alter table if exists maintainer add constraint FK_maintainer_shift foreign key (shift_id) references shift;
alter table if exists maintainer_certificates add constraint FK_maintainer_certificates_certificate foreign key (certificates_id) references certificate;
alter table if exists maintainer_certificates add constraint FK_maintainer_certificates_maintainer foreign key (maintainer_id) references maintainer;
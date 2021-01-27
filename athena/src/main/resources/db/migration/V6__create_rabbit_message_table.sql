create table rabbit_message (id  bigserial not null, origin_queue varchar(255), body varchar(255), created_at timestamp , primary key (id));

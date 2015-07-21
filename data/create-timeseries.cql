/*
Schema for storing raw stackoverflow posts data.
More can be read about these posts here: https://archive.org/details/stackexchange
*/

DROP KEYSPACE IF EXISTS sof_posts_data;
CREATE KEYSPACE sof_posts_data WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

use sof_posts_data;

/*
Posts that correspond with collected raw posts data.
*/
CREATE TABLE IF NOT EXISTS sof_post (
    id text ,             // Row ID
    post_type_id text,
    parent_id text,
    create_date text,
    create_year text,
    create_month text,
    score int,
    view_count int,
    body text,
    owner_user_id text,
    last_activity_date text,
    title text,
    tags text,
    answer_count int,
    comment_count int,
    PRIMARY KEY ((create_year, create_month),id)
);


CREATE TABLE IF NOT EXISTS monthly_aggregate_astorm_post(
    create_year text,
    create_month text,
    post_count int,
    PRIMARY KEY (create_year, create_month)
);

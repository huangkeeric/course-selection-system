CREATE DATABASE lesson_selection_system;
USE lesson_selection_system;

CREATE TABLE students (
    student_number CHAR(13) PRIMARY KEY NOT NULL,
    password CHAR(64) NOT NULL,
    name CHAR(64) NOT NULL,

    CHECK (student_number REGEXP '\\d{13}')
);

CREATE TABLE administrators (
    username CHAR(64) PRIMARY KEY NOT NULL,
    password CHAR(64) NOT NULL,
    name CHAR(64) NOT NULL
);

CREATE TABLE courses (
    course_id SMALLINT PRIMARY KEY NOT NULL,
    name VARCHAR(64) NOT NULL,
    introduction VARCHAR(256)
);

CREATE TABLE lessons (
    course_id SMALLINT NOT NULL REFERENCES courses (course_id),
    lesson_index TINYINT NOT NULL,
    teacher VARCHAR(64) NOT NULL,
    -- counting from 0
    `weekday` TINYINT NOT NULL,
    day_start_index TINYINT NOT NULL,
    day_end_index TINYINT NOT NULL,
    location VARCHAR(64) NOT NULL,

    PRIMARY KEY (course_id, lesson_index),
    CHECK (`weekday` BETWEEN 0 AND 6),
    CHECK ((day_start_index BETWEEN 0 AND 11) AND (day_end_index BETWEEN 0 and 11) AND day_start_index <= day_end_index)
);

CREATE TABLE course_selections (
    student_number CHAR(13) NOT NULL REFERENCES students (student_number),
    course_id SMALLINT NOT NULL REFERENCES courses (course_id),
    lesson_index TINYINT NOT NULL,
    PRIMARY KEY (student_number , course_id)
);

CREATE DATABASE cars;
CREATE TABLE trademarks (
                      id SERIAL PRIMARY KEY,
                      name TEXT,
                      model_id int not null references models(id)
);

CREATE TABLE models (
                      id SERIAL PRIMARY KEY,
                      name TEXT

);
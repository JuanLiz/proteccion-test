CREATE TABLE task_users
(
    task_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    CONSTRAINT pk_task_users PRIMARY KEY (task_id, user_id)
);

CREATE UNIQUE INDEX user_u_index ON public.users (username);

ALTER TABLE task_users
    ADD CONSTRAINT fk_tasuse_on_task FOREIGN KEY (task_id) REFERENCES task (id);

ALTER TABLE task_users
    ADD CONSTRAINT fk_tasuse_on_user FOREIGN KEY (user_id) REFERENCES public.users (id);
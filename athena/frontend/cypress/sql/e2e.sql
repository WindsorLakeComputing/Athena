INSERT INTO maintainer(id,
                       employee_id,
                       first_name,
                       last_name,
                       level,
                       rank,
                       first_shift_preference_id,
                       second_shift_preference_id,
                       third_shift_preference_id,
                       section_id,
                       shift_id)
VALUES (9, '5971', 'Henry', 'Arnold', '3', 'TSgt', 1, 2, 3, 2, 2);

INSERT INTO maintainer_certificates(maintainer_id, certificate_id) VALUES(9, 1);
INSERT INTO maintainer_certificates(maintainer_id, certificate_id) VALUES(9, 4);
INSERT INTO maintainer_certificates(maintainer_id, certificate_id) VALUES(9, 7);

INSERT INTO absence(id, end_date, location, reason, start_date, maintainer_id, hours)
VALUES(111, to_date('2019-05-14', 'YYYY-MM-DD'), 'El Paso', 'Rodeo', to_date('2019-05-14', 'YYYY-MM-DD'), 9, '2100-2400');

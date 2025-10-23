CREATE TABLE agency_location (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pos_x DOUBLE NOT NULL,
    pos_y DOUBLE NOT NULL,
    UNIQUE KEY uk_agency_location_posx_posy (pos_x, pos_y)
);
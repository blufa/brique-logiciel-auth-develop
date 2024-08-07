insert into roles values(1, 'ROLE_USER'),(2, 'ROLE_MODERATOR'),(3, 'ROLE_ADMIN');
insert into users values(1, 'admin.mediation@atos.net', '$2a$10$XDwtBVeZ9NklwB36KyJXm.D0GadPf.XdSQzM88.CqxsbvDB11pQ4m', 'admin_med', 'ACTIVATED', false);
insert into user_roles values(1, 3);


CREATE TABLE JPA_SEQUENCES (
	SEQ_KEY VARCHAR(255) NOT NULL PRIMARY KEY,
	SEQ_VALUE NUMERIC(20) NULL DEFAULT NULL
);

-- Users table
CREATE TABLE UM_User
(
  UM_UserId NUMERIC(20) NOT NULL PRIMARY KEY,
  UM_UserCreationDate TIMESTAMP NOT NULL,
  UM_UserModificationDate TIMESTAMP
);

-- User details table
CREATE TABLE UM_UserDetails
(
  UM_UserId NUMERIC(20) NOT NULL PRIMARY KEY REFERENCES UM_User (UM_UserId),
  UM_UserLastName VARCHAR(255),
  UM_UserFirstName VARCHAR(255),
  UM_UserEmailAddress VARCHAR(255),
  UM_UserLocale VARCHAR(255),
  UNIQUE(UM_UserEmailAddress)
);


-- User credentials table
CREATE TABLE UM_UserCredentials
(
  UM_UserId NUMERIC(20) NOT NULL PRIMARY KEY REFERENCES UM_User (UM_UserId),
  UM_UserRealm VARCHAR(255) NOT NULL,
  UM_UserKeycloakId VARCHAR(255) NOT NULL,
  UM_Username VARCHAR(255) NOT NULL,
  UNIQUE(UM_UserRealm, UM_Username)
);
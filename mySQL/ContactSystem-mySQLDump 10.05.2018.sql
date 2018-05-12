
#Achtung oberste Zeile zur DB Erstellung & Löschung falls bereits vorhanden

DROP DATABASE IF EXISTS 'Datenbankname';
CREATE DATABASE IF NOT EXISTS 'Datenbankname';

# USE Statement -> muss vor Beginn der Erstellung von Tabellen aufgeführt werden
# Dies vermittelt, welche DB verwendet werden soll

USE 'Datenbankname';

#
# Tabellenstruktur für Tabelle User
#


CREATE TABLE User (
ID INT(10) NOT NULL,
owner_ID INT(10) NOT NULL REFERENCES contact(ID),
g_token VARCHAR(255) NOT NULL default '',
PRIMARY KEY(ID)
)ENGINE=InnoDB;



#
# Tabellenstruktur für Tabelle BusinessObject
#

# --> Achtung TIMESTAMP Format wird so nicht für alle mySQL DBs angenommen 
# --> FK stellt Owner Rollenbeziehung zwischen BusinessObject und User dar

CREATE TABLE BusinessObject (
bo_ID INT(10) NOT NULL,
user_ID INT(10) NOT NULL,
creationDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
modificationDate TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY(bo_ID),
FOREIGN KEY(user_ID) REFERENCES User(ID)
)ENGINE=InnoDB;


#
# Tabellenstruktur für Tabelle Contact 
#

CREATE TABLE Contact (
ID INT(10)NOT NULL REFERENCES BusinessObject(bo_ID),
owner_ID INT(10) NOT NULL,
status TINYINT(1) NOT NULL,
PRIMARY KEY(ID),
FOREIGN KEY(owner_ID) REFERENCES User(ID)
)ENGINE=InnoDB;

#
# Tabellenstruktur für Tabelle ContactList
# 

CREATE TABLE ContactList (
ID INT(10) NOT NULL REFERENCES BusinessObject(bo_ID),
contactList_name VARCHAR(255) NOT NULL default '',
status TINYINT(1) NOT NULL,
PRIMARY KEY(ID)
)ENGINE=InnoDB;

#
# Tabellenstruktur für Tabelle Property
# 

CREATE TABLE Property (
ID INT(10) NOT NULL REFERENCES BusinessObject(bo_ID),
status TINYINT(1) NOT NULL,
description VARCHAR(255) NOT NULL default '',
PRIMARY KEY(ID)
)ENGINE=InnoDB;

#
# Tabellenstruktur für Tabelle PropertyValue
# 

CREATE TABLE PropertyValue (
ID INT(10) NOT NULL REFERENCES BusinessObject(bo_ID),
contact_ID INT (10) NOT NULL,
value VARCHAR(255) NOT NULL default '',
PRIMARY KEY(ID),
FOREIGN KEY(contact_ID) REFERENCES property(ID)
)ENGINE=InnoDB;

#
# Tabellenstruktur für Tabelle Contact_ContactList
# 

# Darstellung n:m Beziehungstabelle Contact zu Contactlist Variante 2

CREATE TABLE Contact_ContactList(
Contact_ID INT (10) NOT NULL,
ContactList_ID INT (10) NOT NULL,
PRIMARY KEY(Contact_ID, ContactList_ID),
FOREIGN KEY(ContactList_ID) REFERENCES ContactList(ID),
FOREIGN KEY(Contact_ID) REFERENCES Contact(ID)
)ENGINE=InnoDB;

#
# Tabellenstruktur für Tabelle Contact_PropertyValue
# 

# n:m Beziehungstabelle Contact zu PropertyValue Variante 2

CREATE TABLE Contact_PropertyValue(
Contact_ID INT (10) NOT NULL,
propertyValue_ID INT (10) NOT NULL,
PRIMARY KEY(Contact_ID, PropertyValue_ID),
FOREIGN KEY(propertyValue_ID) REFERENCES propertyValue(ID),
FOREIGN KEY(Contact_ID) REFERENCES Contact(ID)
)ENGINE=InnoDB;

#
# Tabellenstruktur für Zusammengesetzte-Tabelle User_BusinessObject (Participation)
# 

# n:m Beziehungstabelle User zu BusinessObject Variante 2 
# Hinweis: stellt --> Rollenbeziehung zwischen User und BO (Participation) dar

CREATE TABLE User_BusinessObject(
BusinessObject_ID INT (10) NOT NULL,
User_ID INT (10) NOT NULL,
PRIMARY KEY(BusinessObject_ID, User_ID),
FOREIGN KEY(BusinessObject_ID) REFERENCES BusinessObject(bo_ID),
FOREIGN KEY(User_ID) REFERENCES User(ID)
)ENGINE=InnoDB;


